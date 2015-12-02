package Simulation.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */

public class SimTask implements Serializable{



    public SimTask(){}
    public SimTask(SimTask task){
        this.simulationType = task.simulationType;
        this.coflowId = task.coflowId;
        this.masterUrl = task.masterUrl;
        this.data = task.data;
        this.startTime = task.startTime;
        this.mapperCount = task.mapperCount;
        this.mappers = task.mappers;
        this.reducerCount = task.reducerCount;
        this.reducers = task.reducers;
        this.reducersArr = task.reducersArr;
        this.currentSlaveId = task.currentSlaveId;
        this.currentSlavePort = task.currentSlavePort;
        this.id = task.id;
        this.currentSlaveTaskIndex = task.currentSlaveTaskIndex;
        this.masterListenerPort = task.masterListenerPort;
        this.masterListenerIp = task.masterListenerIp;
    }

    public SimulationType simulationType;
    public String coflowId;
    public String masterUrl;
    public TaskData data;

    public double startTime;

    public int mapperCount;
    public ArrayList<Integer> mappers;
    public int reducerCount;
    public HashMap<Integer, Reducer> reducers;
    public ArrayList<Reducer> reducersArr;

    public int currentSlaveId;
    public int currentSlavePort;
    public int currentSlaveTaskIndex;

    public Integer id;

    public int masterListenerPort;
    public String masterListenerIp;
}


