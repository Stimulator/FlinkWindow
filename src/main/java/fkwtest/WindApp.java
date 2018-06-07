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

public class WindApp {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment ( );
        env.registerType (MachineData.class);

        env.setParallelism (2);
        env.enableCheckpointing (10, CheckpointingMode.EXACTLY_ONCE);
        env.setStreamTimeCharacteristic (TimeCharacteristic.ProcessingTime);


        DataStream <MachineData> stream = env.addSource (new DataCollector1 ( ));

        /*if (stream.filter (reading -> reading.getvTemp ( ) > 100 && reading.getvTemp ( ) < 100) != null) {
            stream
                    .keyBy (reading -> reading.getvTemp ( ))
                    .window (SlidingTimeWindows.of (Time.seconds (10), Time.seconds (10)))
                    .apply (new WindowFunction <MachineData, Object, Integer, TimeWindow> ( ) {
                        @Override
                        public void apply(Integer integer, TimeWindow window, Iterable <MachineData> iterable, Collector <Object> collector)
                                throws Exception {
                            int agg = 0;

                            for (MachineData r : iterable) {
                                agg += r.getvTemp ( );
                            }
                            collector.collect (integer);
                        }
                    }).map (reading -> "-- Mixed Readings ##### RESETTING WINDOW! @@@ READINGS: " + reading).print ( );

            System.out.println ("Mixed Reading Execution TRIGGERED!");

        }*/
        if (stream.filter (reading -> reading.getvTemp ( ) < 100)
                .map (reading -> " == Normal Readings ## RESETTING WINDOW @@@@ Readings: " + reading).print () != null) {
            stream.keyBy (reading -> reading.getvTemp ( ))
                    .window (SlidingTimeWindows.of (Time.seconds (10), Time.seconds (10)))
                    .apply (new WindowFunction <MachineData, Object, Integer, TimeWindow> ( ) {
                        @Override
                        public void apply(Integer integer, TimeWindow window, Iterable <MachineData> iterable, Collector <Object> collector) throws Exception {
                            int cout = 0;
                            for (MachineData r : iterable) {
                                cout += r.getvTemp ( );
                            }
                            collector.collect (integer);
                        }
                    }).map (reading -> "!!! READINGS: " + reading).print ( );

            System.out.println ("Normal Reading Execution TRIGGERED!");

        } else {
            stream.filter (reading -> reading.getvTemp ( ) > 100)
                    .map (reading -> "~~ WARNING Reading Aobve Threshold ## ALERTING WINDOW @@@@ Readings: " + reading).print ();

            {
                stream.keyBy (reading -> reading.getvTemp ( ))
                        .window (SlidingTimeWindows.of (Time.seconds (10), Time.seconds (10)))
                        .apply (new WindowFunction <MachineData, Object, Integer, TimeWindow> ( ) {
                            @Override
                            public void apply(Integer integer, TimeWindow window, Iterable <MachineData> iterable, Collector <Object> collector) throws Exception {
                                int cout = 0;
                                for (MachineData r : iterable) {
                                    cout += r.getvTemp ( );
                                }
                                collector.collect (integer);
                            }
                        }).map (reading -> "!!! READINGS: " + reading).print ( );
                System.out.println ("ALERT Reading Execution TRIGGERED!");
            }
        }
        env.execute ("Window Executed!");

    }
}

