package Simulation.Data;

import org.joda.time.DateTime;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class SimTask {

    public SimulationType simulationType;
    //public String url;
    public String coflowId;
    public String masterUrl;
    public TaskData data;

    public String dstAddress;
    public int dstPort;
    public double duration;
    public Double finishTime;
    public String mapper;
    public String reducer;
    public long size;
    public String srcAddress;
    public int srcPort;
    public Double startTime;

    public boolean continueSimulation;
}

