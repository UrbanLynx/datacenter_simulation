package Simulation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class TraditionalClient {

    public void sendTo(String host, int port, String data){
        try{
            Socket clientSocket = new Socket(host, port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(data);
            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
