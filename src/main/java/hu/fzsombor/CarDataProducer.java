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
    final String TOPIC_NAME = "carstream";

    public CarDataProducer(int id, String kafkaCluster) {
        this.id = id;
        this.model = new CarModel(id);
        //Assign topicName to string variable
        // create instance for properties to access producer configs
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        // create the producer from the config
        producer = new KafkaProducer<>(props);
        // get the initial state of the CarModel
        model.nextValue();
    }

    public void nextPayload() {
        final CarData value = model.nextValue();
        String payload = value.getJSON().toString();
        producer.send(new ProducerRecord<>(TOPIC_NAME, Integer.toString(id), payload));
        producer.flush();
    }

    public void close() {
        producer.close();
    }

}

