package Simulation.Data;

/**
 * Created by ruby on 11/17/15.
 */
public class SimEntry extends BasicSimEntry {

    Boolean isCoflow;
    int coflowID;

    public SimEntry(int senderIndex, int receiverIndex,
                    long startTimeMs, long numBytes,
                    Boolean isCoflow, int coflowID) {
        super(senderIndex, receiverIndex, startTimeMs, numBytes);
        this.isCoflow = isCoflow;
        this.coflowID = coflowID;
    }

}
