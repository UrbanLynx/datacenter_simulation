package Simulation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class TraditionalSimulation extends Simulation  {
    public SimulationConfig simulationConfig;
    public TraditionalClient client = new TraditionalClient();

    public TraditionalSimulation(ArrayList<SimTask> simTasks, SimulationConfig simConfig) {
        super(simTasks);
        simulationConfig = simConfig;
    }

    public void init() {

    }

    @Override
    public void executeTask(SimTask task) {
        String data = GenerateData(task.dataSize);
        client.sendTo(task.host,task.port,data);
    }

    private String GenerateData(int dataSize) {
        return null;
    }


    public void receiveAll(){
        TraditionalServer server = new TraditionalServer(simulationConfig.serverPort);
        new Thread(server).start();
        /*ServerSocket ssock = new ServerSocket(simulationConfig.serverPort);
        System.out.println("Listening");
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
        }*/
    }
}
