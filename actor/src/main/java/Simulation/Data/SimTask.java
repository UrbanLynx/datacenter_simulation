package Simulation.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */

public class SimTask implements Serializable{

    public SimulationType simulationType;
    public String coflowId;
    public String masterUrl;
    public TaskData data;

    public double startTime;

    public int mapperCount;
    public ArrayList<Integer> mappers;
    public int reducerCount;
    public HashMap<Integer, Reducer> reducers;

    public int currentSlaveId;
}


