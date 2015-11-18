package Controller;

import java.io.Serializable;

/**
 * Created by ruby on 11/17/15.
 */
public class SimEventDesc implements Serializable {

    public SimEventType event;
    public int numBytes;

    public SimEventDesc(SimEventType event, int numBytes) {
        this.event = event;
        this.numBytes = numBytes;
    }

    public String toString() {
        return (event.toString() + " " + numBytes + " bytes");
    }

}
