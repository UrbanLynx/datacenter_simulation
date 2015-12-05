package Simulation.Communicators;

import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
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
    private int taskId;

    public VarysRegistrator(String url, String name, int numOfSlaves, long size, int taskId){
        masterUrl = url;
        this.clientName = name;
        this.numOfSlaves = numOfSlaves;
        this.size = size;
        this.taskId = taskId;
    }

    public String registerCoflow() {
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread clientThread = new Thread(clientName){
            @Override
            public void run(){
                VarysListener listener = new VarysListener();
                client = new VarysClient(clientName, masterUrl, listener);
                client.start();

                Utils.safePrintln("Master client before coflow "+ client.masterClientId());

                CoflowDescription desc = new CoflowDescription("TASK_"+taskId, CoflowType.SHUFFLE(), -1, -1);
                coflowId = String.valueOf(client.registerCoflow(desc));

                Utils.safePrintln("Master client after coflow "+ client.masterClientId());

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

        return coflowId;

    }

    public void unregisterCoflow(){
        // TODO: unregister
        VarysListener listener = new VarysListener();
        //client = new VarysClient(clientName, masterUrl, listener);
        //client.start();
        client.unregisterCoflow(Integer.parseInt(coflowId));
        client.stop();
        //client.stop();
    }

    public void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }
}
