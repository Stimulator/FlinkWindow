package fkwtest;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DataCollector implements SourceFunction<MachineData> {
    boolean running = true;
    @Override
    public void run(SourceContext<MachineData> sourceContext) throws Exception {
       // while ( running ) {}
            DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
            MachineData m1 = new MachineData (101, "Eng1", df.parse ("2016-04-01"), 90);
            MachineData m2 = new MachineData (102, "Eng2", df.parse ("2016-04-02"), 105);
            MachineData m3 = new MachineData (103, "Eng2", df.parse ("2016-04-02"), 98);
            MachineData m4 = new MachineData (104, "Eng2", df.parse ("2016-04-02"), 106);

            sourceContext.collect (m1);
            sourceContext.collect (m2);
            sourceContext.collect (m3);
            sourceContext.collect (m4);


    }

    @Override
    public void cancel() {
        running = false;
    }
}
