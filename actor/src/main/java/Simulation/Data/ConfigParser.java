package Simulation.Data;

import Simulation.SimTask;
import Simulation.Data.SimulationConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {

    public static ArrayList<SimTask> parseTaskFile(String filename) throws IOException, ParseException {
        ArrayList<SimTask> tasks = null;
        try {
            FileReader reader = new FileReader(filename);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            SimTask task = new SimTask();

            task.dstAddress = (String) jsonObject.get("dstAddress");
            task.dstPort = (Integer) jsonObject.get("dstPort");
            task.duration = (Double) jsonObject.get("duration");
            task.finishTime = (Long) jsonObject.get("finishTime");
            task.mapper = (String) jsonObject.get("mapper");
            task.reducer = (String) jsonObject.get("reducer");
            task.size = (Long) jsonObject.get("size");
            task.srcAddress = (String) jsonObject.get("srcAddress");
            task.srcPort = (Integer) jsonObject.get("srcPort");
            task.startTime = (Long) jsonObject.get("startTime");

            tasks.add(task);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static SimulationConfig parseSimConfigFile(String filename){
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
}