package Simulation.Master;

import Simulation.Data.Confirm;
import Simulation.Data.SimMessage;
import Simulation.Data.SimulationConfig;
import Simulation.Master.Utils.ConnectionDesc;
import Simulation.Master.Utils.SlaveDesc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Router {
    Map<Integer, ConnectionDesc> connections;   // this is an array for quick indexing of connected slave/ports
//    ArrayList<ConnectionDesc> connectionslist;
    SimulationConfig config;


    public Router(SimulationConfig simConfig){
        config = simConfig;
        ArrayList<SlaveDesc> slaves = getSlaves();

        System.out.println("Connecting to each simulation slave");
        connectToSlaves(slaves);
    }

    private ArrayList<SlaveDesc> getSlaves() {
        ArrayList<SlaveDesc> slaves = new ArrayList<SlaveDesc>();
        for(String address: config.hosts){
            slaves.add(new SlaveDesc(address,config.slavePort));
        }

        return slaves;
    }

    private void connectToSlaves(ArrayList<SlaveDesc> slaves) {
        connections = new HashMap<Integer, ConnectionDesc>();

        for(SlaveDesc slaveDesc: slaves){
            try {
                Socket socket = new Socket(slaveDesc.hostName, slaveDesc.portNumber);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ConnectionDesc connectionDesc = new ConnectionDesc(slaveDesc, socket, oos, ois);
                connections.put(config.hostIndex.get(slaveDesc.hostName), connectionDesc);
//                connectionslist.add(connectionDesc);
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }

        // print connections made
        for (Map.Entry<Integer, ConnectionDesc> conn: connections.entrySet()) {
            System.out.println("Connection " + conn.getValue().description);
        }
    }

    public void sendTaskTo(String address, SimMessage simMessage) {
        System.out.print("Simulation event: ");
        System.out.println("Sender: host " + simMessage.task.srcAddress + ", Receiver: host " +
                simMessage.task.dstAddress + ", " + simMessage.task.size + " bytes");

        try {
            connections.get(address).oos.writeObject(simMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitForAck(String address) throws IOException, ClassNotFoundException {
        Object received = connections.get(address).ois.readObject();
        if ( received.getClass() != Confirm.class ) {
            System.err.println("Did not get correct confirmation from slave");
        }
    }
}
