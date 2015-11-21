package Simulation.Communicators;

import Simulation.Data.SimulationConfig;
import varys.VarysException;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.ClientListener;
import varys.ui.VarysUI;
import varys.util.AkkaUtils;
import varys.Logging;
import varys.Utils;
import varys.framework.client.VarysClient;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class VarysSender implements Runnable {
    String url;
    public String coflowId;

    public VarysSender(String url, String coflowId) {
        this.url = url;
        this.coflowId = coflowId;
    }

    public void run() {
        safePrintln("[Sender]: Starting thread with sender.");

        this.sendFakeWorking();

        System.out.println("[Sender]: Sender Stopped.");
    }

    public void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    class TestListener implements ClientListener {
        public void connected(String id) {
            safePrintln("Log: Connected to master, got client ID " + id);
        }

        public void  disconnected() {
            safePrintln("[Sender]: Log: Disconnected from master");
            System.exit(0);
        }

        public void coflowRejected(String s, String s1) {

        }
    }

    public void registerCoflow(){
        long LEN_BYTES = 1010101L;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 5, LEN_BYTES, 10000);
        coflowId = client.registerCoflow(desc);
    }

    public void sendToCoflow(){
        safePrintln("[Sender]: Sending data with url " + url);
        String DATA_NAME = "DATA";
        long LEN_BYTES = 1010101L;


        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("Lalala", url, listener);
        client.start();

        int SLEEP_MS1 = 5000;
        safePrintln("[Sender]: Registered coflow " + coflowId);

        client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        safePrintln("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        client.awaitTermination();
    }

    public void sendFakeWorking() {
        sendToCoflow();
    }

    public void sendFakeWorkingFull() {
        safePrintln("[Sender]: Sending data with url " + url);
        String DATA_NAME = "DATA";
        long LEN_BYTES = 1010101L;


        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 5, LEN_BYTES, 10000);
        coflowId = client.registerCoflow(desc);

        int SLEEP_MS1 = 5000;
        safePrintln("[Sender]: Registered coflow " + coflowId);


        client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        safePrintln("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        client.awaitTermination();
    }

}

