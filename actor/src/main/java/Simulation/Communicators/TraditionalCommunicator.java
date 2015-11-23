package Simulation.Communicators;

import Simulation.Data.Reducer;
import Simulation.Data.SimTask;

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

    public void send(SimTask task) {
        try {
            for (Reducer reducer: task.reducers.values()){
                Socket socket = Utils.connectTo(reducer.address, reducer.port, 2000);
                ObjectOutputStream simOOS = new ObjectOutputStream(socket.getOutputStream());

                Utils.safePrintln("Attempting to send " + reducer.size + " bytes to "+ reducer.address +":"+ reducer.port);
                simOOS.writeObject(Utils.getData(reducer.size));

                simOOS.close();
                socket.close();
            }

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(SimTask task) {
        try {
            ServerSocket serverSocket = new ServerSocket(task.reducers.get(task.currentSlaveId).port);
            Utils.safePrintln("Accepting on port "+task.reducers.get(task.currentSlaveId).port);

            int receivedNumberTimes = 0;
            while (receivedNumberTimes != task.reducers.size()){
                Socket socket = serverSocket.accept();

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String data = (String) inputStream.readObject();

                inputStream.close();
                socket.close();

                Utils.safePrintln("[Reducer]: Got " + data.length() + " bytes.");

                receivedNumberTimes++;
            }

            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
