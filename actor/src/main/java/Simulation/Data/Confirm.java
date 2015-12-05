package Simulation.Data;

import java.io.Serializable;

/**
 * Created by ruby on 11/18/15.
 */

// this is just for slaves to confirm ready state to controller
public class Confirm implements Serializable {
    public enum Code{EXECUTED, SENT, RECEIVED}

    public Confirm(Code code){
        this.code = code;
    }

    public Code code;
}
