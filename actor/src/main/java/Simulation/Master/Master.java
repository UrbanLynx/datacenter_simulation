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
        preprocessTaks(simTasks);
        preparationForSimulation();

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
    }

    private void preparationForSimulation() {
        router = new Router(config);
    }

    // Sort and extract IPs
    private void preprocessTaks(ArrayList<SimTask> tasks){
        HashSet<String> addresses = new HashSet<String>();
        for (SimTask task: tasks){
            addresses.add(task.srcAddress);
            addresses.add(task.dstAddress);
        }

        config.hosts = new ArrayList<String>();
        config.hosts.addAll(addresses);
    }

    private void waitUntillNextTask(SimTask task) {

    }

    public void executeTraditionalTask(SimTask simTask){
        router.sendTaskTo(simTask.dstAddress, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));

        //TODO:how to wait
        waitForAck();

        router.sendTaskTo(simTask.srcAddress, new SimMessage(SimMessage.SimEventType.SEND, simTask));
    }



    public void executeVarysTask(SimTask simTask){
        simTask.masterUrl = config.varysMasterUrl;
        simTask.coflowId = registerCoflow(simTask);

        router.sendTaskTo(simTask.srcAddress, new SimMessage(SimMessage.SimEventType.SEND, simTask));

        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //router.sendTaskTo(simTask.dstAddress, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));
    }

    public String registerCoflow(SimTask task){
        // TODO: change size, name of registrator, number of slaves(senders), config->task
        long LEN_BYTES = 1010101L;
        VarysRegistrator registrator = new VarysRegistrator(config.varysMasterUrl, "ActorMaster", 1, LEN_BYTES);

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
}
