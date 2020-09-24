package hu.fzsombor;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        // getting the parameters from the CL args
        final int numIterations = getNumOrDefault(args, 0, 3000);
        final int numCars = getNumOrDefault(args, 1, 1);
        final String kafkaCluster = args[2];

        // create the cars
        List<CarDataProducer> producers = new ArrayList<>();
        for (int i = 0; i < numCars; ++i) {
            CarDataProducer producer = new CarDataProducer(i, kafkaCluster);
            producers.add(producer);
        }

        // majority of the work gets done here
        for (int i = 0; i < numIterations; ++i)
            for (CarDataProducer producer : producers)
                producer.nextPayload();
        for (CarDataProducer producer : producers)
            producer.close();

    }

    private static int getNumOrDefault(final String[] args, final int index, final int defaultNum) {
        if (args.length > index) {
            final String argString = args[index];
            final int count = Integer.parseInt(argString);
            if (count > 0) {
                return count;
            } else {
                System.err.println("Parameter must be a positive integer");
            }
        }
        return defaultNum;
    }

}
