package Simulation.Communicators;

import Simulation.Data.SimulationConfig;
import varys.framework.client.ClientListener;
import varys.framework.client.VarysClient;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class VarysReceiver implements Runnable{
    public VarysReceiver(String url) {
        this.url = url;
    }

    public void run() {
        safePrintln("[Receiver]: Starting thread with receiver");
        this.receive();
    }

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

    String url;
    String coflowId;

    public void receive(){
        safePrintln("[Receiver]: Start receiving on URL: "+url);

        String DATA_NAME = "DATA";

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("ReceiverClientFake", url, listener);
        client.start();

        try {
            //Thread.sleep(5000);
            safePrintln("[Receiver]: Trying to retrieve " + DATA_NAME);
            client.getFake(DATA_NAME, coflowId);
            safePrintln("[Receiver]: Got " + DATA_NAME + " Now waiting to die.");
            //client.awaitTermination();
        } catch (Exception e) {
            safePrintln(e.toString());
        }
    }
}

