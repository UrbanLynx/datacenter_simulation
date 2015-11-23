package Simulation.Data;

// import org.json.simple.JSONArray;
import Simulation.Master.Utils.SlaveDesc;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// import java.util.Iterator;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {

    public static ArrayList<SimTask> parseTaskFile(String filename) throws IOException, ParseException {
        FileInputStream fis = null;
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        //int count = 1;
        /*
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
        */
        // TODO: Temporary hardcoded, remove later
        SimTask task = new SimTask();
        Reducer reducerItr = new Reducer();
        task.simulationType = SimulationType.valueOf("VARYS");
        task.srcAddress = "localhost";
        task.dstAddress = "localhost";
        task.srcPort = 6000;
        task.dstPort = 7000;
        task.coflowId = "1";
        task.startTime = 0;
        task.mapperCount = 1;
        task.mappers = new ArrayList<Integer>(task.mapperCount);
        task.mappers.add(1);
        task.reducerCount = 1;
        task.reducers = new ArrayList<Reducer>(task.reducerCount);
        reducerItr.reducerId = 1;
        reducerItr.size = (long) (10 * 1048576.0);
        task.reducers.add(reducerItr);
        tasks.add(task);

        /*
        for (int j = 0; j < count; j++) {
            try {
                String line = br.readLine();
                String[] chunks = line.split("\\s+");
                int lIndex = 0;

                SimTask task = new SimTask();
                Reducer reducerItr = new Reducer();

                task.simulationType = SimulationType.valueOf("VARYS");

                // TODO: below src and dst info needs to be moved out of SimTask
                task.srcAddress = "127.0.0.1";
                task.dstAddress = "127.0.0.1";
                task.srcPort = 6000;
                task.dstPort = 7000;

                task.coflowId = chunks[lIndex++];
                task.startTime = Integer.parseInt(chunks[lIndex++]);

                task.mapperCount = Integer.parseInt(chunks[lIndex++]);
                task.mappers = new ArrayList<Integer>(task.mapperCount);
                for (int i = 0; i < task.mapperCount; i++) {
                    task.mappers.add(Integer.parseInt(chunks[lIndex++]));
                }

                task.reducerCount = Integer.parseInt(chunks[lIndex++]);
                task.reducers = new ArrayList<Reducer>(task.reducerCount);
                for (int i = 0; i < task.reducerCount; i++) {
                    String reducer = chunks[lIndex++];
                    reducerItr.reducerId = Integer.parseInt(reducer.split(":")[0]) + 1;
                    reducerItr.size = (long) (Double.parseDouble(reducer.split(":")[1]) * 1048576.0);
                    task.reducers.add(reducerItr);
                }

                tasks.add(task);
            } catch (IOException e) {
                System.err.println("Unknown format in " + filename);
            }

        }*/
        return tasks;
    }

    public static void parseHostsFile(SimulationConfig config) throws IOException {
        String filename = config.hostFile;
        int count = 0;

        // TODO: Temporary hardcoded, remove later
        config.hosts = new ArrayList<SlaveDesc>();
        config.hosts.add(new SlaveDesc(1, "localhost", config.slavePort));

        /*
        try {
            FileInputStream fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // Read number of racks and number of jobs in the trace
        String line = br.readLine();
        String[] chunks = line.split("\\s+");
        count = Integer.parseInt(chunks[0]);

        config.hosts = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            line = br.readLine();
            chunks = line.split("\\s+");
            config.hosts.add(chunks[0]);
        }
        */
    }

    public static SimulationConfig parseSimConfigFile(String filename){
        try {
            FileReader reader = new FileReader(filename);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            SimulationConfig config = new SimulationConfig();
            config.taskFileName = (String) jsonObject.get("taskFile");
            config.hostFile = (String) jsonObject.get("hostsFile");
            //config.simulationStartTime = new DateTime((String) jsonObject.get("StartTime"), DateTimeZone.UTC);
            config.isVarys = (Boolean) jsonObject.get("isVarys");
            config.slavePort = ((Long) jsonObject.get("slavePort")).intValue();
            if (config.isVarys) {
                config.varysMasterUrl = (String) jsonObject.get("masterUrl");
            }

            parseHostsFile(config);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
