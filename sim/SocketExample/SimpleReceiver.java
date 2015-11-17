package SocketExample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ruby on 11/17/15.
 */
public class SimpleReceiver {

    public static void main(String[] args) {

        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            int x = in.readInt();
            System.out.println("read integer " + x);
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.out.println("Exception caught when listening on port " + portNumber +
                    " or listening for a connection");
            System.out.println(e.getMessage());

        }


    }
}
