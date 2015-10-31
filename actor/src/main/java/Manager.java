import Simulation.Simulation;
import Simulation.SimTask;
import Simulation.VarysSimulation;
import Simulation.TraditionalSimulation;

import Simulation.SimulationConfig;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class Manager {
    public static void main(String[] args){
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

    public static Simulation createSimulation(SimulationConfig simConfig){
        ConfigParser parser = new ConfigParser();
        ArrayList<SimTask> simTasks = parser.parseTaskFile(simConfig.taskFileName);
        if (simConfig.isVarys){
            return new VarysSimulation(simTasks, simConfig);
        } else{
            return new TraditionalSimulation(simTasks, simConfig);
        }
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
