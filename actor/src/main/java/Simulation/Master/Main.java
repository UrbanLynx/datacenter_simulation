package Simulation.Master;

import Simulation.Communicators.Utils;
import Simulation.Data.ConfigParser;
import Simulation.Data.DataGenerator;
import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;
import Simulation.Logger.SimLogger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {

        Utils.logger = new SimLogger("Master", true, true);
        //Utils.logger.log(Level.INFO, "Master log initiated");

        SimulationConfig simConfig = ConfigParser.parseSimConfigFile("configs/simulation.json");
        ArrayList<SimTask> simTasks = ConfigParser.parseTaskFile(simConfig);
        Master master = new Master();
        master.conductSimulation(simConfig, simTasks);

        Utils.logger.stopLogger();
        Utils.wait(3000);
        System.exit(0);
        //Utils.safePrintln("Master conducted simulation and exited.");
    }
}
