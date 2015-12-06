package Simulation.Logger;

import Simulation.Communicators.Utils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sreejith Unnikrishnan on 28/11/15.
 */
public class SimLogger {

    class LogWraper{
        public LogLevel level;
        public DateTime time;
        public String logMessage;
        public LogWraper(LogLevel level, String logMessage){
            this.level = level;
            this.logMessage = logMessage;
            this.time = new DateTime();
        }

        //public String toString() { return level + "\t" + logMessage; }
        public String toString() { return logMessage; }
    }

    public enum LogLevel{
        ANALYS,
        COMPLETE,
        INFO,
        ERROR
    }

    private String logFolder = "logs";
    private Map<LogLevel, FileWriter> logFile = new HashMap<LogLevel, FileWriter>();
    private BlockingQueue<LogWraper> logsQueue = new LinkedBlockingQueue<LogWraper>();
    private final CountDownLatch latch = new CountDownLatch(1);

    private Boolean outputFile;
    private Boolean outputConsole;

    public SimLogger(String filename, Boolean outFile, Boolean outConsole) throws FileNotFoundException {
        outputFile = outFile;
        outputConsole = outConsole;
        init(filename);
        printToFile();
    }

    public void init(String filename) {
        //DateTimeFormatter dtf = DateTimeFormat.forPattern("_MM-dd-yyyy_HH:mm:ss");
        //String timestamp = new DateTime().toString(dtf);
        filename += "data.log";

        try {
            File theFile = new File(logFolder);
            theFile.mkdirs();

            for (LogLevel level: LogLevel.values()){
                logFile.put(level, new FileWriter(logFolder + "/" + level.toString() + "_" + filename));
            }

        } catch (IOException e) {
            System.err.println("Couldn't open " + filename);
            System.exit(1);
        }

    }

    public void log(LogLevel level, String logMessage) {
        LogWraper wraper = new LogWraper(level, logMessage);
        try {
            logsQueue.put(wraper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printToFile(){
        Thread clientThread = new Thread(){
            @Override
            public void run(){
                while (latch.getCount() == 1){
                    try {
                        LogWraper wraper = logsQueue.take();
                        if (outputConsole){
                            Utils.safePrintln(wraper.logMessage);
                        }
                        if (outputFile){
                            writeToFile(wraper);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                closeFile();
            }
        };

        clientThread.start();

    }

    public void stopLogger(){
        latch.countDown();
        log(LogLevel.INFO, "logger exit");
        //System.out.println("Finish work");
    }

    private void closeFile() {
        try {
            for (FileWriter writer: logFile.values()){
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(LogWraper wraper){
        try {
            logFile.get(wraper.level).write(wraper.toString());
            logFile.get(wraper.level).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
