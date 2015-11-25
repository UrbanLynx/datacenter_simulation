package Simulation.Data;


import Simulation.Master.Utils.SlaveDesc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class SimulationConfig {
    public String taskFileName;
    public String hostFile;
    //public Boolean isVarys; // TODO: replace by SimulationType
    public SimulationType simulationType;
    public String varysMasterUrl;
    public int serverPort = 8080; // TODO: delete, we have slavePort for this
    public int slavePort = 5000;

    public Boolean doGenerateFiles;
    public String fileDirectory;
    public int fileSizeKBMin;
    public int fileSizeKBMax;
    public int fileSizeKBStep;

    public HashMap<Integer, SlaveDesc> hosts;
}
