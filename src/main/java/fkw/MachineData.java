package fkw;

import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.io.*;
import java.util.Date;
public class MachineData {
    public MachineData(String s, String value) {
    }

    private String EngineId;
    private String EngineName;
    private Date StartDate;
    private int vTemp;

    public MachineData() {
    }

    public MachineData(String id, String name, Date dt, int vtemp) {
        this.EngineId = id;
        this.EngineName = name;
        this.vTemp = vtemp;
        this.StartDate = dt;
    }


    public String getEngineId() {
        return EngineId;
    }

    public void setEngineId(String engineId) {
        EngineId = engineId;
    }

    public String getEngineName() {
        return EngineName;
    }

    public void setEngineName(String engineName) {
        EngineName = engineName;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public int getvTemp() {
        return vTemp;
    }

    public void setvTemp(int vTemp) {
        this.vTemp = vTemp;
    }

    @Override
    public String toString() {
        return '(' + getEngineName () + '/' + getEngineId () + ") @ " + getStartDate () + ":" + getvTemp ();
    }
}

