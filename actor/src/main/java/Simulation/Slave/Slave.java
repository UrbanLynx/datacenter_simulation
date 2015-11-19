package Simulation.Slave;

import Simulation.Data.SimTask;
import Simulation.Data.TaskData;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Slave {

    SimTask task;
    InetAddress myIpAddress;
    boolean isReceiver;

    Slave(SimTask t) throws UnknownHostException {
        this.task = t;
        this.myIpAddress = InetAddress.getLocalHost();
    }

    public boolean AmIReceiver() {
        return task.dstAddress.equals(myIpAddress.toString());
    }

    public void GenerateData() {
        if (!AmIReceiver()) {
            // Sender, so create data
            task.data = new TaskData(task.size);
        }
    }

    public void run() {

        //parse task

        // Check if receiver
        this.isReceiver = AmIReceiver();

        // if sender, call generate data
        if (!this.isReceiver) {
            GenerateData();
        }

        // send the task based on varys or traditional
        if (this.task.simulationType == SimTask.SimulationType.VARYS) {
            if (this.isReceiver) {
                // Call receive task of varys
            } else {
                // Call send task of varys
            }
        } else {
            if (this.isReceiver) {
                // Call receive task of traditional
            } else {
                // Call send task of traditional
            }
        }

    }

}
