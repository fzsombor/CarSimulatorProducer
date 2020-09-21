package hu.fzsombor;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        final int numIterations = getNumOrDefault(args, 0, 1);
        final int numCars = getNumOrDefault(args, 1, 1);

        CarDataProducer carDataProducer = new CarDataProducer(1);
        System.out.println("result:");
        System.out.println(carDataProducer.getModel().nextValue().getJSON());

      /*  List<CarDataProducer> producers = new ArrayList<>();
        for (int i = 0; i< numCars; ++i){
            CarDataProducer producer = new CarDataProducer(i);
            producers.add(producer);
        }
        for (CarDataProducer producer : producers) {
            for (int i = 0; i < numIterations; ++i) {
                producer.getModel().nextValue();
            }
        }*/

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
