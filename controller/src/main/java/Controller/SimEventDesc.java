package Controller;

import javax.mail.search.SentDateTerm;
import java.io.Serializable;

/**
 * Created by ruby on 11/17/15.
 */
public class SimEventDesc implements Serializable {

    public SimEventType event;  // SEND, RECEIVE, TERMINATE
    // fields below only valid when event is SEND or RECEIVE
    public int numBytes;
    public String receiveHostName;  // only needed when SimEventType is SEND
    public int portNumber;

    public SimEventDesc(SimEventType event, int numBytes, String receiveHostName, int portNum) {
        this.event = event;
        this.numBytes = numBytes;
        this.receiveHostName = receiveHostName;
        this.portNumber = portNum;
    }

    public String toString() {
        String desc = null;
        switch (event) {
            case SEND:
            case RECEIVE:
                desc = (event.toString() + " " + numBytes + " bytes, " +
                        "receiver: " + receiveHostName + ":" + portNumber + " ");
                break;
            case TERMINATE:
                desc = "TERMINATE";
                break;
        }
        return desc;
    }

}
