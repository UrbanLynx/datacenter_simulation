package Controller;

/**
 * Created by ruby on 11/17/15.
 */
public class BasicSimEntry {
    String sendHost;
    String receiveHost;
    long startTimeMs;
    long numBytes;

    public BasicSimEntry(String sendHost, String receiveHost,
                    long startTimeMs, long numBytes) {
        this.sendHost = sendHost;
        this.receiveHost = receiveHost;
        this.startTimeMs = startTimeMs;
        this.numBytes = numBytes;
    }
}
