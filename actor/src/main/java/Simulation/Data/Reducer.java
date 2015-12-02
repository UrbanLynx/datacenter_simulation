package Simulation.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aborase on 11/22/15.
 */
public class Reducer implements Serializable {

    public int reducerId;
    public int sizeKB;

    public String address;
    public int port;

    public ArrayList<Integer> doNotRegisterFlow;

    public long sizeBytes(){
        int BytesInKBytes = 1024;
        return sizeKB * BytesInKBytes;
    }

}
