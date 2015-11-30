package Simulation.Master;

import Simulation.Communicators.Utils;
import Simulation.Communicators.VarysRegistrator;
import Simulation.Data.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Master {
    private Router router;
    private ArrayList<VarysRegistrator> masterClients = new ArrayList<VarysRegistrator>();
    private SimulationConfig config;
    private int timeFromStartSimulation = 0;

    public void conductSimulation(SimulationConfig simConfig, ArrayList<SimTask> simTasks) throws IOException {
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
        Utils.wait(200000);
    }



    private void waitUntillNextTask(SimTask task) {
        // TODO: change startTime from double to int
        Utils.wait((int)task.startTime - timeFromStartSimulation);
        timeFromStartSimulation = (int)task.startTime;
    }

    public void executeTraditionalTask(SimTask simTask){
        for (Reducer reducer: simTask.reducersArr){
            Utils.safePrintln(String.valueOf(reducer.reducerId) +" "+ String.valueOf(reducer.port));
            SimTask sendTask = new SimTask(simTask);
            sendTask.currentSlaveId = reducer.reducerId;
            sendTask.currentSlavePort = reducer.port;
//            simTask.currentSlaveId = reducer.reducerId;
//            simTask.currentSlavePort = reducer.port;
            router.sendTaskTo(reducer.reducerId, new SimMessage(SimMessage.SimEventType.RECEIVE, sendTask));
        }

        for (int i=0; i<simTask.mappers.size(); i++){
            int hostIndex = simTask.mappers.get(i);
            simTask.currentSlaveTaskIndex = i;
        /*}
        for (int hostIndex: simTask.mappers){*/
            SimTask sendTask = new SimTask(simTask);
            sendTask.currentSlaveId = hostIndex;
            router.sendTaskTo(hostIndex, new SimMessage(SimMessage.SimEventType.SEND, sendTask));
        }
    }



    public void executeVarysTask(SimTask simTask){
        simTask.masterUrl = config.varysMasterUrl;
        simTask.coflowId = registerCoflow(simTask);

        executeTraditionalTask(simTask);
    }



    public String registerCoflow(SimTask task){
        // TODO: change sizeKB, name of registrator, number of slaves(senders), config->task
        VarysRegistrator registrator = new VarysRegistrator(config.varysMasterUrl, "ActorMaster"+task.id, 1, -1);
        masterClients.add(registrator);
        return registrator.registerCoflow();
    }

    public static void processResultsOfSimulation(){
        return;
    }
}
