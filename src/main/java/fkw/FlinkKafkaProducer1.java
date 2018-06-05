package fkw;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.SerializationSchema;



public class FlinkKafkaProducer1{


    private static StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs (args);

        DataStream<String>messageStream = executionEnvironment.addSource (new TestDataGenerate1 ());

        messageStream.addSink (
                new FlinkKafkaProducer <> (parameterTool.getRequired ("bootstrap.servers"),
                        parameterTool.getRequired ("topic"), new SimpleStringSchema ()));

        executionEnvironment.execute ();
    }

    public static class SimpleStringSchema implements SerializationSchema<String, byte[]> {
        private static final long serialVersionUID = 1L;

        SimpleStringSchema() {
        }


        public byte[] serialize(String element) {
            return element.getBytes();
        }

    }
}

