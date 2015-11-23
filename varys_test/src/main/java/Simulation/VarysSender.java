package Simulation;

import akka.actor.ActorRef;
import scala.Enumeration.Value;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
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
public class VarysSender{

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

        client.stop();
        //client.awaitTermination();
    }

    class TestListener implements ClientListener {
        public void connected(String id) {
            safePrintln("Log: Connected to master, got client ID " + id);
        }

        public void  disconnected() {
            safePrintln("[Sender]: Log: Disconnected from master");
            //System.exit(0);
        }

        public void coflowRejected(String s, String s1) {

        }
    }

    public void registerCoflow(String url) {
        long LEN_BYTES = 1010101L;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("[Sender]: SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 3, LEN_BYTES, 10000);
        String coflowId = client.registerCoflow(desc);
        System.out.println("Registered coflow " + coflowId);

        client.awaitTermination();
    }

    public void registerCoflowAndSend(String url) {
        System.out.println("[Sender]: Sending data with url " + url);
        String DATA_NAME = "DATA";
        long LEN_BYTES = 1010101L;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, LEN_BYTES, 10000);
        String coflowId = client.registerCoflow(desc);

        System.out.println("[Sender]: Registered coflow " + coflowId);


        client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        System.out.println("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        client.awaitTermination();
    }
}
