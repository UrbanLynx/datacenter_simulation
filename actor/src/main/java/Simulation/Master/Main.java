package Simulation.Master;

import Simulation.Data.ConfigParser;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
        SimulationConfig simConfig = ConfigParser.parseSimConfigFile("configs/simulation.json");
        ConfigParser.parseHostsFile(simConfig);
        ArrayList<SimTask> simTasks = ConfigParser.parseTaskFile(simConfig.taskFileName);
        Master master = new Master();
        master.conductSimulation(simConfig, simTasks);
    }
}
