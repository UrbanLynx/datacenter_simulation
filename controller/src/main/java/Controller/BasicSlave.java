package Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ruby on 11/17/15.
 */
public class BasicSlave {

    int portNumber;
    ServerSocket serverSocket;
    Socket clientSocket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    BasicSlave(int portNumber) {
        this.portNumber = portNumber;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Exception 1 : " + e.getMessage());
        }
    }

    public void simulation() {

        // get connection from controller
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Exception 2 : " + e.getMessage());
        }

        if ( clientSocket != null ) {

            // open input/output streams
            try {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                System.out.println("Exception 3: " + e.getMessage());
            }
            // debug
            if ( oos != null && ois != null ) {
                System.out.println("opened output/input streams");
            }

            // read instructions until receive terminatation instruction
            boolean simulationRunning = true;
            Object received = null;
            SimEventDesc instruction = null;
            System.out.println("Awaiting simulation instructions");
            do {
                try {
                    received = ois.readObject();
                    // debug
                    //System.out.println("received an object of type: " + received.getClass());
                    /*
                    if ( received.getClass() == null || received.getClass() == SimEventDesc.class )
                        simulationRunning = true;
                    else
                        simulationRunning = false;
                        */
                    if ( received.getClass() == SimEventDesc.class ) {
                        instruction = (SimEventDesc)received;
                        System.out.println("Received " + instruction);
                        if ( instruction.event == SimEventType.TERMINATE) {
                            simulationRunning = false;
                        }
                    }
                /*
                switch ( instruction.event ) {
                    case SEND:
                        // create socket to destination host:port and send specified number of bytes
                        Socket socket = new Socket(dest, port);
                    case RECEIVE:
                        // listen for client on simulationServerSocket
                    default:
                }
                */
                } catch (IOException e) {
                    // TODO: is this the best thing to do?? is it supposed to be like this??
                    // do nothing
                    //System.out.println("Exception 4: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println("Exception 5: " + e.getMessage());
                }
            } while ( simulationRunning );

            // close streams
            try {
                ois.close();
                oos.close();
            } catch (IOException e) {
                System.out.println("Exception 6: " + e.getMessage());
            }
        }

        // close sockets
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Exception 7 : " + e.getMessage());
        }

    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Exception 8: " + e.getMessage());
        }

    }

    public static void main(String[] args) {

        /*
        int[] portNumbers = new int[args.length];
        for ( int i=0; i<portNumbers.length; ++i ) {
            portNumbers[i] = Integer.parseInt(args[i]);
        }
        */

        // for now, just have each slave liten on a single port
        if ( args.length == 0 ) {
            System.out.println("usage: BasicSlave <port number>");
            return;
        }

        int portNumber = Integer.parseInt(args[0]);

        BasicSlave slave = new BasicSlave(portNumber);
        slave.simulation();
        slave.stopServer();

    }
}
