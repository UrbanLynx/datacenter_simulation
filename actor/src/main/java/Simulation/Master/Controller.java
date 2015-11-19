package Simulation.Master;

import varys.framework.client.VarysClient;

/**
 * Created by ruby on 11/17/15.
 */

// Incorporate Varys scheduler
public class Controller extends BasicController {

    String varysMasterURL;
    VarysClient varysClient;

    // constructor starts Varys client
    public Controller(String varysMasterUrl) {
        varysMasterURL = varysMasterUrl;
        TestListener listener = new TestListener();
        varysClient = new VarysClient("SimulationController", varysMasterURL, listener);
        varysClient.start();
    }

}
