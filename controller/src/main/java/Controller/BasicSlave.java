package Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

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
            e.printStackTrace();
        }
    }

    public void simulation() {

        // get connection from controller
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( clientSocket != null ) {

            // open input/output streams
            try {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // debug
            if ( oos != null && ois != null ) {
                System.out.println("opened output/input streams");
            }

            // read instructions until receive terminatation instruction
            boolean continueSimulation = true;
            Object received = null;
            SimEventDesc instruction = null;
            System.out.println("Awaiting simulation instructions");
            do {
                try {
                    // read an object and check its type
                    received = ois.readObject();
                    // debug
                    if ( received.getClass() == Integer.class ) {
                        System.out.println("Received an Integer: " + received);
                        int portNum = 11;
                        int numBytes = 100;
                        String hostName = "localhost";
                        switch ( (Integer)received ) {
                            case 4:
                                receive(portNum, numBytes);
                            case 5:
                                send(hostName, portNum, numBytes);
                            default:
                                ;
                        }
                    }
                    if ( received.getClass() == SimEventDesc.class ) {
                        // we received a simulation instruction, check its type and execute
                        instruction = (SimEventDesc)received;
                        System.out.println("Received simulation instruction " + instruction);
                        // TODO: remove hard coding
                        int portNum = 11;
                        int numBytes = 100;
                        String hostName = "localhost";
                        switch ( instruction.event ) {
                            case SEND:
                                send(hostName, portNum, numBytes);
                            case RECEIVE:
                                receive(portNum, numBytes);
                            case TERMINATE:
                                continueSimulation = false;
                            default:
                        }
                    }
                } catch (IOException e) {
                    // TODO: is this the best thing to do?? is it supposed to be like this??
                    // do nothing
                    //System.out.println("Exception 4: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } while ( continueSimulation );

            // close streams
            try {
                ois.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // close sockets
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void send(String hostName, int portNum, int numBytes) {
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

        ServerSocket simServerSocket;
        Socket simClientSocket;
        byte[] buf = new byte[numBytes];
        int numBytesRead = 0;
        // attempt to receive data
        try {
            simServerSocket = new ServerSocket(portNumber);
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

    }


    public void stopServer() {
        try {
            serverSocket.close();
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

        // for now, just have each slave listen on a single port
        if ( args.length == 0 ) {
            System.out.println("usage: BasicSlave <port number>");
            return;
        }

        System.out.println("Simulation host process started");

        int portNumber = Integer.parseInt(args[0]);

        BasicSlave slave = new BasicSlave(portNumber);
        slave.simulation();
        slave.stopServer();

    }
}
