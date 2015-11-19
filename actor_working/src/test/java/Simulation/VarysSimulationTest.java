package Simulation;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 03.11.2015.
 */
public class VarysSimulationTest extends TestCase {

    public void testSender() throws Exception {
        SimulationConfig config = new SimulationConfig();
        config.serverPort = 8080;
        config.varysMasterUrl = "varys://stanislavs-air:1606";

        VarysSender sender = new VarysSender(config);
        sender.sendFake();
    }

    public void testExecuteTask() throws Exception {
        SimTask task = new SimTask();
        task.host = "localhost";
        task.port = 8080;
        task.dataSize = 10;
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        tasks.add(task);

        SimulationConfig config = new SimulationConfig();
        config.serverPort = 8080;
        config.varysMasterUrl = "varys://stanislavs-air:1606";

        VarysSimulation simulation = new VarysSimulation(tasks, config);
        //simulation.receiveAll();
        //Thread.sleep(1000);
        simulation.executeTask(task);
        Thread.sleep(10000);
        simulation.receiveAll();
        Thread.sleep(10000);
        //simulation.stopSimulation();

    }


}