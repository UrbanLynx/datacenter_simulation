import Simulation.Simulation;
import Simulation.VarysSimulation;
import Simulation.TraditionalSimulation;

import Simulation.SimulationConfig;
import org.apache.commons.cli.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class Manager {
    public static void main(String[] args){
        SimulationConfig simConfig = readConfig("configs/simulation.json");
        Simulation simulation = createSimulation(simConfig);
        waitForStartOfSimulation(simConfig);
        conductSimulation(simulation);
        processResultsOfSimulation(simulation);
    }

    public static SimulationConfig readConfig(String filename){
        try {
            FileReader reader = new FileReader(filename);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            SimulationConfig config = new SimulationConfig();
            config.taskFileName = (String) jsonObject.get("TasksFile");
            config.simulationStartTime = new DateTime((String) jsonObject.get("StartTime"), DateTimeZone.UTC);
            config.isVarys = (Boolean) jsonObject.get("IsVarys");
            if (config.isVarys) {
                config.varysMasterUrl = (String) jsonObject.get("MasterUrl");
            }
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        /*Options options = new Options();

        options.addOption("tasks", true, "Task filename");
        options.addOption("time", true, "Simulation start time ISO 8601 string (UTC)");
        options.addOption("varys", false, "Use Vary's scheduler");
        options.addOption("url", true, "Vary's Master URL");

        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse( options, args );

            SimulationConfig config = new SimulationConfig();
            config.taskFileName = cmd.getOptionValue("tasks");
            config.simulationStartTime = new DateTime(cmd.getOptionValue("time"), DateTimeZone.UTC);
            if (cmd.hasOption("varys")){
                config.isVarys = true;
                config.varysMasterUrl = cmd.getOptionValue("url");
            }
            return config;
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }*/

    }

    public static Simulation createSimulation(SimulationConfig simConfig){
        if (simConfig.isVarys){
            return new VarysSimulation(simConfig);
        } else{
            return new TraditionalSimulation(simConfig);
        }
    }

    public static void waitForStartOfSimulation(SimulationConfig simConfig){
        return;
    }

    public static void conductSimulation(Simulation simulation){
        return;
    }

    public static void processResultsOfSimulation(Simulation simulation){
        return;
    }
}
