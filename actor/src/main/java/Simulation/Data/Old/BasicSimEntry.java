package Simulation.Data.Old;

/**
 * Created by ruby on 11/17/15.
 */
public class BasicSimEntry {
    int senderIndex;         // index into an array maintained by BasicController
    int receiverIndex;       // index into an array maintained by BasicController
    long startTimeMs;
    long numBytes;

    public BasicSimEntry(int senderIndex, int receiverIndex,
                    long startTimeMs, long numBytes) {
        this.senderIndex = senderIndex;
        this.receiverIndex = receiverIndex;
        this.startTimeMs = startTimeMs;
        this.numBytes = numBytes;
    }
}
