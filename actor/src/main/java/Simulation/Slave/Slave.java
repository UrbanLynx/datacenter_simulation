package Simulation.Slave;

import Simulation.Communicators.Communicator;
import Simulation.Data.*;

import java.io.IOException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Slave implements Runnable{

    private SimTask task;
    private boolean isSender;

    private SimMessage message;

    public Slave(SimMessage message) {
        this.task = message.task;
        isSender = (message.eventType == SimMessage.SimEventType.SEND);

        this.message = message;
    }


    public void run() {
        Communicator communicator = new Communicator();

        if (this.isSender) {
            communicator.send(task);
        } else {
            communicator.receive(task);
        }

        /*switch(this.task.simulationType){
            case VARYS:

                Communicator communicator = new Communicator();

                if (this.isSender) {
                    communicator.send(task);
                } else {
                    communicator.receive(task);
                }
                break;
            case TRADITIONAL:
                TraditionalCommunicator traditionalCommunicator = new TraditionalCommunicator();
                if (!this.isSender) {
                    traditionalCommunicator.receive(task);
                } else {
                    traditionalCommunicator.send(task);
                }
                break;
        }*/
    }

    public void ackToMaster(Confirm.Code code){
        try {
            message.outputToMaster.writeObject(new Confirm(code));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
