package hu.fzsombor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CarDataProducer {
    final private int id;
    private CarModel model;
    Producer<String, String> producer;
    String topicName;
    private final String KAFKA_CLUSTER_ENV_VAR_NAME = "KAFKA_CLUSTER";

    public CarModel getModel() {
        return model;
    }

    public CarDataProducer(int id, String kafkaCluster) {
        this.id = id;
        this.model = new CarModel(id);

        //Assign topicName to string variable
        this.topicName = "car" + id;

        // create instance for properties to access producer configs
        Properties props = new Properties();

        String servers = System.getProperty(KAFKA_CLUSTER_ENV_VAR_NAME,
                kafkaCluster);
        //Assign kafka cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        //Set acknowledgements for producer requests.
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        //If the request fails, the producer can automatically retry,
        props.put(ProducerConfig.RETRIES_CONFIG, 0);

        //Specify buffer size in config
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        //Reduce the no of requests less than 0
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }


    public void nextPayload() {
        final CarData value = model.nextValue();
        producer.send(new ProducerRecord<>(topicName, Integer.toString(value.getCarId()), value.getJSON().toString()));
    }

    public void close() {
        producer.close();
    }

}

