package Simulation.Slave;

import Simulation.Communicators.TraditionalCommunicator;
import Simulation.Communicators.VarysCommunicator;
import Simulation.Data.SimMessage;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationType;
import Simulation.Data.TaskData;

import java.net.InetAddress;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Slave implements Runnable{

    private SimTask task;
    private boolean isSender;

    public Slave(SimMessage message) {
        this.task = message.task;
        isSender = (message.eventType == SimMessage.SimEventType.SEND);
    }

    public void GenerateData() {
        task.data = new TaskData(task.size);
    }

    public void run() {

        if (this.isSender) {
            GenerateData();
        }

        switch(this.task.simulationType){
            case VARYS:
                VarysCommunicator varysCommunicator = new VarysCommunicator();
                if (this.isSender) {
                    varysCommunicator.send(task);
                } else {
                    varysCommunicator.receive(task);
                }
                break;
            case TRADITIONAL:
                TraditionalCommunicator traditionalCommunicator = new TraditionalCommunicator();
                if (this.isSender) {
                    traditionalCommunicator.receive(task);
                } else {
                    traditionalCommunicator.send(task);
                }
                break;
        }
    }

}
