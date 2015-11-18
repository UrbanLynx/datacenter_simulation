package Controller;

import varys.framework.client.VarysClient;
import varys.framework.client.ClientListener;

/**
 * Created by ruby on 11/17/15.
 */

// Incorporate Varys scheduler
public class Controller extends BasicController {

    String masterURL;       // Varys MasterUrl
    VarysClient client;

    // constructor starts Varys client
    public Controller(String varysMasterUrl) {
        masterURL = varysMasterUrl;
        TestListener listener = new TestListener();
        client = new VarysClient("SimulationController", masterURL, listener);
        client.start();
    }

    // TestListener copied from Stas' Actor code
    class TestListener implements ClientListener {
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

}
