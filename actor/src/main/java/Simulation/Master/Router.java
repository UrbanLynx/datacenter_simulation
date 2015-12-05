package Simulation.Master;

import Simulation.Communicators.Utils;
import Simulation.Data.Confirm;
import Simulation.Data.SimMessage;
import Simulation.Data.SimulationConfig;
import Simulation.Data.SimulationType;
import Simulation.Master.Utils.ConnectionDesc;
import Simulation.Master.Utils.SlaveDesc;
import com.sun.mail.iap.ConnectionException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Router {
    Map<Integer, ConnectionDesc> connections;
    SimulationConfig config;

    public Router(SimulationConfig simConfig) throws IOException {
        config = simConfig;

        System.out.println("Connecting to each simulation slave");
        connectToSlaves();

        System.out.println("Configuring logger for Master");
    }

    private void connectToSlaves() {
        connections = new HashMap<Integer, ConnectionDesc>();

        for(SlaveDesc slaveDesc: config.hosts.values()){
            try {
                Socket socket = Utils.connectTo(slaveDesc.hostName, slaveDesc.portNumber, 1000);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ConnectionDesc connectionDesc = new ConnectionDesc(slaveDesc, socket, oos, ois);
                connections.put(slaveDesc.index, connectionDesc);
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }

        printConnections();
    }

    public void closeConnections(){
        for (ConnectionDesc slave: connections.values()){
            try {
                slave.oos.writeObject(new SimMessage(SimMessage.SimEventType.TERMINATE));
                slave.oos.close();
                slave.socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printConnections() {
        // print connections made
        for (Map.Entry<Integer, ConnectionDesc> conn: connections.entrySet()) {
            System.out.println("Connection " + conn.getValue().description);
        }
    }

    public String getLogContent(SimMessage simMessage) {

        StringBuilder buf = new StringBuilder();
        buf.append("sendTaskTo:Master:");
        buf.append(":SystemTime:"+System.currentTimeMillis());
        buf.append(":EventType");
        if (simMessage.eventType == SimMessage.SimEventType.RECEIVE)
            buf.append(":RECEIVE");
        else if (simMessage.eventType == SimMessage.SimEventType.SEND)
            buf.append(":SEND");
        else
            buf.append(":TERMINATE");

        buf.append(":CoflowID"+simMessage.task.coflowId);

        return buf.toString();

    }

    public void sendTaskTo(int hostIndex, SimMessage simMessage) {
        System.out.println("Sending task to host " + hostIndex);

        try {
            connections.get(hostIndex).oos.writeObject(simMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
