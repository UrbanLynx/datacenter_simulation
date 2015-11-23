package Simulation.Master;

import Simulation.Communicators.VarysRegistrator;
import Simulation.Data.*;
import Simulation.Data.ConfigParser;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.VarysClient;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Master {
    private Router router;
    private VarysClient masterClient;
    private SimulationConfig config;

    public void conductSimulation(SimulationConfig simConfig, ArrayList<SimTask> simTasks){
        config = simConfig;
        router = new Router(config);
        //preprocessTaks(simTasks);
        //preparationForSimulation();

        for (SimTask task : simTasks){
            waitUntillNextTask(task);
            switch (task.simulationType){
                case TRADITIONAL:
                    executeTraditionalTask(task);
                    break;
                case VARYS:
                    executeVarysTask(task);
                    break;
            }
        }

        // TODO: somehow wait for end of simulation
        System.out.println("All tasks accomplished!");
        wait(200000);
    }



    private void waitUntillNextTask(SimTask task) {

    }

    public void executeTraditionalTask(SimTask simTask){
//        router.sendTaskTo(simTask.dstAddress, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));

        //TODO:how to wait
//        waitForAck();

        for (Reducer reducer: simTask.reducers){
            simTask.currentSlaveId = reducer.reducerId;
            router.sendTaskTo(reducer.reducerId, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));
        }

        // TODO: After putObj Slave-sender connects to Slave-receiver via TCP socket and tells to receive.
        // TODO: After receiving a task from master, Slave-receiver waits for message
        // TODO: from Salve-sender and only after that registers VarysClient and getObd.
        wait(15000);

        for (int hostIndex: simTask.mappers){
            simTask.currentSlaveId = hostIndex;
            router.sendTaskTo(hostIndex, new SimMessage(SimMessage.SimEventType.SEND, simTask));
        }

        wait(100000);

//        router.sendTaskTo(simTask.srcAddress, new SimMessage(SimMessage.SimEventType.SEND, simTask));
    }



    public void executeVarysTask(SimTask simTask){
        simTask.masterUrl = config.varysMasterUrl;
        simTask.coflowId = registerCoflow(simTask);

        for (int hostIndex: simTask.mappers){
            simTask.currentSlaveId = hostIndex;
            router.sendTaskTo(hostIndex, new SimMessage(SimMessage.SimEventType.SEND, simTask));
        }


        // TODO: After putObj Slave-sender connects to Slave-receiver via TCP socket and tells to receive.
        // TODO: After receiving a task from master, Slave-receiver waits for message
        // TODO: from Salve-sender and only after that registers VarysClient and getObd.
        wait(15000);

        // !!! If you make Debug (not Run) of program you need wait(90000)
        // because slaves are very slow for some reason in debug mode
        for (Reducer reducer: simTask.reducers){
            simTask.currentSlaveId = reducer.reducerId;
            router.sendTaskTo(reducer.reducerId, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));
        }
        wait(100000);
    }

    public void wait(int millisec){
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String registerCoflow(SimTask task){
        // TODO: change size, name of registrator, number of slaves(senders), config->task
        VarysRegistrator registrator = new VarysRegistrator(config.varysMasterUrl, "ActorMaster", 1, task.size);
        return registrator.registerCoflow();
    }

    public static void processResultsOfSimulation(){
        return;
    }


    public void waitForAck(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void preparationForSimulation() {
        router = new Router(config);
    }

    // TODO: Sort and extract IPs
    private void preprocessTaks(ArrayList<SimTask> tasks){
        HashSet<String> addresses = new HashSet<String>();
        for (SimTask task: tasks){
            addresses.add(task.srcAddress);
            addresses.add(task.dstAddress);
        }

        //config.hosts = new ArrayList<String>();
        //config.hosts.addAll(addresses);
    }
}
