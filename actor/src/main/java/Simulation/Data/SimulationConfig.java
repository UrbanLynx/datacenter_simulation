package Simulation.Data;


import Simulation.Simulation;
import org.joda.time.DateTime;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class SimulationConfig {
    public String taskFileName;
    public Boolean isVarys;
    public String varysMasterUrl;
    public int serverPort = 8080;
    public int slavePort = 1000;
}
