package Simulation;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 03.11.2015.
 */
public class VarysSimulationTest extends TestCase {

    public void testSenderRegister() throws Exception {
        String varysMasterUrl = "varys://stanislavs-air:1606";

        VarysSender sender = new VarysSender();
        sender.registerCoflow(varysMasterUrl);
    }

    public void testSenderSend() throws Exception {
        String varysMasterUrl = "varys://stanislavs-air:1606";

        VarysSender sender = new VarysSender();
        sender.registerCoflowAndSend(varysMasterUrl);
    }

    public void testSender() throws Exception {
        String varysMasterUrl = "varys://stanislavs-air:1606";
        String coflowId = "COFLOW-000002";

        VarysSender sender = new VarysSender();
        sender.sendToCoflow(varysMasterUrl, coflowId);
    }

    public void testReceiver() throws Exception {
        String varysMasterUrl = "varys://stanislavs-air:1606";
        String coflowId = "COFLOW-000002";

        VarysReceiver receiver = new VarysReceiver();
        receiver.receive(varysMasterUrl, coflowId);
    }


}