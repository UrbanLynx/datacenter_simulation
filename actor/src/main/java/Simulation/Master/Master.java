package Simulation.Master;

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
        preparationForSimulation(simConfig);

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

    private void preparationForSimulation(SimulationConfig simConfig) {
        router = new Router(simConfig);

        if (simConfig.isVarys){
            registerMaster(simConfig);
        }
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
        simTask.coflowId = registerCoflow(simTask);
        simTask.masterUrl = config.varysMasterUrl;

        router.sendTaskTo(simTask.srcAddress, new SimMessage(SimMessage.SimEventType.SEND, simTask));

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        router.sendTaskTo(simTask.dstAddress, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));
    }

    public String registerCoflow(SimTask task){
        // TODO: Change coflow name
        CoflowDescription desc = new CoflowDescription("DEFAULT", CoflowType.DEFAULT(), 1, task.size, 10000);
        String coflowId = masterClient.registerCoflow(desc);
        return coflowId;
    }

    public void registerMaster(SimulationConfig config){
        VarysListener listener = new VarysListener();
        masterClient = new VarysClient("Simulation.Master", config.varysMasterUrl, listener);
        masterClient.start();
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
