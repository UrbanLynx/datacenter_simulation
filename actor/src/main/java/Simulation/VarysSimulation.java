package Simulation;

import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class VarysSimulation extends Simulation {
    public SimulationConfig simulationConfig;

    public VarysSimulation(ArrayList<SimTask> simTasks, SimulationConfig simConfig) {
        super(simTasks);
        simulationConfig = simConfig;
    }
}
