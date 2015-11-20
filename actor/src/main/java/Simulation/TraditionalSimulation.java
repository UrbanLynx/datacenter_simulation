package Simulation;

import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;

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
        String data = GenerateData(task.size);
        for (int i =0 ;i < 1024; ++i)
            client.sendTo(task.dstAddress,task.dstPort,data);
    }

    private String GenerateData(long dataSize) {
        // data Size is larger than int maximum.
        // Convert into kilobytes
        String s = "";
        for (int i=0; i<dataSize; ++i)
            s.concat(String.valueOf(new Random().nextInt()));
        return s;
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
