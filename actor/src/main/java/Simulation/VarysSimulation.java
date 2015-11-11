package Simulation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class VarysSimulation extends Simulation {
    public SimulationConfig simulationConfig;
    public VarysSender client;
    public VarysReceiver server;

    public VarysSimulation(ArrayList<SimTask> simTasks, SimulationConfig simConfig) {
        super(simTasks);
        simulationConfig = simConfig;
        client = new VarysSender(simConfig);
        server = new VarysReceiver(simConfig);
    }

    public void init() {

    }

    @Override
    public void executeTask(SimTask task) {
        byte[] data = GenerateData(task.dataSize);
        client.send(data);
    }

    private byte[] GenerateData(int dataSize) {
        byte[] b = new byte[dataSize];
        new Random().nextBytes(b);
        return b;
    }


    public void receiveAll(){
        System.out.println("Starting server");
        // new Thread(server).start();
        server.receive();
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
