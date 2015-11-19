package Simulation.Master;

import Simulation.Data.ConfigParser;
import Simulation.Data.SimulationConfig;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by stanislavmushits on 19/11/15.
 */
public class Main {

    public void main(String[] args) throws IOException, ParseException {
        SimulationConfig simConfig = ConfigParser.parseSimConfigFile("configs/simulation.json");
        Master master = new Master();
        master.conductSimulation(simConfig);
    }
}
