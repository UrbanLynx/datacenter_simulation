package Simulation;

import varys.framework.client.ClientListener;
import varys.framework.client.VarysClient;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Stanislav-macbook on 31.10.2015.
 */
public class VarysReceiver implements Runnable{

    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public VarysReceiver(SimulationConfig simConfig) {
        url = simConfig.varysMasterUrl;
    }

    public void run() {
        safePrintln("[Receiver]: Starting thread with receiver");
        /*synchronized(this){
            this.runningThread = Thread.currentThread();
        }*/

        //openServerSocket();
        this.receive();
        /*while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
        }*/

        System.out.println("[Receiver]: Receiver Stopped.");
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

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        /*try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }*/
    }

    String url;
    String coflowId;

    public void receive(){
        safePrintln("[Receiver]: Start receiving on URL: "+url);

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
