package hu.fzsombor;

import org.json.JSONObject;

public class CarData {

    private float coolantTemp;
    private float intakeAirTemp;
    private float intakeAirFlowSpeed;
    private float batteryPercentage;
    private float batteryVoltage;
    private float currentDraw;
    private float speed;
    private float engineVibrationAmplitude;
    private float throttlePos;
    private float tirePressure11;
    private float tirePressure12;
    private float tirePressure21;
    private float tirePressure22;
    private float accelerometer11Value;
    private float accelerometer12Value;
    private float accelerometer21Value;
    private float accelerometer22Value;
    private int controlUnitFirmware;
    private boolean failureOccurred;
    private int carId;

    public CarData() {
    }

    // creating the instance from a JSON string
    public CarData(String JSON) {
        JSONObject obj = new JSONObject(JSON);

        this.carId = obj.getInt("id");
        this.coolantTemp = obj.getFloat("coolantTemp");
        this.intakeAirTemp = obj.getFloat("intakeAirTemp");
        this.intakeAirFlowSpeed = obj.getFloat("intakeAirFlowSpeed");
        this.batteryPercentage = obj.getFloat("batteryPercentage");
        this.batteryVoltage = obj.getFloat("batteryVoltage");
        this.currentDraw = obj.getFloat("currentDraw");
        this.speed = obj.getFloat("speed");
        this.engineVibrationAmplitude = obj.getFloat("engineVibrationAmplitude");
        this.throttlePos = obj.getFloat("throttlePos");
        this.tirePressure11 = obj.getFloat("tirePressure11");
        this.tirePressure12 = obj.getFloat("tirePressure12");
        this.tirePressure21 = obj.getFloat("tirePressure21");
        this.tirePressure22 = obj.getFloat("tirePressure22");
        this.accelerometer11Value = obj.getFloat("accelerometer11Value");
        this.accelerometer12Value = obj.getFloat("accelerometer12Value");
        this.accelerometer21Value = obj.getFloat("accelerometer21Value");
        this.accelerometer22Value = obj.getFloat("accelerometer22Value");
        this.controlUnitFirmware = obj.getInt("controlUnitFirmware");
        this.failureOccurred = obj.getBoolean("failureOccurred");
    }

    public float getCoolantTemp() {
        return coolantTemp;
    }

    public void setCoolantTemp(float coolantTemp) {
        this.coolantTemp = coolantTemp;
    }

    public float getIntakeAirTemp() {
        return intakeAirTemp;
    }

    public void setIntakeAirTemp(float intakeAirTemp) {
        this.intakeAirTemp = intakeAirTemp;
    }

    public float getIntakeAirFlowSpeed() {
        return intakeAirFlowSpeed;
    }

    public void setIntakeAirFlowSpeed(float intakeAirFlowSpeed) {
        this.intakeAirFlowSpeed = intakeAirFlowSpeed;
    }

    public float getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(float batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public float getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(float batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public float getCurrentDraw() {
        return currentDraw;
    }

    public void setCurrentDraw(float currentDraw) {
        this.currentDraw = currentDraw;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getEngineVibrationAmplitude() {
        return engineVibrationAmplitude;
    }

    public void setEngineVibrationAmplitude(float engineVibrationAmplitude) {
        this.engineVibrationAmplitude = engineVibrationAmplitude;
    }

    public float getThrottlePos() {
        return throttlePos;
    }

    public void setThrottlePos(float throttlePos) {
        this.throttlePos = throttlePos;
    }

    public float getTirePressure11() {
        return tirePressure11;
    }

    public void setTirePressure11(float tirePressure11) {
        this.tirePressure11 = tirePressure11;
    }

    public float getTirePressure12() {
        return tirePressure12;
    }

    public void setTirePressure12(float tirePressure12) {
        this.tirePressure12 = tirePressure12;
    }

    public float getTirePressure21() {
        return tirePressure21;
    }

    public void setTirePressure21(float tirePressure21) {
        this.tirePressure21 = tirePressure21;
    }

    public float getTirePressure22() {
        return tirePressure22;
    }

    public void setTirePressure22(float tirePressure22) {
        this.tirePressure22 = tirePressure22;
    }

    public float getAccelerometer11Value() {
        return accelerometer11Value;
    }

    public void setAccelerometer11Value(float accelerometer11Value) {
        this.accelerometer11Value = accelerometer11Value;
    }

    public float getAccelerometer12Value() {
        return accelerometer12Value;
    }

    public void setAccelerometer12Value(float accelerometer12Value) {
        this.accelerometer12Value = accelerometer12Value;
    }

    public float getAccelerometer21Value() {
        return accelerometer21Value;
    }

    public void setAccelerometer21Value(float accelerometer21Value) {
        this.accelerometer21Value = accelerometer21Value;
    }

    public float getAccelerometer22Value() {
        return accelerometer22Value;
    }

    public void setAccelerometer22Value(float accelerometer22Value) {
        this.accelerometer22Value = accelerometer22Value;
    }

    public int getControlUnitFirmware() {
        return controlUnitFirmware;
    }

    public void setControlUnitFirmware(int controlUnitFirmware) {
        this.controlUnitFirmware = controlUnitFirmware;
    }

    public boolean isFailureOccurred() {
        return failureOccurred;
    }

    public void setFailureOccurred(boolean failureOccurred) {
        this.failureOccurred = failureOccurred;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    // getting the JSON object of the instance
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", carId);
        obj.put("coolantTemp", coolantTemp);
        obj.put("intakeAirTemp", intakeAirTemp);
        obj.put("intakeAirFlowSpeed", intakeAirFlowSpeed);
        obj.put("batteryPercentage", batteryPercentage);
        obj.put("batteryVoltage", batteryVoltage);
        obj.put("currentDraw", currentDraw);
        obj.put("speed", speed);
        obj.put("engineVibrationAmplitude", engineVibrationAmplitude);
        obj.put("throttlePos", throttlePos);
        obj.put("tirePressure11", tirePressure11);
        obj.put("tirePressure12", tirePressure12);
        obj.put("tirePressure21", tirePressure21);
        obj.put("tirePressure22", tirePressure22);
        obj.put("accelerometer11Value", accelerometer11Value);
        obj.put("accelerometer12Value", accelerometer12Value);
        obj.put("accelerometer21Value", accelerometer21Value);
        obj.put("accelerometer22Value", accelerometer22Value);
        obj.put("controlUnitFirmware", controlUnitFirmware);
        obj.put("failureOccurred", failureOccurred);
        return obj;
    }
}
