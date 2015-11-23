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
        for (Reducer reducer: simTask.reducers.values()){
            simTask.currentSlaveId = reducer.reducerId;
            router.sendTaskTo(reducer.reducerId, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));
        }

        for (int hostIndex: simTask.mappers){
            simTask.currentSlaveId = hostIndex;
            router.sendTaskTo(hostIndex, new SimMessage(SimMessage.SimEventType.SEND, simTask));
        }
    }



    public void executeVarysTask(SimTask simTask){
        simTask.masterUrl = config.varysMasterUrl;
        simTask.coflowId = registerCoflow(simTask);

        executeTraditionalTask(simTask);

        /*for (int hostIndex: simTask.mappers){
            simTask.currentSlaveId = hostIndex;
            router.sendTaskTo(hostIndex, new SimMessage(SimMessage.SimEventType.SEND, simTask));
        }

        for (Reducer reducer: simTask.reducers.values()){
            simTask.currentSlaveId = reducer.reducerId;
            router.sendTaskTo(reducer.reducerId, new SimMessage(SimMessage.SimEventType.RECEIVE, simTask));
        }*/
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
        VarysRegistrator registrator = new VarysRegistrator(config.varysMasterUrl, "ActorMaster", 1, -1);
        return registrator.registerCoflow();
    }

    public static void processResultsOfSimulation(){
        return;
    }
}
