package Simulation.Slave;

import Simulation.Data.SimTask;
import Simulation.Data.SimulationType;
import Simulation.Data.TaskData;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Slave implements Runnable{

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
        if (this.task.simulationType == SimulationType.VARYS) {
            VarysCommunicator varysCommunicator = new VarysCommunicator(task);
            if (this.isReceiver) {
                varysCommunicator.receive();
            } else {
                varysCommunicator.send(task.data.getData());
            }
        } else {
            TraditionalCommunicator traditionalCommunicator = new TraditionalCommunicator();
            if (this.isReceiver) {
                // Call receive task of traditional
                traditionalCommunicator.receive(task.srcPort, task.size);
            } else {
                // Call send task of traditional
                traditionalCommunicator.receive(task.srcPort, task.size);
            }
        }

    }

}
