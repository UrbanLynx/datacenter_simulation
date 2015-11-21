package Simulation.Slave;

import Simulation.Data.SimulationConfig;

import java.io.IOException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SimulationConfig config = getDefaultConfig();
        Router router = new Router(config);
        router.listenForTasks();
    }

    public static SimulationConfig getDefaultConfig(){
        SimulationConfig config = new SimulationConfig();
        config.slavePort = 5000;
        return config;
    }
}
