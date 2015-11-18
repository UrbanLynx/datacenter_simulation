package Controller;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ruby on 11/17/15.
 */

/**
 * BasicController does not invoke Varys scheduler.
 * Just implement simulation Controller parsing a simulation file and
 * sending messages at specified times to slave processes
 * that will be running on Mininet hosts.
 */
public class BasicController {

    ArrayList<SlaveDesc> slaves;
    ConnectionDesc[] connections;   // this is an array for quick indexing of connected slave/ports
    ArrayList<BasicSimEntry> simEvents;

    // TODO: pass in slave file and simulation file, which is all hardcoded for now
    public BasicController() {

        // create list of slaves
        slaves = new ArrayList<SlaveDesc>();
        parseSlaveFile();

        // open control connection to each slave
        System.out.println("Connecting to each simulation slave");
        connectToSlaves();

        // create list of simulation events
        simEvents = new ArrayList<BasicSimEntry>();
        parseSimFile();

    }

    // TODO: implement actual functionality, pass in File, or filename. For now hard coded.
    // NOTE: in actual simulation setup, each host will listen for controller on a fixed port
    private void parseSlaveFile() {
        slaves.add(new SlaveDesc("localhost",7));
        slaves.add(new SlaveDesc("localhost",8));
    }

    private void connectToSlaves() {

        connections = new ConnectionDesc[slaves.size()];
        SlaveDesc slaveDesc;
        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        ConnectionDesc connectionDesc;
        Iterator<SlaveDesc> it = slaves.iterator();
        // blah, refactor iteration
        int i=0;
        while ( it.hasNext() ) {
            slaveDesc = it.next();
            try {
                socket = new Socket(slaveDesc.hostName, slaveDesc.portNumber);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                connectionDesc = new ConnectionDesc(slaveDesc, socket, oos, ois);
                connections[i++] = connectionDesc;
            } catch ( IOException e) {
                System.out.println(e.getMessage());
            }
        }

        // print connections made
        for ( int k=0; k<connections.length; ++k ) {
            System.out.println("Connection " + k + " : " + connections[k].description);
        }

    }

    // TODO: implement actual functionality, pass in File, or filename. For now hard coded.
    // takes a simulation file and resturns a list of SimEvents
    // return an error if a host not in its list of slaves is in simulation file
    private void parseSimFile() {
        simEvents.add(new BasicSimEntry("10.0.0.1", "10.0.0.3", 10, 1000));      // send from host 10.0.0.1 to host 10.0.0.3 at t=10 ms, 1000 bytes
        simEvents.add(new BasicSimEntry("10.0.0.3", "10.0.0.1", 2000, 1000));
    }


    public void executeSimulation() {
        Iterator<BasicSimEntry> it = simEvents.iterator();
        while ( it.hasNext() ) {
            BasicSimEntry event = it.next();
            // wait until appropriate time, then
            // create messages and send to each host
            // TODO: check that time is incrementing in simulation

        }
    }

    public static void main(String[] args) {
        BasicController controller = new BasicController();
        //controller.executeSimulation();
    }

}
