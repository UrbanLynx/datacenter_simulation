package Simulation.Communicators;

import Simulation.Data.DataGenerator;
import Simulation.Data.Reducer;
import Simulation.Data.SimTask;
import Simulation.Logger.SimLogger;

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
    public SimLogger loggers;

    public TraditionalCommunicator() throws IOException {
        //configureTraditionalLogger();
    }

    private void configureTraditionalLogger() throws IOException {
        loggers = new SimLogger("configs/hosts");
    }

    public String getSendLogContent(SimTask task, Reducer reducer) {

        StringBuilder buf = new StringBuilder();
        buf.append(",SEND");
        buf.append(",CoflowID:"+task.coflowId);
        buf.append(",ReducerID:"+reducer.reducerId);
        buf.append(",ReducerSize:"+reducer.sizeKB);
        buf.append(",ReducerAddress:"+reducer.address);
        buf.append(",ReducerPort:"+reducer.port);
        buf.append("\n");

        return buf.toString();

    }

    public String getReceiveLogContent(SimTask task, Reducer reducer) {

        StringBuilder buf = new StringBuilder();
        buf.append(",RECIEVE");
        buf.append(",CoflowID:"+task.coflowId);
        buf.append(",ReducerID:"+reducer.reducerId);
        buf.append(",ReducerSize:"+reducer.sizeKB);
        buf.append(",ReducerAddress:"+reducer.address);
        buf.append(",ReducerPort:"+reducer.port);
        buf.append("\n");

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
                long timeStamp = System.currentTimeMillis();
                simOOS.writeObject(generator.generateObject(reducer.sizeKB));
                Utils.logger.log(Level.INFO, String.valueOf(timeStamp)+getSendLogContent(task,reducer));
                //traditionalCommunicatorLogger.log(Level.INFO, getSendLogContent(task, reducer));
                //loggers.log(Level.INFO, reducer.address, String.valueOf(timeStamp) + getSendLogContent(task, reducer));
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
                //traditionalCommunicatorLogger.log(Level.INFO, getReceiveLogContent(task));
                long timeStamp = System.currentTimeMillis();
                Utils.logger.log(Level.INFO, String.valueOf(timeStamp)+getReceiveLogContent(task,task.reducers.get(task.currentSlaveId)));
                //loggers.log(Level.INFO, task.reducers.get(task.currentSlaveId).address,
                //      String.valueOf(timeStamp) + getSendLogContent(task, task.reducers.get(task.currentSlaveId)));
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
