package Simulation.Slave;

import Simulation.Communicators.TraditionalCommunicator;
import Simulation.Communicators.VarysCommunicator;
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

    public void GenerateData() {
        task.data = new TaskData(100);
    }

    public void run() {

        if (this.isSender) {
            // TODO: GENERATE DATA FOR EACH REDUCER
            GenerateData();
        }

        switch(this.task.simulationType){
            case VARYS:
                VarysCommunicator varysCommunicator = new VarysCommunicator();
                if (this.isSender) {
                    varysCommunicator.send(task);
                    //ackToMaster(Confirm.Code.SENT);
                } else {
                    varysCommunicator.receive(task);
                    //ackToMaster(Confirm.Code.RECEIVED);
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
        }
    }

    public void ackToMaster(Confirm.Code code){
        try {
            message.outputToMaster.writeObject(new Confirm(code));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
