package Simulation.Data;


import Simulation.Simulation;
import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class SimulationConfig {
    public String taskFileName;
    public Boolean isVarys; // TODO: replace by SimulationType
    public String varysMasterUrl;
    public int serverPort = 8080; // TODO: delete, we have slavePort for this
    public int slavePort = 5000;
    public ArrayList<String> hosts;
}
