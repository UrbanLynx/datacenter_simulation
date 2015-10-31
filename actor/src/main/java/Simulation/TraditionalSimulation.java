package Simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class TraditionalSimulation extends Simulation  {
    public SimulationConfig simulationConfig;
    public TraditionalClient client;
    public TraditionalServer server;

    public TraditionalSimulation(ArrayList<SimTask> simTasks, SimulationConfig simConfig) {
        super(simTasks);
        simulationConfig = simConfig;
        client = new TraditionalClient();
        server = new TraditionalServer(simulationConfig.serverPort);
    }

    public void init() {

    }

    @Override
    public void executeTask(SimTask task) {
        byte[] data = GenerateData(task.dataSize);
        client.sendTo(task.host,task.port,data);
    }

    private byte[] GenerateData(int dataSize) {
        byte[] b = new byte[dataSize];
        new Random().nextBytes(b);
        return b;
    }


    public void receiveAll(){
        new Thread(server).start();
        /*ServerSocket ssock = new ServerSocket(simulationConfig.serverPort);
        System.out.println("Listening");
        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
        }*/
    }

    public void stopSimulation(){
        server.stop();
    }
}
