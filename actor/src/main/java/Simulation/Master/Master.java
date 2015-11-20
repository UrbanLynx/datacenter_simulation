package Simulation.Master;

import Simulation.Data.*;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import varys.framework.CoflowDescription;
import varys.framework.CoflowType;
import varys.framework.client.VarysClient;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Master {
    private Router router;
    private VarysClient masterClient;
    private SimulationConfig config;

    public void conductSimulation(SimulationConfig simConfig){
        config = simConfig;
        router = new Router(simConfig);
        registerMaster(simConfig);
        try {
            ArrayList<SimTask> simTasks = ConfigParser.parseTaskFile(simConfig.taskFileName);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void waitUntillNextTask(SimTask task) {

    }

    public void executeTraditionalTask(SimTask simTask){
        router.sendTaskTo(simTask.dstAddress, simTask);

        //TODO:how to wait
        waitForAck();

        router.sendTaskTo(simTask.srcAddress, simTask);
    }



    public void executeVarysTask(SimTask simTask){
        simTask.coflowId = registerCoflow(simTask);
        simTask.masterUrl = config.varysMasterUrl;

        router.sendTaskTo(simTask.srcAddress, simTask);
        router.sendTaskTo(simTask.dstAddress, simTask);
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
