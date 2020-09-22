package hu.fzsombor;

import static java.lang.Math.*;
import static org.apache.commons.lang3.RandomUtils.nextFloat;
import static org.apache.commons.lang3.RandomUtils.nextInt;


/**
 * Models the physical properties and sensor values of an electric vehicle, including synthetic failure modes.
 */
class CarModel {

    // Failure Modes
    private static final float SHOCK_DEGRADATION_PROBABILITY = 1F;
    private static final float TIRE_PRESSURE_LOSS_PROBABILITY = 0.5F;
    private static final float VIBRATION_DRIVE_SHAFT_DEGRADATION_PROBABILITY = 1F;
    private static final float OVERHEATING_PROBABILITY = 0.2F;
    private static final float BATTERY_CELL_DEGRADATION_PROBABILITY = 1F;
    private static final float OUTDATED_FIRMWARE_PROBABILITY = 4F;

    // Typical upper soft speed limit of an EV (140km/h = 38.889m/s)
    private static final float MAX_SPEED = 38.889F;

    // Static inertia for more simplicity, also calculated linearly for more simplicity
    private static final float COOLANT_INERTIA = 0.8F;
    private static final float VEHICLE_INERTIA = 0.8F;

    private static final float AIR_SPEED_MULTIPLIER = 4;
    private static final float VIBRATION_AMPLITUDE_MULTIPLIER = 100F;

    // Typical battery voltage is somewhere between 200-260V
    private static final float DISCHARGED_BATTERY_VOLTAGE = 180;
    private static final float FULLY_CHARGED_BATTERY_VOLTAGE = 260F;

    // Probability of running into a bump on the road in percent
    private static final float BUMP_PROBABILITY = 5F;

    private CarData previousSample;

    /* Failure mode state */
    private boolean pressureLossTire1;
    private boolean pressureLossTire2;
    private boolean pressureLossTire3;
    private boolean pressureLossTire4;

    private boolean shockFailure1;
    private boolean shockFailure2;
    private boolean shockFailure3;
    private boolean shockFailure4;

    private boolean driveShaftDegradation;

    private boolean overheatingCoolant;

    private boolean outdatedFirmware;
    private int id;

    public CarModel(int id) {
        this.id = id;
    }


    CarData nextValue() {
        final float previousSpeed;
        final float previousThrottlePos;
        final float intakeAirTemp;
        final float prevBatteryPercentage;
        final float batteryVoltage;
        final float prevIntakeAirSpeed;
        final float previousCoolantTemp;

        boolean failureOccurred = false;
        if (previousSample == null) {
            // (we assume that the time series starts at any point in time during a trip for now)
            previousSpeed = nextFloat(0, 50);

            // Much of the data depends on the throttle position,
            // so we generate a random throttle position first
            previousThrottlePos = nextFloat(0, 1);
            intakeAirTemp = nextFloat(15, 40);
            prevBatteryPercentage = nextFloat(30F, 100F);
            batteryVoltage = DISCHARGED_BATTERY_VOLTAGE +
                    prevBatteryPercentage * (FULLY_CHARGED_BATTERY_VOLTAGE - DISCHARGED_BATTERY_VOLTAGE);
            // We'll assume we're heading straight forward the whole time
            prevIntakeAirSpeed = previousSpeed * AIR_SPEED_MULTIPLIER;

            previousCoolantTemp = nextFloat(intakeAirTemp, intakeAirTemp + 20);
        } else {
            previousSpeed = previousSample.getSpeed();
            previousThrottlePos = max(0, min(previousSample.getThrottlePos() + (0.5F - nextFloat(0F, 1F)), 1));
            intakeAirTemp = previousSample.getIntakeAirTemp();

            prevBatteryPercentage = previousSample.getBatteryPercentage() - previousSpeed * 0.001F;
            batteryVoltage = previousSample.getBatteryVoltage();
            prevIntakeAirSpeed = previousSample.getIntakeAirFlowSpeed();

            previousCoolantTemp = min(max(previousSample.getCoolantTemp() + previousSpeed * 0.008F - intakeAirTemp * 0.1F, 60F), intakeAirTemp);
        }

        // Update state of failure modes. Once an event happens the state is kept across iterations
        this.pressureLossTire1 = pressureLossTire1 || eventHappens(TIRE_PRESSURE_LOSS_PROBABILITY);
        this.pressureLossTire2 = pressureLossTire2 || eventHappens(TIRE_PRESSURE_LOSS_PROBABILITY);
        this.pressureLossTire3 = pressureLossTire3 || eventHappens(TIRE_PRESSURE_LOSS_PROBABILITY);
        this.pressureLossTire4 = pressureLossTire4 || eventHappens(TIRE_PRESSURE_LOSS_PROBABILITY);

        this.shockFailure1 = shockFailure1 || eventHappens(SHOCK_DEGRADATION_PROBABILITY);
        this.shockFailure2 = shockFailure2 || eventHappens(SHOCK_DEGRADATION_PROBABILITY);
        this.shockFailure3 = shockFailure3 || eventHappens(SHOCK_DEGRADATION_PROBABILITY);
        this.shockFailure4 = shockFailure4 || eventHappens(SHOCK_DEGRADATION_PROBABILITY);

        this.driveShaftDegradation = driveShaftDegradation || eventHappens(VIBRATION_DRIVE_SHAFT_DEGRADATION_PROBABILITY);
        this.overheatingCoolant = overheatingCoolant || eventHappens(OVERHEATING_PROBABILITY);
        this.outdatedFirmware = outdatedFirmware || eventHappens(OUTDATED_FIRMWARE_PROBABILITY);

        // Simple labelling
        failureOccurred = pressureLossTire1 || pressureLossTire2 || pressureLossTire3 || pressureLossTire4
                || shockFailure1 || shockFailure2 || shockFailure3 || shockFailure4
                || driveShaftDegradation || overheatingCoolant || outdatedFirmware;

        // The throttle position translates almost directly to current drawn in this model (and also relates to the battery voltage)
        final float currentDraw = max(previousThrottlePos * (abs(260 - batteryVoltage) + 4), 80);

        // We will assume that there is no lag between the coolant temp sensor picking up the change
        // in temp from higher current between cells and engine
        final float coolantTemp = overheatingCoolant ?
                COOLANT_INERTIA * previousCoolantTemp + ((1 - COOLANT_INERTIA) * (previousCoolantTemp + currentDraw * 2.5F))
                : COOLANT_INERTIA * previousCoolantTemp + (1 - COOLANT_INERTIA) * (previousCoolantTemp + currentDraw * 0.5F);

        // Instantaneous acceleration and deceleration with "recuperation" for simplicity
        final float speed = VEHICLE_INERTIA * previousSpeed + (1 - VEHICLE_INERTIA) * (previousThrottlePos * MAX_SPEED);

        // Drive shaft rotation translates directly to the vibration amplitude
        final float engineVibrationAmplitude = driveShaftDegradation ?
                speed * (VIBRATION_AMPLITUDE_MULTIPLIER * 1.5F)
                : speed * VIBRATION_AMPLITUDE_MULTIPLIER;

        final int tirePressure1 = pressureLossTire1 ? nextInt(20, 25) : nextInt(30, 35);
        final int tirePressure2 = pressureLossTire2 ? nextInt(20, 25) : nextInt(30, 35);
        final int tirePressure3 = pressureLossTire3 ? nextInt(20, 25) : nextInt(30, 35);
        final int tirePressure4 = pressureLossTire4 ? nextInt(20, 25) : nextInt(30, 35);

        final boolean bumpHappens = eventHappens(BUMP_PROBABILITY);
        final float accelerometerValue1 = getShockAcceleration(shockFailure1, bumpHappens);
        final float accelerometerValue2 = getShockAcceleration(shockFailure2, bumpHappens);
        final float accelerometerValue3 = getShockAcceleration(shockFailure3, bumpHappens);
        final float accelerometerValue4 = getShockAcceleration(shockFailure4, bumpHappens);

        // Well, this one is kind of lame
        final int firmwareVersion = outdatedFirmware ?
                1000
                : 2000;
        final CarData nextModel = new CarData();
        nextModel.setCarId(id);
        nextModel.setThrottlePos(previousThrottlePos);
        nextModel.setIntakeAirFlowSpeed(prevIntakeAirSpeed);
        nextModel.setIntakeAirTemp(intakeAirTemp);
        nextModel.setSpeed(speed);
        nextModel.setEngineVibrationAmplitude(engineVibrationAmplitude);
        // Accelerometer
        nextModel.setAccelerometer11Value(accelerometerValue1);
        nextModel.setAccelerometer12Value(accelerometerValue2);
        nextModel.setAccelerometer21Value(accelerometerValue3);
        nextModel.setAccelerometer22Value(accelerometerValue4);
        // Tire pressure
        nextModel.setTirePressure11(tirePressure1);
        nextModel.setTirePressure12(tirePressure2);
        nextModel.setTirePressure21(tirePressure3);
        nextModel.setTirePressure22(tirePressure4);
        // Battery
        nextModel.setBatteryPercentage(prevBatteryPercentage);
        nextModel.setBatteryVoltage(batteryVoltage);
        nextModel.setCoolantTemp(coolantTemp);
        nextModel.setCurrentDraw(currentDraw);
        nextModel.setControlUnitFirmware(firmwareVersion);
        // Basic failure mode labelling
        nextModel.setFailureOccurred(failureOccurred);

        previousSample = nextModel;
        return nextModel;
    }

    private float getShockAcceleration(boolean shockFailed, boolean bumpHappens) {
        if (bumpHappens) {
            if (shockFailed) {
                return nextFloat(5, 7);
            } else {
                return nextFloat(2, 3);
            }
        } else {
            if (shockFailed) {
                return nextFloat(3, 4);
            } else {
                return nextFloat(0, 1);
            }
        }
    }

    /**
     * Check if a random event is going to happen based on its percentage
     *
     * @param percentage evenly distributed probability of the event
     * @return {@code true} if the event happens, {@code false} otherwise
     */
    private boolean eventHappens(final float percentage) {
        return percentage > nextFloat(0, 100);
    }


}
