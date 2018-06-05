package fkw;

import org.apache.kafka.clients.producer.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class KafkaProducer1 {


    public static void main(String[] args) throws Exception{


        String topicName = "fkwin";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "fkw.SerializingData");

        Producer<String, MachineData> producer = new KafkaProducer <>(props);

        DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
        MachineData m1 = new MachineData ("101","Eng1",df.parse("2016-04-01"),95);
        MachineData m2 = new MachineData ("102","Eng2",df.parse("2012-01-01"),105);

        producer.send(new ProducerRecord<>(topicName,"Reading1: ",m1));
        producer.send(new ProducerRecord<>(topicName,"Reading2: ",m2));

        System.out.println("Producer Completed!");
        producer.close();

    }
}

