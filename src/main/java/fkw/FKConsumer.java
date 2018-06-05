package fkw;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.Properties;

public class FKConsumer implements SourceFunction<MachineData> {


    @Override
    public void run(SourceContext<MachineData> sourceContext) throws Exception {

        Properties properties = new Properties ();
        properties.setProperty ("zookeeper.connect", "localhost:2181");
        properties.setProperty ("bootstrap.servers", "localhost:9092");
        properties.put ("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put ("value.deserializer", "Fkwindow.DeserializingData");
        properties.setProperty ("group.id","fkwin");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment ();
        env.enableCheckpointing (5000);
        DataStream<String> stream = env.addSource (new FlinkKafkaConsumer082 <> ("fkwin",
                new SimpleStringSchema (), properties));
        stream.addSink (new PrintSinkFunction <> ());
        env.execute ();
    }


    @Override
    public void cancel() {

    }
}