package Simulation.Slave;

import Simulation.Data.SimTask;
import junit.framework.TestListener;
import varys.VarysException;
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
            safePrintln("[Receiver]: Log: Connected to master, got client ID " + id);
        }

        public void  disconnected() {
            safePrintln("[Receiver]: Log: Disconnected from master");
            System.exit(0);
        }

        public void coflowRejected(String s, String s1) {

        }
    }

    public String getDataId(SimTask task){
        return task.dstAddress + ":" + task.dstPort;
    }

    public void receivePrev(SimTask task) {

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
        //Simulation.Communicators.VarysSender client = new Simulation.Communicators.VarysSender(task.masterUrl, task.coflowId);
        //client.registerCoflow();

        //Simulation.Communicators.VarysSender client2 = new Simulation.Communicators.VarysSender(task.masterUrl, task.coflowId);
        //client2.coflowId = client.coflowId;
        //client2.coflowId = task.coflowId;
        //client2.run();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void receive(SimTask task) {
        Simulation.Communicators.VarysReceiver client = new Simulation.Communicators.VarysReceiver(task.masterUrl);
        client.run();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendPrev(SimTask task) {
        sendFake(task);

        /*String dataId = getDataId(task);

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("[Sender]: SenderClientFake", task.masterUrl, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 10, task.size, 200000);
        task.coflowId = client.registerCoflow(desc);


        // TODO: "DATA" -> dataId
        client.putFake("DATA", task.coflowId, task.size, 1);

        System.out.println("[Sender]: Put a fake piece of data of " + task.size + " bytes. Now waiting to die.");*/

        // client.unregisterCoflow(coflowId)

        //client.awaitTermination();
    }


    public String coflowId;
    public String url;

    public void sendFake(SimTask task) {
        coflowId = task.coflowId;
        url = task.masterUrl;

        System.out.println("[Sender]: Sending data with url " + url);
        String DATA_NAME = "DATA";
        long LEN_BYTES = 1010101L;


        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(),10, LEN_BYTES, 70000);
        coflowId = client.registerCoflow(desc);

        int SLEEP_MS1 = 5000;
        System.out.println("[Sender]: Registered coflow " + coflowId);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread t = new Thread(new Runnable() {
            public void run()
            {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long LEN_BYTES = 1010101L;
                TestListener masterListener = new TestListener();
                VarysClient master = new VarysClient("MasterClientFake", url, masterListener);

                master.start();

                master.putFake("DATA1", coflowId, LEN_BYTES, 1);

                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                master.awaitTermination();
                int a  =1;
                a += 1;
            }
        });
        t.start();

        //client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        System.out.println("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t2 = new Thread(new Runnable() {
            public void run()
            {
                TestListener receiverListener = new TestListener();
                VarysClient rec = new VarysClient("ReceiverClientFake", url, receiverListener);
                rec.start();

                try {
                    rec.getFake("DATA1", coflowId);
                    safePrintln("Get DATA1 on t2");
                } catch (VarysException e) {
                    e.printStackTrace();
                }

                rec.awaitTermination();
            }
        });
        t2.start();

        // client.unregisterCoflow(coflowId)

        /*try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        client.awaitTermination();
    }

}
