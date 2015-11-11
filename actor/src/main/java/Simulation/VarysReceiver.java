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
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
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

        System.out.println("Server Stopped.");
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
    int coflowId;

    public void receive(){
        coflowId = 0;
        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("ReceiverClientObject", url, (ClientListener) listener);
        client.start();

        try {
            Thread.sleep(5000);
            String OBJ_NAME = "OBJ";
            System.out.println("Trying to retrieve " + OBJ_NAME);
            byte[] respArr = new byte[0];
            respArr = client.getObject(OBJ_NAME, String.valueOf(coflowId));
            System.out.println("Got " + OBJ_NAME + " with " + respArr.length + " elements. Now waiting to die.");
            client.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
