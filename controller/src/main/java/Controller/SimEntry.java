package Controller;

/**
 * Created by ruby on 11/17/15.
 */
public class SimEntry extends BasicSimEntry {

    Boolean isCoflow;
    int coflowNum;

    public SimEntry(String sendHost, String receiveHost,
                    long startTimeMs, long numBytes,
                    Boolean isCoflow, int coflowNum) {
        super(sendHost, receiveHost, startTimeMs, numBytes);
        this.isCoflow = isCoflow;
        this.coflowNum = coflowNum;
    }
}
