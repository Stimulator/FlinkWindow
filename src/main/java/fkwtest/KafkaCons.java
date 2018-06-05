package fkwtest;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaCons implements SourceFunction<MachineData> {


    @Override
    public void run(SourceContext ctx) throws Exception {

        String topicName = "fkwin";
        String groupName = "myGroup";

        Properties props = new Properties ( );
        props.put ("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put ("group.id", groupName);
        props.put ("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put ("value.deserializer", "fkw.DeserializingData");


        KafkaConsumer <String, MachineData> consumer = new KafkaConsumer <> (props);
        consumer.subscribe (Collections.singletonList (topicName));
        boolean running = true;
        try {
            while ( running ) {

                ConsumerRecords <String, MachineData> records = consumer.poll (500);
                for (TopicPartition partition : records.partitions ()) {
                    List<ConsumerRecord<String, MachineData>> partitionRecords = records.records (partition);
                    for (ConsumerRecord <String, MachineData> record : partitionRecords) {
                        System.out.println ("Readings: " + record.value ( ).getEngineId());
                        ctx.collect ("Engine-id= " + record.value ( ).getEngineId() +
                                " Engine-Name = "
                                + record.value ( ).getEngineName ( )
                                + " Engine-StartDate = " + record.value ( ).getStartDate ( ).toString ( ) +
                                " Engine-Temperature= " + record.value ( ).getvTemp ( ));
                        System.out.println ("Engine-id= " + record.value ( ).getEngineId() +
                                " Engine-Name = "
                                + record.value ( ).getEngineName ( )
                                + " Engine-StartDate = " + record.value ( ).getStartDate ( ).toString ( ) +
                                " Engine-Temperature= " + record.value ( ).getvTemp ( ));
                    }
                    long lastOffset = partitionRecords.get (partitionRecords.size () -1).offset ();
                    consumer.commitSync (Collections.singletonMap (partition, new OffsetAndMetadata (lastOffset +1)));

                    running = false;
                }
            }
        } finally {
            consumer.close();
        }
    }

    @Override
    public void cancel() {

    }
}
