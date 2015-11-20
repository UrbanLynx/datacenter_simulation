package Simulation.Data;

import com.sun.javafx.tk.Toolkit;

import java.io.Serializable;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class TaskData implements Serializable {
    String data;
    long bytes;
    public String dataId;

    public void generateData(long numBytes) {
        // Creates a string with proper size
        // One char is 1 Byte. Using StringBuffer to improve memory performance
        StringBuffer outputBuffer = new StringBuffer();
        for (int i = 0; i<this.bytes; ++i) {
            outputBuffer.append('a');
        }
        this.data = outputBuffer.toString();
    }

    public String getData() {
        return this.data;
    }

    public TaskData(long bytes) {
        this.bytes = bytes;
        generateData(bytes);
    }
}
