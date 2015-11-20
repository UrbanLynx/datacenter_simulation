package Simulation.Slave;

import Simulation.Data.SimMessage;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationType;
import Simulation.Data.TaskData;

import java.net.InetAddress;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Slave implements Runnable{

    SimTask task;
    InetAddress myIpAddress;
    boolean isSender;

    /*public Slave(SimTask task) throws UnknownHostException {
        this.task = task;
        this.myIpAddress = InetAddress.getLocalHost();
    }*/

    public Slave(SimMessage message) {
        this.task = message.task;
        isSender = (message.eventType == SimMessage.SimEventType.SEND);
    }

    public void GenerateData() {
        // Sender, so create data
        task.data = new TaskData(task.size);
    }

    public void run() {

        //parse task

        // Check if receiver
        //this.isSender = AmISender();

        // if sender, call generate data
        if (this.isSender) {
            GenerateData();
        }

        // send the task based on varys or traditional
        if (this.task.simulationType == SimulationType.VARYS) {
            VarysCommunicator varysCommunicator = new VarysCommunicator();
            if (this.isSender) {
                varysCommunicator.send(task);
            } else {
                varysCommunicator.receive(task);
            }
        } else {
            TraditionalCommunicator traditionalCommunicator = new TraditionalCommunicator();
            if (this.isSender) {
                // Call receive task of traditional
                traditionalCommunicator.receive(task);
            } else {
                // Call send task of traditional
                traditionalCommunicator.send(task);
            }
        }

    }

}
