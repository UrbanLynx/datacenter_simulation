package Simulation.Master;

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

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Router {
    Map<Integer, ConnectionDesc> connections;   // this is an array for quick indexing of connected slave/ports
//    ArrayList<ConnectionDesc> connectionslist;
    SimulationConfig config;


    public Router(SimulationConfig simConfig){
        config = simConfig;
        //ArrayList<SlaveDesc> slaves = getSlaves();

        System.out.println("Connecting to each simulation slave");
        connectToSlaves();
    }

    private ArrayList<SlaveDesc> getSlaves() {
        /*ArrayList<SlaveDesc> slaves = new ArrayList<SlaveDesc>();
        for(String address: config.hosts){
            slaves.add(new SlaveDesc(address,config.slavePort));
        }
        return slaves;*/
        return null;
    }

    private void connectToSlaves() {
        connections = new HashMap<Integer, ConnectionDesc>();

        for(SlaveDesc slaveDesc: config.hosts){
            try {
                Socket socket = connectTo(slaveDesc.hostName, slaveDesc.portNumber);



                //Socket socket = new Socket(slaveDesc.hostName, slaveDesc.portNumber);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ConnectionDesc connectionDesc = new ConnectionDesc(slaveDesc, socket, oos, ois);
                connections.put(slaveDesc.index, connectionDesc);
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

    public Socket connectTo(String host, int port) throws IOException {
        Socket socket = null;
        boolean scanning=true;
        while(scanning)
        {
            try
            {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 0);
                scanning=false;
            }
            catch(ConnectException e) {
                System.out.println("Connect to "+host+" failed, waiting and trying again");
                try {
                    Thread.sleep(2000);//2 seconds
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        return socket;
    }

    public void sendTaskTo(int hostIndex, SimMessage simMessage) {
        System.out.print("Simulation event: ");
        /*System.out.println("Sender: host " + simMessage.task.srcAddress + ", Receiver: host " +
                simMessage.task.dstAddress + ", " + simMessage.task.size + " bytes");*/

        try {
            if (simMessage.task.simulationType == SimulationType.VARYS) {
                connections.get(hostIndex).oos.writeObject(simMessage);
            } else {
                // TODO : Add port for traditional
                connections.get(hostIndex).oos.writeObject(simMessage);
            }
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
