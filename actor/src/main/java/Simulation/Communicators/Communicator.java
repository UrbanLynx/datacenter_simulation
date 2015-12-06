package Simulation.Communicators;

import Simulation.Data.*;
import Simulation.Logger.SimLogger;
import Simulation.Logger.LogUtils;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.VarysClient;
import varys.framework.client.VarysOutputStream;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Communicator {

    public String getSendLogContent(SimTask task, Reducer reducer, String message) throws UnknownHostException {

        StringBuilder buf = new StringBuilder();
        buf.append("SEND");
        buf.append(",TaskID:"+task.id);
        buf.append(",MapperAddr:"+InetAddress.getLocalHost().toString());
        buf.append(",CoflowID:"+task.coflowId);
        buf.append(",ReducerID:"+reducer.reducerId);
        buf.append(",ReducerSize:"+reducer.sizeKB);
        buf.append(",ReducerAddress:"+reducer.address);
        buf.append(",ReducerPort:"+reducer.port);
        buf.append(",Action:"+message);
        buf.append("\n");

        return buf.toString();

    }

    public String getReceiveLogContent(SimTask task, Reducer reducer, String message) throws UnknownHostException {

        StringBuilder buf = new StringBuilder();
        buf.append("RECIEVE");
        buf.append(",TaskID:"+task.id);
        buf.append(",ReducerAddr:"+InetAddress.getLocalHost().toString());
        buf.append(",CoflowID:"+task.coflowId);
        buf.append(",ReducerID:"+reducer.reducerId);
        buf.append(",ReducerSize:"+reducer.sizeKB);
        buf.append(",ReducerAddress:"+reducer.address);
        buf.append(",ReducerPort:"+reducer.port);
        buf.append(",Action:"+message);
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

    public void send(final SimTask task) {
        try {
            //Utils.logger.log(Level.INFO, String.format("%1$s, task %2$d, slave %3$d started with coflow %4$d",
            //        "MAPPER", task.id, task.currentSlaveId, InetAddress.getLocalHost().toString(), task.coflowId));



            //int coflowId = 0;
            //VarysListener listener;
            //VarysClient client;
            //if (task.simulationType == SimulationType.VARYS){
                //listener = new VarysListener();
                //client = new VarysClient(getSenderId(task), task.masterUrl, listener);
                //client.start();
                //CoflowDescription desc = new CoflowDescription("DEFAULT"+task.id, CoflowType.DEFAULT(), -1, -1);
                //int coflowId = client.registerCoflow(desc);
            //    coflowId = Integer.parseInt(task.coflowId);
            //}


            //Utils.safePrintln("Master client id " + client.masterClientId());

            //DataGenerator generator = new DataGenerator();
            //generator.generateUnitObject(1024);

            final CountDownLatch latch = new CountDownLatch(task.reducersArr.size());
            for (final Reducer newReducer: task.reducersArr){
                //RP removed
                Utils.logger.log(SimLogger.LogLevel.INFO, getSendLogContent(task,newReducer, "trying to connect"));

                Thread t = new Thread(){
                    //public Socket socket;
                    @Override
                    public void run(){
                        try{
                            Reducer reducer = newReducer;
                            Socket socket = Utils.connectTo(reducer.address, reducer.port, 1);

                            InputStream inputStream = socket.getInputStream();
                            //RP removed
                            Utils.logger.log(SimLogger.LogLevel.INFO, getSendLogContent(task,reducer, "connected"));
                            long timeStamp = System.currentTimeMillis();
                            Utils.logger.log(SimLogger.LogLevel.ANALYS, LogUtils.getSlaveLogContent(task, LogUtils.Event.SEND, Long.toString(timeStamp)));

                            OutputStream output = null;
                            if (task.simulationType == SimulationType.TRADITIONAL){
                                output = socket.getOutputStream();
                            } else{
                                if (doUseCoflow(reducer, task)){
                                    Utils.safePrintln(getSendLogContent(task,reducer, "use coflow"));
                                    output = new VarysOutputStream(socket, Integer.parseInt(task.coflowId));

                                } else{
                                    Utils.safePrintln(getSendLogContent(task,reducer, "no coflow"));
                                    output = socket.getOutputStream();
                                }
                            }


                            //RP removed
                            //Utils.logger.log(Level.INFO, getSendLogContent(task,reducer, "attempt to send"));

                            //long timeStamp = System.currentTimeMillis();
                            byte[] buf = new byte[1024];
                            for (int i=0; i < reducer.sizeKB; i++){
                                output.write(buf);
                                output.flush();
                            }


                            //RP removed
                            //Utils.logger.log(Level.INFO, String.valueOf(timeStamp)+getSendLogContent(task,reducer, "finished"));

                            // log send time for results analysis


                            output.close();
                            socket.close();

                            Utils.safePrintln("[Mapper" + task.currentSlaveTaskIndex + "] finished with reducer " +
                                    reducer.address + ":" + reducer.port);
                            latch.countDown();
                        }
                        catch(Exception e){
                            Utils.safePrintln(e.getMessage());
                            e.printStackTrace();
                        }

                    }
                };
                t.start();

            }
            latch.await();
        } catch (Exception e) {
            Utils.safePrintln(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean doUseCoflow(Reducer reducer, SimTask task) {
        return !reducer.doNotRegisterFlow.contains(task.currentSlaveTaskIndex);
    }

    public class ReceiveThread implements Runnable {

        Socket socket;
        CountDownLatch latch;

        public ReceiveThread(Socket socket, CountDownLatch latch) {
            // store parameter for later user
        }

        public void run() {
        }
    }

    public void receive(final SimTask task) {
        try {
            ServerSocket serverSocket = new ServerSocket(task.currentSlavePort);
            final CountDownLatch latch = new CountDownLatch(task.mappers.size());
            //Utils.safePrintln("Accepting on port "+task.currentSlavePort);

            int receivedNumberTimes = 0;
            while (receivedNumberTimes != task.mappers.size()){

                //Utils.logger.log(Level.INFO, getReceiveLogContent(task,task.reducers.get(task.currentSlaveId), "accepting"));

                final Socket newSocket = serverSocket.accept();
                Thread t = new Thread(){
                    //public Socket socket;
                    @Override
                    public void run(){
                        try {
                            Socket socket = newSocket;
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.flush();
                            //ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                            InputStream inputStream = socket.getInputStream();

                            //Utils.logger.log(Level.INFO, getReceiveLogContent(task,task.reducers.get(task.currentSlaveId), "connected"));

                            //byte[] data = new byte[(int)task.reducers.get(task.currentSlaveId).sizeBytes()];
                            byte[] data = new byte[1024];
                            int n = 0;
                            for(int i=0; i< (int)task.reducers.get(task.currentSlaveId).sizeKB; i++){
                                n += inputStream.read(data);
                            }

                            long timeStamp = System.currentTimeMillis();
                            Utils.logger.log(SimLogger.LogLevel.ANALYS, LogUtils.getSlaveLogContent(task, LogUtils.Event.RECEIVE, Long.toString(timeStamp) ));


                            //Utils.logger.log(Level.INFO, String.valueOf(timeStamp)+getReceiveLogContent(task,task.reducers.get(task.currentSlaveId), "data received"));

                            inputStream.close();
                            socket.close();

                            Utils.safePrintln("[Reducer" + task.currentSlaveTaskIndex + "]: Got " + n + " bytes.");
                            latch.countDown();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
                receivedNumberTimes++;
            }

            latch.await();
            serverSocket.close();

            Utils.reportFinishToMaster(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public void receive(SimTask task) {
        try {

            Utils.safePrintln("Accepting on port "+task.currentSlavePort);

            VarysListener listener = new VarysListener();
            VarysClient client = new VarysClient(getReceiverId(task), task.masterUrl, listener);
            client.start();

            ServerSocket serverSocket = new ServerSocket(task.currentSlavePort);

            int receivedNumberTimes = 0;
            while (receivedNumberTimes != task.mappers.size()){
                Socket socket = serverSocket.accept();

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                VarysOutputStream outputStream = new VarysOutputStream(socket, Integer.getInteger(task.coflowId));

                //String data = (String) inputStream.readObject();
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
    }*/

/*    public void send(SimTask task) {
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
    }*/
}
