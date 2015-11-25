package Simulation.Communicators;

import Simulation.Data.DataGenerator;
import Simulation.Data.Reducer;
import Simulation.Data.SimTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class TraditionalCommunicator {

    public final static Logger traditionalCommunicatorLogger = Logger.getLogger(TraditionalCommunicator.class.getName());

    public TraditionalCommunicator() throws IOException {
        configureTraditionalLogger();
    }

    private void configureTraditionalLogger() throws IOException {
        traditionalCommunicatorLogger.addHandler(new FileHandler());
        traditionalCommunicatorLogger.setLevel(Level.ALL);
    }

    public String getSendLogContent(SimTask task, Reducer reducer) {

        StringBuilder buf = new StringBuilder();
        buf.append("send,");
        buf.append("SystemTime:"+System.currentTimeMillis());
        buf.append(",CoflowID:"+task.coflowId);
        buf.append(",ReducerID:"+reducer.reducerId);
        buf.append(",ReducerSize:"+reducer.sizeKB);
        buf.append(",ReducerAddress:"+reducer.address);
        buf.append(",ReducerPort:"+reducer.port);

        return buf.toString();

    }

    public String getReceiveLogContent(SimTask task) {

        StringBuilder buf = new StringBuilder();
        buf.append("recieve,");
        buf.append("SystemTime:"+System.currentTimeMillis());
        buf.append(",CoflowID:"+task.coflowId);

        return buf.toString();

    }

    public void send(SimTask task) {
        try {
            DataGenerator generator = new DataGenerator();
            generator.generateUnitObject(1024);

            for (Reducer reducer: task.reducers.values()){
                Socket socket = Utils.connectTo(reducer.address, reducer.port, 2000);
                ObjectOutputStream simOOS = new ObjectOutputStream(socket.getOutputStream());

                Utils.safePrintln("Attempting to send " + reducer.sizeBytes() + " bytes to "+ reducer.address +":"+ reducer.port);
                simOOS.writeObject(generator.generateObject(reducer.sizeKB));
                traditionalCommunicatorLogger.log(Level.INFO, getSendLogContent(task, reducer));
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
                traditionalCommunicatorLogger.log(Level.INFO, getReceiveLogContent(task));
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
