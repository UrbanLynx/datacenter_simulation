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
        configureVarysLogger(task);
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
        buf.append("send,");
        //buf.append("SystemTime:"+System.currentTimeMillis());
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
        //buf.append("SystemTime:"+System.currentTimeMillis());
        buf.append(",CoflowID:"+task.coflowId);

        return buf.toString();

    }

    public String getDataId(String coflowId, int mapperId, int reducerId){
        return "DATA_" + coflowId+ "_" + mapperId + "_" + reducerId;
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

        for (Reducer reducer: task.reducers.values()){
            String dataId = getDataId(task.coflowId, task.currentSlaveId, reducer.reducerId);
            String object = generator.generateObject(reducer.sizeKB);
            client.putObject(dataId, object, task.coflowId, reducer.sizeBytes(), 1, null);
            Utils.safePrintln("Put fake data for " + reducer.reducerId);

            try {
                Socket socket = Utils.connectTo(reducer.address, reducer.port, 2000);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                long timeStamp = System.currentTimeMillis();
                outputStream.writeObject(dataId);
                //varysCommunicatorLogger.log(Level.INFO,getSendLogContent(task,reducer));
                //loggers.log(Level.INFO, reducer.address, String.valueOf(timeStamp) + getSendLogContent(task, reducer));
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        client.stop();
    }

    public void receive(SimTask task) {
        Utils.safePrintln("[Receiver]: Start receiving on URL: "+task.masterUrl);

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getReceiverId(task), task.masterUrl, listener);
        client.start();

        try {
            ServerSocket serverSocket = new ServerSocket(task.reducers.get(task.currentSlaveId).port);
            int receivedNumberTimes = 0;
            while (receivedNumberTimes != task.reducers.size()){
                Utils.safePrintln("Accepting on port"+task.reducers.get(task.currentSlaveId).port);
                Socket socket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String DATA_NAME = (String) inputStream.readObject();
                inputStream.close();
                socket.close();

                Utils.safePrintln("[Receiver]: Trying to retrieve " + DATA_NAME);
                String object = client.getObject(DATA_NAME, task.coflowId);
                long timeStamp = System.currentTimeMillis();
                //varysCommunicatorLogger.log(Level.INFO,getReceiveLogContent(task));
                //loggers.log(Level.INFO, task.reducers.get(task.currentSlaveId).address, String.valueOf(timeStamp) + getSendLogContent(task, task.reducers.get(task.currentSlaveId)));
                Utils.safePrintln("[Receiver]: Got " + DATA_NAME + " of size "+object.length());

                receivedNumberTimes++;
            }

            client.stop();
            serverSocket.close();
        } catch (Exception e) {
            Utils.safePrintln(e.toString());
        }
    }
}
