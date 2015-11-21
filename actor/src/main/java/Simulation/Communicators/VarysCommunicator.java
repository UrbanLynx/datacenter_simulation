package Simulation.Communicators;

import Simulation.Data.SimTask;
import Simulation.Communicators.VarysListener;
import junit.framework.TestListener;
import varys.VarysException;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.VarysClient;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class VarysCommunicator {

    public void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    public String getDataId(SimTask task){
        return "DATA_" + task.dstAddress + "_" + task.dstPort;
    }

    public String getSenderId(SimTask task){
        return "SENDER_" + task.srcAddress + "_" + task.srcPort;
    }

    public String getReceiverId(SimTask task){
        return "RECEIVER_" + task.dstAddress + "_" + task.dstPort;
    }

    public void send(SimTask task) {
        task.data.dataId = getDataId(task);

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getSenderId(task), task.masterUrl, listener);
        client.start();

        client.putFake(getDataId(task), task.coflowId, task.size, 1);
        safePrintln("[Sender]: Put a fake piece of data of " + task.size + " bytes. Now waiting to die.");

        client.awaitTermination();
    }

    public void receive(SimTask task) {
        safePrintln("[Receiver]: Start receiving on URL: "+task.masterUrl);

        String DATA_NAME = getDataId(task);

        VarysListener listener = new VarysListener();
        VarysClient client = new VarysClient(getReceiverId(task), task.masterUrl, listener);
        client.start();

        try {
            //Thread.sleep(5000);
            safePrintln("[Receiver]: Trying to retrieve " + DATA_NAME);
            client.getFake(DATA_NAME, task.coflowId);
            safePrintln("[Receiver]: Got " + DATA_NAME + " Now waiting to die.");
            client.awaitTermination();
        } catch (Exception e) {
            safePrintln(e.toString());
        }
    }
}
