package Simulation.Logger;

import Simulation.Data.SimTask;

/**
 * Created by ruby on 12/1/15.
 */
public class LogUtils {

    public enum Event{ SEND, RECEIVE }

    public static String getSlaveLogContent(SimTask task, Event event, String timestamp) {
        StringBuilder buf = new StringBuilder();
        buf.append(task.currentSlaveId+" ");
        buf.append(event.toString()+" ");
        buf.append(task.coflowId+" ");
        buf.append(timestamp+" ");
        buf.append("\n");
        return buf.toString();
    }

    public static String getMasterLogContent(SimTask task, String coflowID) {
        StringBuilder buf = new StringBuilder();
        buf.append(task.id+" ");
        buf.append(coflowID+" ");
        buf.append("\n");
        return buf.toString();
    }
}
