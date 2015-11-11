package Simulation;

import scala.Enumeration.Value;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import varys.VarysException;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.ClientListener;
import varys.util.AkkaUtils;
import varys.Logging;
import varys.Utils;
import varys.framework.client.VarysClient;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class VarysSender {
    String url;
    int coflowId;

    public VarysSender(SimulationConfig simConfig) {
        url = simConfig.varysMasterUrl;
    }

    public void init(String url, int coflowId){

    }

    class TestListener implements ClientListener {
        public void connected(String id) {
            System.out.println("Log: Connected to master, got client ID " + id);
        }

        public void  disconnected() {
            System.out.println("Log: Disconnected from master");
            System.exit(0);
        }

        public void coflowRejected(String s, String s1) {

        }
    }


    public void send(byte[] data) {
        coflowId = 0;
        String OBJ_NAME = "OBJ"; // to the args
        int NUM_ELEMS = data.length;
        byte[] toSend = data;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientObject", url, listener);
        client.start();

        // Last argument deadline, ms - what to write?
        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, NUM_ELEMS * 4,1000);
        coflowId = Integer.parseInt(client.registerCoflow(desc));

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

        client.awaitTermination();
    }
}
