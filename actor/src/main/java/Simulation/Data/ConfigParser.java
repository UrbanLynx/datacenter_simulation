package Simulation.Data;

// import org.json.simple.JSONArray;
import Simulation.Master.Utils.SlaveDesc;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// import java.util.Iterator;

/**
 * Created by Stanislav-macbook on 30.10.2015.
 */
public class ConfigParser {


    public static ArrayList<SimTask> parseTaskFile(SimulationConfig config) throws IOException, ParseException {
        FileInputStream fis = null;
        ArrayList<SimTask> tasks = new ArrayList<SimTask>();
        int count = 1;
        String filename = config.taskFileName;

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

        //int numberOfBytesInMegabyte = 1048576;
        for (int j = 0; j < count; j++) {
            try {
                String line = br.readLine();
                String[] chunks = line.split("\\s+");
                int lIndex = 0;

                SimTask task = new SimTask();
                task.simulationType = config.simulationType;

                task.reducers = new HashMap<Integer, Reducer>();

                // TODO: below src and dst info needs to be moved out of SimTask


                task.id = Integer.parseInt(chunks[lIndex++]);
                task.startTime = Integer.parseInt(chunks[lIndex++]);

                task.masterListenerIp = config.masterListenerIp;
                task.masterListenerPort = config.masterListenerPort;

                task.mapperCount = Integer.parseInt(chunks[lIndex++]);
                task.mappers = new ArrayList<Integer>();
                for (int i = 0; i < task.mapperCount; i++) {
                    task.mappers.add(Integer.parseInt(chunks[lIndex++]));
                }

                task.reducerCount = Integer.parseInt(chunks[lIndex++]);
                task.reducers = new HashMap<Integer, Reducer>();
                task.reducersArr = new ArrayList<Reducer>();
                for (int i = 0; i < task.reducerCount; i++) {
                    Reducer reducerItr = new Reducer();
                    String reducer = chunks[lIndex++];
                    reducerItr.reducerId = Integer.parseInt(reducer.split(":")[0]);
                    reducerItr.sizeKB = (int) (long) (Double.parseDouble(reducer.split(":")[1]));

                    // TODO: change hosts to Map
                    reducerItr.address = config.hosts.get(reducerItr.reducerId).hostName;
                    reducerItr.port = (int) (Double.parseDouble(reducer.split(":")[2]));

                    reducerItr.doNotRegisterFlow = new ArrayList<Integer>();
                    if (reducer.split(":").length > 3){
                        String mappersNotRegister = reducer.split(":")[3];
                        List<String> mappersArr = Arrays.asList(mappersNotRegister.split(","));
                        for (String mapp: mappersArr){
                            reducerItr.doNotRegisterFlow.add(Integer.parseInt(mapp));
                        }
                    }

                    task.reducers.put(reducerItr.reducerId, reducerItr);
                    task.reducersArr.add(reducerItr);
                }

                tasks.add(task);
            } catch (IOException e) {
                System.err.println("Unknown format in " + filename);
            }

        }
        return tasks;
    }

    public static void parseHostsFile(SimulationConfig config) throws IOException {
        String filename = config.hostFile;
        int count = 0;

        // TODO: Temporary hardcoded, remove later
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

        config.hosts = new HashMap<Integer, SlaveDesc>();
        for (int i = 1; i <= count; i++) {
            line = br.readLine();
            chunks = line.split("\\s+");
            config.hosts.put(i, new SlaveDesc(i,chunks[0],config.slavePort));
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

            config.masterListenerIp = (String) jsonObject.get("masterListenerIp");
            config.masterListenerPort = (int) (long) (Long) jsonObject.get("masterListenerPort");

            /* TODO: use in case of big objects > 1 MB
            config.doGenerateFiles = (Boolean) jsonObject.get("doGenerateFiles");
            config.fileDirectory = (String) jsonObject.get("fileDirectory");
            config.fileSizeKBMax = (int) (long) (Long) jsonObject.get("fileSizeKBMax");
            config.fileSizeKBMin = (int) (long) (Long)  jsonObject.get("fileSizeKBMin");
            config.fileSizeKBStep = (int) (long) (Long)  jsonObject.get("fileSizeKBStep");*/
            parseHostsFile(config);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
