package Simulation.Data;

import Simulation.Data.SimTask;
import Simulation.Data.SimulationConfig;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {

    public static ArrayList<SimTask> parseTaskFile(String filename) throws IOException, ParseException {
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        try {
            FileReader reader = new FileReader(filename);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray tasksContent = (JSONArray) jsonObject.get("tasks");
            Iterator i = tasksContent.iterator();

            while (i.hasNext()) {
                JSONObject jTask = (JSONObject) i.next();
                SimTask task = new SimTask();

                task.dstAddress = (String) jTask.get("dstAddress");
                task.dstPort = ((Long) jTask.get("dstPort")).intValue();
                task.duration = (Double) jTask.get("duration");
                task.finishTime = (Double) jTask.get("finishTime");
                task.mapper = (String) jTask.get("mapper");
                task.reducer = (String) jTask.get("reducer");
                task.size = (Long) jTask.get("size");
                task.srcAddress = (String) jTask.get("srcAddress");
                task.srcPort = ((Long) jTask.get("srcPort")).intValue();
                task.startTime = (Double) jTask.get("startTime");
                task.simulationType = SimulationType.valueOf((String) jsonObject.get("simulationType"));

                tasks.add(task);
            }
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
            config.taskFileName = (String) jsonObject.get("taskFile");
            //config.simulationStartTime = new DateTime((String) jsonObject.get("StartTime"), DateTimeZone.UTC);
            config.isVarys = (Boolean) jsonObject.get("isVarys");
            config.slavePort = ((Long) jsonObject.get("slavePort")).intValue();
            if (config.isVarys) {
                config.varysMasterUrl = (String) jsonObject.get("masterUrl");
            }
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}