package Simulation.Slave;

import Simulation.Data.SimTask;
import junit.framework.TestListener;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.ClientListener;
import varys.framework.client.VarysClient;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class VarysCommunicator {

    //SimTask task;

    //VarysCommunicator(SimTask task) {
    //    this.task = task;
    //}

    public void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    class TestListener implements ClientListener {
        public void connected(String id) {
            System.out.println("[Receiver]: Log: Connected to master, got client ID " + id);
        }

        public void  disconnected() {
            System.out.println("[Receiver]: Log: Disconnected from master");
            System.exit(0);
        }

        public void coflowRejected(String s, String s1) {

        }
    }

    public String getDataId(SimTask task){
        return task.dstAddress + ":" + task.dstPort;
    }

    public void receive(SimTask task) {

        safePrintln("[Receiver]: Start receiving on URL: "+ task.masterUrl);

        //coflowId = "COFLOW-000000";

        /*TestListener listener = new TestListener();
        VarysClient client = new VarysClient("ReceiverClientObject", url, (ClientListener) listener);
        client.start();

        try {
            Thread.sleep(5000);
            String OBJ_NAME = "OBJ";
            safePrintln("Trying to retrieve " + OBJ_NAME);
            byte[] respArr = new byte[0];
            respArr = client.getObject(OBJ_NAME, coflowId);
            safePrintln("Got " + OBJ_NAME + " with " + respArr.length + " elements. Now waiting to die.");
            client.awaitTermination();
        } catch (Exception e) {
            safePrintln(e.toString());
        }*/

        String dataId = getDataId(task);

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("ReceiverClientFake", task.masterUrl, listener);
        client.start();

        try {
            //Thread.sleep(5000);
            safePrintln("[Receiver]: Trying to retrieve " + dataId);

            // TODO: "DATA" -> dataId
            client.getFake("DATA", task.coflowId);

            safePrintln("[Receiver]: Got " + dataId + " Now waiting to die.");
            //client.awaitTermination();
        } catch (Exception e) {
            safePrintln(e.toString());
        }
    }

    public void send(SimTask task) {
        System.out.println("[Sender]: Sending data with coflow " + task.coflowId);
        //coflowId = "COFLOW-000000";

        /*String OBJ_NAME = "OBJ"; // to the args
        int NUM_ELEMS = data.length;
        byte[] toSend = data;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientObject", url, listener);
        client.start();

        // Last argument deadline, ms - what to write?
        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, NUM_ELEMS * 4,1000);
        coflowId = client.registerCoflow(desc);

        int SLEEP_MS1 = 5000;
        System.out.println("Registered coflow " + coflowId + ". Now sleeping for " + SLEEP_MS1 + " milliseconds.");
        try {
            Thread.sleep(SLEEP_MS1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Didnt have last arg, so i inserted null
        client.putObject(OBJ_NAME, toSend, String.valueOf(coflowId), NUM_ELEMS * 4, 1,null);
        System.out.println("Put an Array[Int] of " + NUM_ELEMS + " elements. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        client.awaitTermination();*/

        String dataId = getDataId(task);

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("[Sender]: SenderClientFake", task.masterUrl, listener);
        client.start();

        // TODO: "DATA" -> dataId
        client.putFake("DATA", task.coflowId, task.size, 1);

        System.out.println("[Sender]: Put a fake piece of data of " + task.size + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        //client.awaitTermination();
    }

}
