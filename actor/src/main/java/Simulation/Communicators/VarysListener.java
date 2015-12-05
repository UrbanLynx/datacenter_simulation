package Simulation.Communicators;

import varys.framework.client.ClientListener;

/**
 * Created by ruby on 11/18/15.
 */

// VarysListener copied from Stas' Actor code
public class VarysListener implements ClientListener {
    public void connected(String id) {
        Utils.safePrintln("Log: Connected to master, got client ID " + id);
    }

    public void  disconnected() {
        Utils.safePrintln("Log: Disconnected from master");
        //System.exit(0);
    }

    public void coflowRejected(int i, String s) {

    }


}
