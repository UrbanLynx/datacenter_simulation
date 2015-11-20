import Simulation.Data.ConfigParser;
import Simulation.Data.SimTask;
import Simulation.Simulation;
import Simulation.TraditionalSimulation;

import Simulation.Data.SimulationConfig;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class Manager {
    public static void main(String[] args) throws IOException, ParseException {
        SimulationConfig simConfig = readConfig("configs/simulation.json");
        Simulation simulation = createSimulation(simConfig);
        waitForStartOfSimulation(simConfig);
        conductSimulation(simulation);
        processResultsOfSimulation(simulation);
    }

    public static SimulationConfig readConfig(String filename){
        ConfigParser parser = new ConfigParser();
        return parser.parseSimConfigFile(filename);
    }

    public static Simulation createSimulation(SimulationConfig simConfig) throws IOException, ParseException {
        ConfigParser parser = new ConfigParser();
        ArrayList<SimTask> simTasks = parser.parseTaskFile(simConfig.taskFileName);
        //if (simConfig.isVarys){
            //return new VarysSimulation(simTasks, simConfig);
        //} else{
            return new TraditionalSimulation(simTasks, simConfig);
        //}
    }

    public static void waitForStartOfSimulation(SimulationConfig simConfig){
        return;
    }

    public static void conductSimulation(Simulation simulation){
        simulation.conductSimulation();
    }

    public static void processResultsOfSimulation(Simulation simulation){
        return;
    }
}
