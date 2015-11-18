package Controller;

import varys.framework.client.VarysClient;

/**
 * Created by ruby on 11/17/15.
 */

// Incorporate Varys scheduler
public class Slave extends BasicSlave {

    int ID;     // Controller makes sure every slave has a unique ID
    String varysMasterURL;
    VarysClient varysClient;

    Slave(int ID, int portNumber, String varysMasterURL) {
        super(portNumber);
        this.ID = ID;
        this.varysMasterURL = varysMasterURL;
        TestListener listener = new TestListener();
        varysClient = new VarysClient("SimulationSlave-"+ID, varysMasterURL, listener);
        varysClient.start();
    }

}
