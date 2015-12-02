package Simulation.Master.Utils;

import Simulation.Communicators.VarysRegistrator;
import Simulation.Data.Reducer;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationType;

import java.util.ArrayList;

/**
 * Created by stanislavmushits on 02/12/15.
 */
public class TaskProgress {
    public SimTask task;
    public VarysRegistrator registrator;
    public ArrayList<Reducer> nonFinishedReducers = new ArrayList<Reducer>();
    private Boolean isFinished = false;

    public TaskProgress(SimTask task) {
        this.task = task;
        nonFinishedReducers.addAll(task.reducersArr);
    }

    public void removeReducer(SimTask task) {
        Reducer reducerToRemove = nonFinishedReducers.get(task.currentSlaveTaskIndex);
        nonFinishedReducers.remove(reducerToRemove);
        checkIsFinished();
    }

    public Boolean isFinished(){
        return isFinished;
    }

    private void checkIsFinished(){
        if (nonFinishedReducers.size()==0) {
            if (task.simulationType == SimulationType.VARYS) {
                registrator.unregisterCoflow();
            }
            isFinished = true;
        }
    }

}
