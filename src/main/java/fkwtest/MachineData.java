package fkwtest;

import java.text.SimpleDateFormat;
import java.util.Date;
public class MachineData {
    public MachineData(String engineName, Date startDate, int i) {
    }

    private int EngineId;
    private String EngineName;
    private Date StartDate;
    private int vTemp;

    public MachineData(byte[] message) {
    }

    public MachineData(int id, String name, Date dt, int vtemp) {
        this.EngineId = id;
        this.EngineName = name;
        this.vTemp = vtemp;
        this.StartDate = dt;
    }

    public MachineData() {

    }


    int getEngineId() {
        return EngineId;
    }

    public void setEngineId(int engineId) {
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

    int getvTemp() {
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


