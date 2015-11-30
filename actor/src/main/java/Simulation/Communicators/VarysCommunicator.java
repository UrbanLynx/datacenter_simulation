package Simulation.Communicators;

import Simulation.Data.DataGenerator;
import Simulation.Data.Reducer;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;
import Simulation.Logger.SimLogger;
import varys.framework.client.VarysClient;

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
public class VarysCommunicator {

    public final static Logger varysCommunicatorLogger = Logger.getLogger(VarysCommunicator.class.getName());
    public SimLogger loggers;

    public VarysCommunicator(SimTask task){
        //configureVarysLogger(task);
    }

    private void configureVarysLogger(SimTask task){
        try {
            loggers = new SimLogger("configs/hosts");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /*public String getDataId(String coflowId, int mapperId, int reducerId, int port){
        return "DATA_" + coflowId+ "_" + mapperId + "_" + reducerId + "_" + port;
    }*/

    public String getDataId(SimTask task, Reducer reducer){
        return "DATA_" + task.coflowId+ "_" + task.currentSlaveId + "_" + task.currentSlaveTaskIndex + "_" +
                reducer.reducerId + "_" + reducer.port;
    }

    public String getSenderId(SimTask task){
        return "MAPPER_" + task.coflowId+ "_" + task.currentSlaveId;
    }

    public String getReceiverId(SimTask task){
        return "REDUCER_" + task.coflowId+ "_" + task.currentSlaveId;
    }

    public void send(SimTask task) {
        //task.data.dataId = getDataId(task);
        DataGenerator generator = new DataGenerator();
        generator.generateUnitObject(1024);

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getSenderId(task), task.masterUrl, listener);
        client.start();

        int i = 0;
        for (Reducer reducer: task.reducersArr){
            //String dataId = getDataId(task.coflowId, task.currentSlaveId, reducer.reducerId, reducer.port);
            String dataId = getDataId(task,reducer);
            String object = generator.generateObject(reducer.sizeKB);
            client.putObject(dataId, object, task.coflowId, reducer.sizeBytes(), 5, null);
            Utils.safePrintln("Put fake data for " + reducer.reducerId);

            try {
                Socket socket = Utils.connectTo(reducer.address, reducer.port, 2000);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                Utils.safePrintln("[Mapper] Connected to " + reducer.address + reducer.port);
                long timeStamp = System.currentTimeMillis();
                outputStream.writeObject(dataId);
                Utils.logger.log(Level.INFO, String.valueOf(timeStamp)+getSendLogContent(task,reducer));
                //varysCommunicatorLogger.log(Level.INFO,getSendLogContent(task,reducer));
                //loggers.log(Level.INFO, reducer.address, String.valueOf(timeStamp) + getSendLogContent(task, reducer));
                //Utils.wait(5000);
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Utils.wait(5000);
        client.stop();
    }

    public void receive(SimTask task) {
        Utils.safePrintln("[Receiver]: Start receiving on URL: "+task.masterUrl);

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getReceiverId(task), task.masterUrl, listener);
        client.start();
        Utils.safePrintln("Accepting on port "+task.currentSlavePort + ". Number of mappers "+task.mappers.size());

        try {
            ServerSocket serverSocket = new ServerSocket(task.currentSlavePort);
            int receivedNumberTimes = 0;
            while (receivedNumberTimes != task.mappers.size()){
                Utils.safePrintln("Receive number " + receivedNumberTimes);
                Socket socket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String DATA_NAME = (String) inputStream.readObject();

                inputStream.close();
                socket.close();

                Utils.safePrintln("[Receiver]: Trying to retrieve " + DATA_NAME);
                String object = client.getObject(DATA_NAME, task.coflowId);
                long timeStamp = System.currentTimeMillis();
                Utils.logger.log(Level.INFO, String.valueOf(timeStamp)+getReceiveLogContent(task, task.reducers.get(task.currentSlaveId)));
                //varysCommunicatorLogger.log(Level.INFO,getReceiveLogContent(task));
                //loggers.log(Level.INFO, task.reducers.get(task.currentSlaveId).address, String.valueOf(timeStamp) + getSendLogContent(task, task.reducers.get(task.currentSlaveId)));
                Utils.safePrintln("[Receiver]: Got " + DATA_NAME + " of size "+object.length());

                receivedNumberTimes++;
            }

            Utils.safePrintln("[Reducer] Received from all mappers");
            client.stop();
            serverSocket.close();
        } catch (Exception e) {
            Utils.safePrintln("[Reducer] error: " + e.toString());
        }
    }
}
