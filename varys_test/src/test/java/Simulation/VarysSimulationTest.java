package Simulation;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 03.11.2015.
 */
public class VarysSimulationTest extends TestCase {

    String coflowId = "COFLOW-000000";
    String varysMasterUrl = "varys://stanislavs-air:1606";

    public void testSenderRegister() throws Exception {

        VarysSender sender = new VarysSender();
        sender.registerCoflow(varysMasterUrl);
    }

    public void testSenderSend() throws Exception {

        VarysSender sender = new VarysSender();
        sender.registerCoflowAndSend(varysMasterUrl);
    }

    public void testSender() throws Exception {
        int senderID = 1;

        VarysSender sender = new VarysSender();
        sender.sendToCoflow(varysMasterUrl, coflowId, senderID);
    }

    public void testSender2() throws Exception {
        int senderID = 2;

        VarysSender sender = new VarysSender();
        sender.sendToCoflow(varysMasterUrl, coflowId, senderID);
    }

    public void testSender3() throws Exception {
        int senderID = 3;

        VarysSender sender = new VarysSender();
        sender.sendToCoflow(varysMasterUrl, coflowId, senderID);
    }

    public void testReceiver() throws Exception {
        int receiverID = 1;

        VarysReceiver receiver = new VarysReceiver();
        receiver.receive(varysMasterUrl, coflowId, receiverID);
    }

    public void testReceiver2() throws Exception {
        int receiverID = 2;

        VarysReceiver receiver = new VarysReceiver();
        receiver.receive(varysMasterUrl, coflowId, receiverID);
    }

    public void testReceiver3() throws Exception {
        int receiverID = 3;

        VarysReceiver receiver = new VarysReceiver();
        receiver.receive(varysMasterUrl, coflowId, receiverID);
    }



}