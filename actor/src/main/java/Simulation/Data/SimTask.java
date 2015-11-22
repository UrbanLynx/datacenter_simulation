package Simulation.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */

public class SimTask implements Serializable{

    public SimulationType simulationType;
    //public String url;
    public String coflowId;
    public String masterUrl;
    public TaskData data;

    public String dstAddress;
    public int dstPort;
    //public double duration;
    //public Double finishTime;
    //public String mapper;
    //public String reducer;
    public String srcAddress;
    public int srcPort;
    public long size;
    public double startTime;
    public int mapperCount;
    public ArrayList<Integer> mappers;
    public int reducerCount;
    public ArrayList<Reducer> reducers;

    //public boolean continueSimulation;
}


