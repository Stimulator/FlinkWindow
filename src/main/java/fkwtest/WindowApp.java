package fkwtest;


import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.FoldAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.RichAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;


public class WindowApp {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment ();
        env.registerType (MachineData.class);

        env.setParallelism (2);
        env.enableCheckpointing (10, CheckpointingMode.EXACTLY_ONCE);
        env.setStreamTimeCharacteristic (TimeCharacteristic.ProcessingTime);


        DataStream<MachineData> stream = env.addSource (new DataCollector ());

        stream
                .filter (reading  -> reading.getvTemp ()<100)
                .map (reading -> "== Normal Reading: " + reading)
                .print ();


        stream
                .filter (reading -> reading.getvTemp ()>100)
                .map (reading -> "-- ALERT Reading Above Threshold!: " + reading)
                .print ();

        stream
                .filter (reading -> reading.getvTemp () > 100 && reading.getvTemp ()<100)
                .keyBy (reading -> reading.getvTemp ())
                .window (SlidingTimeWindows.of (Time.seconds (10),Time.seconds (10)))
                .apply (new WindowFunction <MachineData, Object, Integer, TimeWindow> ( ) {
                    @Override
                    public void apply(Integer integer, TimeWindow window, Iterable <MachineData> iterable, Collector <Object> collector)
                            throws Exception {
                        int agg = 0;

                        for (MachineData r : iterable){
                            agg += r.getvTemp ();
                        } collector.collect (integer);
                }
                }) .map (reading -> "~~ Warning Reading ABOVE THRESHOLD with Normal Reading Will Reset: " + reading).print ();



        stream
                .keyBy (reading -> reading.getvTemp ())
                .window (SlidingTimeWindows.of (Time.seconds (10),Time.seconds (10)))
                .apply (new WindowFunction <MachineData, Object, Integer, TimeWindow> ( ) {

                    @Override
                    public void apply(Integer integer, TimeWindow window, Iterable <MachineData> iterable, Collector <Object> collector)
                            throws Exception {
                        int agg = 0;

                        for (MachineData r : iterable){
                            agg += r.getvTemp ();
                        } collector.collect (integer);

                    }

                }).map (stat -> "## Readings: " + stat).print ();

        env.execute ("Window Executed!");

    }


}



