package Simulation.Slave.Old;

import Simulation.Data.Confirm;
import Simulation.Data.Old.SimEventDesc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by ruby on 11/17/15.
 */
public class BasicSlave {

    int ctrlPortNumber;
    ServerSocket ctrlServerSocket;
    Socket ctrlClientSocket;
    ObjectOutputStream ctrlOOS;
    ObjectInputStream ctrlOIS;

    BasicSlave(int ctrlPortNumber) {
        this.ctrlPortNumber = ctrlPortNumber;
        try {
            ctrlServerSocket = new ServerSocket(this.ctrlPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simulation() {

        // get connection from controller
        try {
            ctrlClientSocket = ctrlServerSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( ctrlClientSocket != null ) {

            // open input/output streams
            try {
                ctrlOOS = new ObjectOutputStream(ctrlClientSocket.getOutputStream());
                ctrlOIS = new ObjectInputStream(ctrlClientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // debug
            if ( ctrlOOS != null && ctrlOIS != null ) {
                System.out.println("opened output/input streams");
            }

            // read instructions until receive terminatation instruction
            boolean continueSimulation = true;
            Object received = null;
            SimEventDesc instruction = null;
            System.out.println("Awaiting simulation instructions...\n");
            do {
                try {
                    // read an object and check its type
                    received = ctrlOIS.readObject();
                    if ( received.getClass() == SimEventDesc.class ) {
                        // we received a simulation instruction, check its type and execute
                        instruction = (SimEventDesc)received;
                        System.out.println("Received simulation instruction " + instruction);
                        switch ( instruction.event ) {
                            case SEND:
                                send(instruction.receiveHostName, instruction.portNumber, instruction.numBytes);
                                break;
                            case RECEIVE:
                                receive(instruction.portNumber, instruction.numBytes);
                                break;
                            case TERMINATE:
                                continueSimulation = false;
                                break;
                            default:
                                System.err.println("invalid instruction!!");
                                break;
                        }
                    }
                } catch (IOException e) {
                    // TODO: is this the best thing to do?? is it supposed to be like this??
                    // do nothing
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while ( continueSimulation );

            // close streams
            try {
                ctrlOIS.close();
                ctrlOOS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // close sockets
        try {
            ctrlClientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void send(String hostName, int portNum, int numBytes) {

        System.out.println("Attempting to send " + numBytes + " bytes\n");

        // create some fake data according to specified number of bytes
        byte[] data = new byte[numBytes];
        new Random().nextBytes(data);
        //  send it!
        try {
            Socket socket = new Socket(hostName, portNum);
            ObjectOutputStream simOOS = new ObjectOutputStream(socket.getOutputStream());
            simOOS.write(data);
            simOOS.close();
            socket.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(int portNumber, int numBytes) {

        System.out.println("Attempting to receive " + numBytes + " bytes");

        ServerSocket simServerSocket;
        Socket simClientSocket;
        byte[] buf = new byte[numBytes];
        int numBytesRead = 0;
        // attempt to receive data
        try {
            simServerSocket = new ServerSocket(portNumber);
            // send confirmation to controller
            ctrlOOS.writeObject(new Confirm());
            // listen for connection
            simClientSocket = simServerSocket.accept();
            if ( simClientSocket != null ) {
                ObjectInputStream simOIS = new ObjectInputStream(simClientSocket.getInputStream());
                numBytesRead = simOIS.read(buf, 0, numBytes);
                simOIS.close();
            }
            simServerSocket.close();
            simClientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // print how many bytes received
        if ( numBytesRead == numBytes ) {
            System.out.println("Read " + numBytesRead + " bytes, as expected");
        } else {
            System.out.println("Read " + numBytesRead + " bytes, was expecting " + numBytes + "bytes");
        }
        System.out.print("\n");

    }


    public void stopServer() {
        System.out.println("stopping server for control");
        try {
            ctrlServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        /*
        int[] portNumbers = new int[args.length];
        for ( int i=0; i<portNumbers.length; ++i ) {
            portNumbers[i] = Integer.parseInt(args[i]);
        }
        */

        // for now, just have each slave listen for controller on a single port
        if ( args.length == 0 ) {
            System.out.println("usage: BasicSlave <port number that controller connects to>");
            return;
        }

        System.out.println("Simulation host process started");

        int portNumber = Integer.parseInt(args[0]);

        BasicSlave slave = new BasicSlave(portNumber);
        slave.simulation();
        slave.stopServer();

    }
}
