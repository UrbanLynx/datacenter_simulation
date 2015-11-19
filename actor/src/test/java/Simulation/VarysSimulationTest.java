package Simulation;

import Simulation.Data.SimulationConfig;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 03.11.2015.
 */
public class VarysSimulationTest extends TestCase {

    public void testExecuteTask() throws Exception {
        SimTask task = new SimTask();
        task.host = "localhost";
        task.port = 8080;
        task.dataSize = 10;
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        tasks.add(task);

        SimulationConfig config = new SimulationConfig();
        config.serverPort = 8080;

        VarysSimulation simulation = new VarysSimulation(tasks, config);
        simulation.receiveAll();
        Thread.sleep(1000);
        simulation.executeTask(task);
        simulation.stopSimulation();
    }
}