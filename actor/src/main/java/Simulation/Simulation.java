package Simulation;

import Simulation.Data.SimTask;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public abstract class Simulation {
    protected ArrayList<SimTask> simTasks;

    public Simulation( ArrayList<SimTask> simTasks){
        this.simTasks = simTasks;
    }

    public void conductSimulation(){
        for (SimTask simTask : simTasks){
            executeTask(simTask);
        }
    }

    public void executeTask(SimTask simTask){

    }
}
