package Controller;

/**
 * Created by ruby on 11/17/15.
 */
public class SimEntry extends BasicSimEntry {

    Boolean isCoflow;
    int coflowNum;

    public SimEntry(int senderIndex, int receiverIndex,
                    long startTimeMs, long numBytes,
                    Boolean isCoflow, int coflowNum) {
        super(senderIndex, receiverIndex, startTimeMs, numBytes);
        this.isCoflow = isCoflow;
        this.coflowNum = coflowNum;
    }

}
