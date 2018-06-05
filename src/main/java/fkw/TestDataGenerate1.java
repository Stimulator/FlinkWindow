package fkw;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TestDataGenerate1 implements  SourceFunction<String> {
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
        MachineData m1 = new MachineData ("101","Eng1",df.parse("2016-04-01"), 90);
        MachineData m2 = new MachineData ("102","Eng1",df.parse("2016-04-01"), 106);

        ctx.collect ("Reading: " + m1);
        ctx.collect ("Reading: " + m2);
    }

    @Override
    public void cancel() {

    }
}
