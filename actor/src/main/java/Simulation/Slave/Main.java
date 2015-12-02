package Simulation.Slave;

import Simulation.Communicators.Utils;
import Simulation.Data.SimulationConfig;
import Simulation.Logger.SimLogger;

import java.io.IOException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        Utils.logger = new SimLogger("Slave", false, false);

        SimulationConfig config = getDefaultConfig();
        Router router = new Router(config);
        router.listenForTasks();

        Utils.logger.stopLogger();

    }

    public static SimulationConfig getDefaultConfig(){
        SimulationConfig config = new SimulationConfig();
        config.slavePort = 5000;
        return config;
    }
}
