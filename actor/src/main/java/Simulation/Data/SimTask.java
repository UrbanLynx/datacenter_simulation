package Simulation.Data;

import org.joda.time.DateTime;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class SimTask {

    public SimulationType simulationType;
    public String coflowId;
    public TaskData data;

    public String dstAddress;
    public int dstPort;
    public double duration;
    public long finishTime;
    public String mapper;
    public String reducer;
    public long size;
    public String srcAddress;
    public int srcPort;
    public long startTime;
}

