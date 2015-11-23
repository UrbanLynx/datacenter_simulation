package Simulation.Data;

// import org.json.simple.JSONArray;
import Simulation.Master.Utils.SlaveDesc;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// import java.util.Iterator;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {


    public static ArrayList<SimTask> parseTaskFile(SimulationConfig config) throws IOException, ParseException {
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

     //   if()
        // TODO: Temporary hardcoded, remove later
        SimTask task = new SimTask();
        task.simulationType = config.simulationType;

        task.coflowId = "1";
        task.startTime = 0;

        task.mappers = new ArrayList<Integer>();
        task.mappers.add(1);

        task.reducers = new HashMap<Integer, Reducer>();

        Reducer reducerItr = new Reducer();
        reducerItr.address = "localhost";
        reducerItr.port = 7000;
        reducerItr.reducerId = 1;
        reducerItr.size = (long) (10 * 1048576.0);

        task.reducers.put(1, reducerItr);
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
                    reducerItr.reducerId = Integer.parseInt(reducer.split(":")[0]) + 2;
                    reducerItr.size = (long) (Double.parseDouble(reducer.split(":")[1]) * 1048576.0);
                    reducerItr.port = (long) (Double.parseDouble(reducer.split(":")[2]);
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
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open " + filename);
            System.exit(1);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // Read number of racks and number of jobs in the trace
        String line = br.readLine();
        String[] chunks = line.split("\\s+");
        count = Integer.parseInt(chunks[0]);

        config.hosts = new ArrayList<SlaveDesc>();
        for (int i = 1; i <= count; i++) {
            line = br.readLine();
            chunks = line.split("\\s+");
            config.hosts.add(new SlaveDesc(i,chunks[0],config.slavePort));
        }
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
            config.simulationType = SimulationType.valueOf((String) jsonObject.get("simulationType"));
            config.slavePort = ((Long) jsonObject.get("slavePort")).intValue();
            config.varysMasterUrl = (String) jsonObject.get("masterUrl");


            parseHostsFile(config);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
