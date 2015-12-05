package Simulation.Slave;

import Simulation.Communicators.Utils;
import Simulation.Data.SimMessage;
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

            SimMessage message = null;
            do {
                System.out.println("Awaiting simulation instructions...\n");
                message = (SimMessage) ctrlOIS.readObject();
                if (message.eventType != SimMessage.SimEventType.TERMINATE){
                    message.outputToMaster = ctrlOOS;
                    startNewTask(message);
                }
            } while (message.eventType != SimMessage.SimEventType.TERMINATE);

            ctrlOIS.close();
            ctrlOOS.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctrlClientSocket.close();
        }
        Utils.safePrintln("Slave finished all tasks and exited.");
    }

    public void startNewTask(SimMessage message) throws Exception {
        Slave slave = new Slave(message);
        //slave.run();
        new Thread(slave).start();
    }
}
