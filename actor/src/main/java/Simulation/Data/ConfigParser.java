package Simulation.Data;

// import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
// import java.util.Iterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {

    public static ArrayList<SimTask> parseTaskFile(String filename) throws IOException, ParseException {
        FileInputStream fis = null;
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        int count = 0;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // Read number of racks and number of jobs in the trace
        try {
            String line = br.readLine();
            String[] chunks = line.split("\\s+");

            count = Integer.parseInt(chunks[0]);
        } catch (IOException e) {
            System.err.println("Unknown Format " + filename);
            System.exit(1);
        }

        for (int j = 0; j < count; j++) {
            try {
                String line = br.readLine();
                String[] chunks = line.split("\\s+");
                int lIndex = 0;

                SimTask task = new SimTask();
                Reducer reducerItr = new Reducer();

                task.coflowId = chunks[lIndex++];
                task.startTime = Integer.parseInt(chunks[lIndex++]);

                task.mapperCount = Integer.parseInt(chunks[lIndex++]);
                for (int i = 0; i < task.mapperCount; i++) {
                    task.mappers.add(Integer.parseInt(chunks[lIndex++]));
                }

                task.reducerCount = Integer.parseInt(chunks[lIndex++]);
                for (int i = 0; i < task.reducerCount; i++) {
                    String reducer = chunks[lIndex++];
                    reducerItr.reducerId = Integer.parseInt(reducer.split(":")[0]) + 1;
                    reducerItr.size = Double.parseDouble(reducer.split(":")[1]) * 1048576.0;
                    task.reducers.add(reducerItr);
                }

                tasks.add(task);
            } catch (IOException e) {
                System.err.println("Unknown format in " + filename);
            }
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