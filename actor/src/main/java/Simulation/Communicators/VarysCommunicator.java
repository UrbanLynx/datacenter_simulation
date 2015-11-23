package Simulation.Communicators;

import Simulation.Data.Reducer;
import Simulation.Data.SimMessage;
import Simulation.Data.SimTask;
import varys.framework.client.VarysClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class VarysCommunicator {

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

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getSenderId(task), task.masterUrl, listener);
        client.start();

        for (Reducer reducer: task.reducers.values()){
            String dataId = getDataId(task.coflowId, task.currentSlaveId, reducer.reducerId);
            client.putFake(dataId, task.coflowId, reducer.size, 1);
            Utils.safePrintln("Put fake data for " + reducer.reducerId);

            try {
                Socket socket = Utils.connectTo(reducer.address, reducer.port, 2000);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(dataId);

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
                client.getFake(DATA_NAME, task.coflowId);
                Utils.safePrintln("[Receiver]: Got " + DATA_NAME + " Now waiting to die.");

                receivedNumberTimes++;
            }

            client.stop();
            serverSocket.close();
        } catch (Exception e) {
            Utils.safePrintln(e.toString());
        }
    }
}
