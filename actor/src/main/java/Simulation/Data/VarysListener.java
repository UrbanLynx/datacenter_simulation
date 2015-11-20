package Simulation.Data;

import varys.framework.client.ClientListener;

/**
 * Created by ruby on 11/18/15.
 */

// VarysListener copied from Stas' Actor code
public class VarysListener implements ClientListener {
    public void connected(String id) {
        System.out.println("Log: Connected to master, got client ID " + id);
    }

    public void  disconnected() {
        System.out.println("[Sender]: Log: Disconnected from master");
        System.exit(0);
    }

    public void coflowRejected(String s, String s1) {

    }
}
