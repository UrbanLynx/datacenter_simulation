package Simulation.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sreejith Unnikrishnan on 28/11/15.
 */
public class SimLogger {

    private int numHosts;
    private static boolean initialized = false;
    private static ArrayList<String> hostAddresses;
    private static Logger simLoggers[];

    public SimLogger(String hostsFilePath) throws FileNotFoundException {

        if (!initialized) {
            System.out.println("Initializing loggers");
            init(hostsFilePath);
            initialized = true;
        }

    }

    public void init(String hostsFilePath) {

        /*  This function goes through hosts file, creates file handlers
            and Loggers for each host */

        FileInputStream file = null;

        try {
            file = new FileInputStream(hostsFilePath);
        } catch (IOException e) {
            System.err.println("Couldn't open " + hostsFilePath);
            System.exit(1);
        }

        BufferedReader bf = new BufferedReader(new InputStreamReader(file));

        // Get the number of hosts
        try {
            numHosts = Integer.parseInt(bf.readLine());
        } catch (IOException e) {
            System.err.println("Error while parsing num of hosts field in host file");
            e.printStackTrace();
            System.exit(1);
        }

        // Instantiate logger and hostAddress lists
        hostAddresses = new ArrayList<String>(numHosts);
        simLoggers = new Logger[numHosts];

        // Get the host IP`s
        for (int i=0; i<numHosts; ++i) {
            try {
                hostAddresses.add(i, bf.readLine());
            } catch (IOException e) {
                System.err.println("Error while parsing host addresses in host file");
                e.printStackTrace();
                System.exit(1);
            }
        }

        // Add file handlers based on IP to corresponding simLogger
        String filePrefix = "Log_";
        for (int i=0; i<hostAddresses.size(); ++i) {
            try {
                simLoggers[i].addHandler(new FileHandler(filePrefix + hostAddresses.get(i)));
            } catch (IOException e) {
                System.err.println("Error while creating file handler for logger");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    public void log(Level level, String hostAddress, String logMessage) {

        // Check if host address is present. If so log the message to corresponding logger
        boolean logged = false;
        for (int i=0; i<hostAddresses.size(); ++i) {
            if (hostAddresses.get(i).equals(hostAddress)) {
                // Add the log to i th Logger
                simLoggers[i].log(level, logMessage);
                logged = true;
                break;
            }
        }

        // If the host file is not found, something`s wrong. Exit
        if (!logged) {
            System.err.println("Could not find a matching hostAddress");
            System.exit(1);
        }

    }

}
