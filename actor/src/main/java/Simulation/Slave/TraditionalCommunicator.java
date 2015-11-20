package Simulation.Slave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class TraditionalCommunicator {

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

    public void receive(int portNumber, long numBytes) {

        System.out.println("Attempting to receive " + numBytes + " bytes");

        ServerSocket simServerSocket;
        Socket simClientSocket;
        StringBuffer buf = new StringBuffer();
        int numBytesRead = 0;
        // attempt to receive data
        try {
            simServerSocket = new ServerSocket(portNumber);
            // send confirmation to controller
            //ctrlOOS.writeObject(new Confirm());
            // listen for connection
            simClientSocket = simServerSocket.accept();
            if ( simClientSocket != null ) {
                ObjectInputStream simOIS = new ObjectInputStream(simClientSocket.getInputStream());
                //numBytesRead = simOIS.read(buf, 0, numBytes);
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



}
