package Simulation;

import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class TraditionalSimulationTest extends TestCase {

    public void testExecuteTask() throws Exception {
        SimTask task = new SimTask();
        task.dstAddress = "localhost";
        task.dstPort = 8080;
        task.size = 10000000;
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        tasks.add(task);

        SimulationConfig config = new SimulationConfig();
        config.serverPort = 8080;

        TraditionalSimulation simulation = new TraditionalSimulation(tasks, config);
        simulation.receiveAll();
        Thread.sleep(1000);
        simulation.executeTask(task);
        simulation.stopSimulation();
    }

}