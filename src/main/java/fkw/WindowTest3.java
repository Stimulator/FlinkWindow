package fkw;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ProcessingTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class WindowTest3 {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment ();
        env.registerType (Statistic.class);
        env.registerType (MachineData.class);

        env.setParallelism (2);
        env.setStreamTimeCharacteristic (TimeCharacteristic.EventTime);

        DataStreamSource<MachineData> readStream = env.addSource (new FKConsumer ());

        readStream
                .filter (reading -> reading.getvTemp () > 100)
                .map (reading -> " -- ALERT Reading Above Threshold!: " + reading).print ();

        readStream.windowAll (SlidingTimeWindows.of (Time.seconds (10),Time.seconds (10)));

        env.execute ();
    }


}

