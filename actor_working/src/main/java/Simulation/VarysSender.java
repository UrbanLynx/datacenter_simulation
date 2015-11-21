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
public class VarysSender implements Runnable {
    String url;
    String coflowId;

    public VarysSender(SimulationConfig simConfig) {
        url = simConfig.varysMasterUrl;
    }

    public void run() {
        safePrintln("[Sender]: Starting thread with sender.");
        /*synchronized(this){
            this.runningThread = Thread.currentThread();
        }*/

        //openServerSocket();
        this.sendFakeWorking();
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


    public void send(byte[] data) {
        System.out.println("[Sender]: Sending data with url " + url);
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

        String DATA_NAME = "DATA";
        long LEN_BYTES = 1010101L;

        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("[Sender]: SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, LEN_BYTES, 10000);
        coflowId = client.registerCoflow(desc);

        int SLEEP_MS1 = 5000;
        System.out.println("[Sender]: Registered coflow " + coflowId + ". Now sleeping for " + SLEEP_MS1 + " milliseconds.");
        try {
            Thread.sleep(SLEEP_MS1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        System.out.println("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        //client.awaitTermination();
    }

    public void sendFakeWorking() {
        System.out.println("[Sender]: Sending data with url " + url);
        String DATA_NAME = "DATA";
        long LEN_BYTES = 1010101L;

        /*Thread t = new Thread(new Runnable() {
            public void run()
            {
                TestListener masterListener = new TestListener();
                VarysClient master = new VarysClient("MasterClientFake", url, masterListener);
                master.start();
                *//*try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*//*
                master.awaitTermination();
            }
        });
        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        TestListener listener = new TestListener();
        VarysClient client = new VarysClient("SenderClientFake", url, listener);
        client.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, LEN_BYTES, 10000);
        coflowId = client.registerCoflow(desc);

        int SLEEP_MS1 = 5000;
        System.out.println("[Sender]: Registered coflow " + coflowId);


        client.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
        System.out.println("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        client.awaitTermination();
    }

    public void sendFake4() {
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

    public void sendFake3() {
        System.out.println("[Sender]: Sending data with url " + url);
        final String DATA_NAME = "DATA";

        final long LEN_BYTES = 1010101L;
        TestListener masterListener = new TestListener();
        final VarysClient master = new VarysClient("MasterClientFake", url, masterListener);
        master.start();

        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, LEN_BYTES, 10000);
        coflowId = master.registerCoflow(desc);

        Thread t = new Thread(new Runnable() {
            public void run()
            {
                String DATA_NAME = "DATA";

                long LEN_BYTES = 1010101L;
                master.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        master.putFake(DATA_NAME, coflowId, LEN_BYTES, 1);

        System.out.println("[Sender]: Put a fake piece of data of " + LEN_BYTES + " bytes. Now waiting to die.");

        // client.unregisterCoflow(coflowId)

        master.awaitTermination();
    }

    public void sendFake() {
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

                CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(),10, LEN_BYTES, 70000);
                coflowId = master.registerCoflow(desc);

                master.putFake("DATA1", coflowId, LEN_BYTES, 1);

                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                master.awaitTermination();
            }
        });
        t.start();

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
        //t2.start();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
}
