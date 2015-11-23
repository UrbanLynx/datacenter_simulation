package Simulation.Communicators;

import Simulation.Data.Reducer;
import Simulation.Data.SimTask;
import varys.framework.client.VarysClient;

import java.util.concurrent.CountDownLatch;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class VarysCommunicator {

    public void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
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

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getSenderId(task), task.masterUrl, listener);
        client.start();

        for (Reducer reducer: task.reducers){
            client.putFake(getDataId(task.coflowId, task.currentSlaveId, reducer.reducerId), task.coflowId, reducer.size, 1);
            safePrintln("Put fake data for " + reducer.reducerId);
        }

        client.awaitTermination();
    }

    public void receive(SimTask task) {
        safePrintln("[Receiver]: Start receiving on URL: "+task.masterUrl);



        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getReceiverId(task), task.masterUrl, listener);
        client.start();

        try {
            //Thread.sleep(5000);
            for (int mapper: task.mappers){
                String DATA_NAME = getDataId(task.coflowId, mapper, task.currentSlaveId);
                safePrintln("[Receiver]: Trying to retrieve " + DATA_NAME);
                client.getFake(DATA_NAME, task.coflowId);
                safePrintln("[Receiver]: Got " + DATA_NAME + " Now waiting to die.");
            }


            client.awaitTermination();
        } catch (Exception e) {
            safePrintln(e.toString());
        }
    }
}
