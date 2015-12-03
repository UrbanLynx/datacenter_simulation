package Simulation.Master.Utils;

import Simulation.Communicators.VarysRegistrator;
import Simulation.Data.Reducer;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stanislavmushits on 02/12/15.
 */
public class TaskProgress {
    public SimTask task;
    public VarysRegistrator registrator;
    public Map<Integer, Reducer> nonFinishedReducers = new HashMap<Integer, Reducer>();
    private Boolean isFinished = false;

    public TaskProgress(SimTask task) {
        this.task = task;
        for(int i=0; i<task.reducersArr.size(); i++){
            nonFinishedReducers.put(i, task.reducersArr.get(i));
        }
    }

    public void removeReducer(SimTask task) {
        //Reducer reducerToRemove = nonFinishedReducers.get(task.currentSlaveTaskIndex);
        nonFinishedReducers.remove(task.currentSlaveTaskIndex);
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
