package Simulation.Master;

import Simulation.Data.ConfigParser;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;
import Simulation.Data.SimulationType;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Master {

    public void conductSimulation(SimulationConfig simConfig){
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

    }

    public void executeVarysTask(SimTask simTask){

    }

    public static void processResultsOfSimulation(){
        return;
    }
}
