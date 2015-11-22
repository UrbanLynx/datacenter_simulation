package Simulation.Data;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class SimMessage implements Serializable {


    public enum SimEventType {
        SEND,
        RECEIVE,
        TERMINATE,
    }

    public SimMessage(SimEventType eventType){this(eventType, null);}

    public SimMessage(SimEventType eventType, SimTask task){
        this.task = task;
        this.eventType = eventType;
    }

    public SimEventType eventType;
    public SimTask task;
    public ObjectOutputStream outputToMaster;
}
