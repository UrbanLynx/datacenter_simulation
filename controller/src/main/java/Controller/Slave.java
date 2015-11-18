package Controller;

import varys.framework.client.VarysClient;
import varys.framework.client.ClientListener;

/**
 * Created by ruby on 11/17/15.
 */

// Incorporate Varys scheduler
public class Slave extends BasicSlave {

    Slave(int portNumber) {
        super(portNumber);
    }

}
