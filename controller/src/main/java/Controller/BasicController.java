package Controller;

import scala.tools.cmd.gen.AnyVals;

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
        slaves.add(new SlaveDesc("localhost",1));
        slaves.add(new SlaveDesc("localhost",2));
        slaves.add(new SlaveDesc("localhost",3));
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
        System.out.print("\n");

    }

    // TODO: implement actual functionality, pass in File, or filename. For now hard coded.
    // takes a simulation file and resturns a list of SimEvents
    // return an error if a host not in its list of slaves is in simulation file
    private void parseSimFile() {
        //simEvents.add(new BasicSimEntry("10.0.0.1", "10.0.0.3", 10, 1000));      // send from host 10.0.0.1 to host 10.0.0.3 at t=10 ms, 1000 bytes
        simEvents.add(new BasicSimEntry(0, 1, 2000, 1000));         // send from host:port associated with connections[0] to connections[1] at t=2000 ms, 1000 bytes
    }


    public void executeSimulation() {
        System.out.println("Simulation is starting now\n");
        Iterator<BasicSimEntry> it = simEvents.iterator();
        while ( it.hasNext() ) {
            BasicSimEntry event = it.next();
            // wait until appropriate time, then
            // create messages and send to each host
            // TODO: sort simulation events by time
            // TODO: implement timer
            // TODO: ***(In Controller, not BasicController) register coflows with scheduler **when appropriate**

        }
    }

    public void executeTestTwoHosts() {
        // TODO: some more interesting tests with more than two hosts
        System.out.println("Test is starting now\n");
        int numBytes = 100;
        int portNumHost0 = 30;
        int portNumHost1 = 31;
        // send some traffic from host 0 to host 1
        executeSingleEvent(0, 1, portNumHost1, numBytes);
        // now send traffic in opposite direction
        executeSingleEvent(1, 0, portNumHost0, numBytes);
        // reverse direction again
        executeSingleEvent(0, 1, portNumHost1, numBytes);
    }

    public void executeTestThreeHosts() {
        // TODO: some more interesting tests with more than two hosts
        System.out.println("Test is starting now\n");
        int numBytes = 100;
        int portNumHost0 = 30;
        int portNumHost1 = 31;
        int portNumHost2 = 32;
        // send some traffic from host 0 to host 1
        executeSingleEvent(0, 1, portNumHost1, numBytes);
        // now send traffic in opposite direction
        executeSingleEvent(1, 0, portNumHost0, numBytes);
        // reverse direction again
        executeSingleEvent(0, 1, portNumHost1, numBytes);

        // send traffic from host 2 to host 1
        executeSingleEvent(2, 1, portNumHost1, numBytes);
    }

    public void terminate() {

        // instruct all slaves to terminate and close connection
        SimEventDesc instruction = new SimEventDesc(SimEventType.TERMINATE, 0, null, 0);
        for ( ConnectionDesc slaveConnection : connections ) {
            try {
                slaveConnection.oos.writeObject(instruction);
                slaveConnection.oos.close();
                slaveConnection.ois.close();
                slaveConnection.socket.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }

    }


    /**
     * send numBytes bytes from host represented by connections[senderIndex] to
     * portNumber on host represented by connections[receiverIndex]
     */
    private void executeSingleEvent(int senderIndex, int receiverIndex, int portNumber, int numBytes) {

        System.out.print("Simulation event: ");
        System.out.println("Sender: host " + senderIndex + ", Receiver: host " + receiverIndex +
                " on port " + portNumber + ", " + numBytes + " bytes");

        // a single simulation event has a pair of commands, send and receive
        SimEventDesc instruction;
        String receiveHostName = connections[receiverIndex].description.hostName;
        try {
            // these have to be done in this order, and controller needs confirmation that receiving host is ready
            // command receiver
            instruction = new SimEventDesc(SimEventType.RECEIVE, numBytes, receiveHostName, portNumber);
            System.out.println("created instruction: " + instruction);
            connections[receiverIndex].oos.writeObject(instruction);
            // get confirmation
            Object received = connections[receiverIndex].ois.readObject();
            if ( received.getClass() != Confirm.class ) {
                System.err.println("Did not get correct confirmation from slave");
            }
            // command sender
            instruction = new SimEventDesc(SimEventType.SEND, numBytes, receiveHostName, portNumber);
            System.out.println("created instruction: " + instruction);
            connections[senderIndex].oos.writeObject(instruction);
        } catch ( IOException e) {
            System.out.println("BasicController.executeSimulation() exception: " + e.getMessage());
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }

        System.out.print("\n");

    }

    public static void main(String[] args) {
        BasicController controller = new BasicController();
        controller.executeTestTwoHosts();
        controller.executeTestThreeHosts();
        controller.terminate();
    }

}
