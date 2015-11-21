package Simulation.Communicators;

import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.ClientListener;
import varys.framework.client.VarysClient;

import java.util.concurrent.CountDownLatch;

/**
 * Created by stanislavmushits on 21/11/15.
 */
public class VarysRegistrator{

    private final long size;
    private VarysClient client;
    private String masterUrl;
    private String clientName;
    private int numOfSlaves;
    String coflowId;

    public VarysRegistrator(String url, String name, int numOfSlaves, long size){
        masterUrl = url;
        this.clientName = name;
        this.numOfSlaves = numOfSlaves;
        this.size = size;
    }

    public void run() {
        registerCoflow();
    }

    class TestListener implements ClientListener {
        public void connected(String id) {
            safePrintln("["+clientName+"]Log: Connected to master, got client ID " + id);
        }

        public void  disconnected() {
            safePrintln("["+clientName+"]Log: Disconnected from master");
            System.exit(0);
        }

        public void coflowRejected(String s, String s1) {

        }
    }


    public String registerCoflow() {
        final CountDownLatch latch = new CountDownLatch(1);

        Thread clientThread = new Thread(clientName){
            @Override
            public void run(){
                /*String url = "varys://stanislavs-air:1606";
                long LEN_BYTES = 1010101L;

                TestListener listener = new TestListener();
                VarysClient client = new VarysClient("[Sender]: SenderClientFake", url, listener);
                client.start();

                CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 3, LEN_BYTES, 10000);
                String coflowId = client.registerCoflow(desc);
                System.out.println("Registered coflow " + coflowId);

                client.awaitTermination();*/

                int deadlineMillis = 10000;

                TestListener listener = new TestListener();
                client = new VarysClient(clientName, masterUrl, listener);
                client.start();

                CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), numOfSlaves, size, deadlineMillis);
                coflowId = client.registerCoflow(desc);

                safePrintln("Registered coflow " + coflowId);
                latch.countDown();
                client.awaitTermination();
            }
        };

        clientThread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendToCoflow(masterUrl, coflowId, 1);
        return coflowId;

    }

    public void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    public void sendToCoflow(String varysMasterUrl, String coflowId, int senderID) {
        String DATA_NAME = "DATA"+senderID;
        long LEN_BYTES = 1010101L;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("Actor2"+senderID, varysMasterUrl, listener);
        client.start();

        client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        System.out.println("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        client.awaitTermination();
    }
}
