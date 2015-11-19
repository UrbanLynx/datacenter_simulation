package Simulation.Slave;

import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;
import Simulation.Data.SimulationType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Router {
    int ctrlPortNumber;
    ServerSocket ctrlServerSocket;
    Socket ctrlClientSocket;
    ObjectOutputStream ctrlOOS;
    ObjectInputStream ctrlOIS;

    public Router(SimulationConfig config) {
        this.ctrlPortNumber = config.slavePort;
        try {
            ctrlServerSocket = new ServerSocket(this.ctrlPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForTasks() throws IOException {
        try {
            ctrlClientSocket = ctrlServerSocket.accept();
            ctrlOOS = new ObjectOutputStream(ctrlClientSocket.getOutputStream());
            ctrlOIS = new ObjectInputStream(ctrlClientSocket.getInputStream());

            SimTask task = null;
            do {
                System.out.println("Awaiting simulation instructions...\n");
                task = (SimTask) ctrlOIS.readObject();
                startNewTask(task);
            } while (task.continueSimulation);

            ctrlOIS.close();
            ctrlOOS.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctrlClientSocket.close();
        }
    }

    public void startNewTask(SimTask task) throws Exception {
        Slave slave = new Slave(task);
        new Thread(slave).start();
    }
}
