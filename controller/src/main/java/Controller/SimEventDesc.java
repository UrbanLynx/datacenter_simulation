package Controller;

import java.io.Serializable;

/**
 * Created by ruby on 11/17/15.
 */
public class SimEventDesc implements Serializable {

    public SimEventType event;
    public int numBytes;
    public int portNumber;

    public SimEventDesc(SimEventType event, int numBytes, int portNum) {
        this.event = event;
        this.numBytes = numBytes;
        this.portNumber = portNum;
    }

    public String toString() {
        return (event.toString() + " " + numBytes + " bytes");
    }

}
