import Simulation.SimTask;
import Simulation.SimulationConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {

    public ArrayList<SimTask> parseTaskFile(String filename){
        return null;
    }

    public SimulationConfig parseSimConfigFile(String filename){
        try {
            FileReader reader = new FileReader(filename);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            SimulationConfig config = new SimulationConfig();
            config.taskFileName = (String) jsonObject.get("TasksFile");
            config.simulationStartTime = new DateTime((String) jsonObject.get("StartTime"), DateTimeZone.UTC);
            config.isVarys = (Boolean) jsonObject.get("IsVarys");
            config.serverPort = (Integer) jsonObject.get("ServerPort");
            if (config.isVarys) {
                config.varysMasterUrl = (String) jsonObject.get("MasterUrl");
            }
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public SimulationConfig parseSimConfigArgs(String[] args){
        Options options = new Options();

        options.addOption("simTasks", true, "SimTask filename");
        options.addOption("time", true, "Simulation start time ISO 8601 string (UTC)");
        options.addOption("varys", false, "Use Vary's scheduler");
        options.addOption("url", true, "Vary's Master URL");

        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse( options, args );

            SimulationConfig config = new SimulationConfig();
            config.taskFileName = cmd.getOptionValue("simTasks");
            config.simulationStartTime = new DateTime(cmd.getOptionValue("time"), DateTimeZone.UTC);
            if (cmd.hasOption("varys")){
                config.isVarys = true;
                config.varysMasterUrl = cmd.getOptionValue("url");
            }
            return config;
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
        return null;
    }*/
}
