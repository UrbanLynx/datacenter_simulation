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
        // blah, refactor iteration (made connections an array instead of list for quick access)
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
                e.printStackTrace();
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
        //simEvents.add(new BasicSimEntry("10.0.0.1", "10.0.0.3", 10, 1000));      // send from host 10.0.0.1 to host 10.0.0.3 at t=10 ms, 1000 bytes
        simEvents.add(new BasicSimEntry(0, 1, 2000, 1000));         // send from host:port associated with connections[0] to connections[1] at t=2000 ms, 1000 bytes
    }


    public void executeSimulation() {
        System.out.println("Simulation is starting now");
        Iterator<BasicSimEntry> it = simEvents.iterator();
        while ( it.hasNext() ) {
            BasicSimEntry event = it.next();
            // wait until appropriate time, then
            // create messages and send to each host
            // TODO: sort simulation events by time
            // TODO: implement timer
            // TODO: ***(In Controller, not BasicController) register coflows with scheduler **when appropriate**

            // for now just send a message with instruction to execute simulation event
            //TODO: this is hard coded for now for testing:
            int numBytes = 100;
            // a single simulation event has a pair of commands
            try {
                /*
                // debug
                connections[0].oos.writeObject(new Integer(4));
                connections[1].oos.writeObject(new Integer(5));
                 */
                SimEventDesc instruction;
                // these have to be done in this order
                // receiver
                instruction = new SimEventDesc(SimEventType.RECEIVE, numBytes);
                System.out.println("created instruction: " + instruction);
                connections[0].oos.writeObject(instruction);
                // sender
                instruction = new SimEventDesc(SimEventType.SEND, numBytes);
                System.out.println("created instruction: " + instruction);
                connections[1].oos.writeObject(instruction);
            } catch ( IOException e) {
                System.out.println("BasicController.executeSimulation() exception: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        BasicController controller = new BasicController();
        controller.executeSimulation();
    }

}
