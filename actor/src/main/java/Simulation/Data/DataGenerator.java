package Simulation.Data;

import java.io.PrintWriter;

/**
 * Created by stanislavmushits on 24/11/15.
 */
public class DataGenerator {

    public String unitObject;

    public void writeToFile(String filename, String object){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.println(object);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateUnitObject(int sizeBytes){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < sizeBytes; i++){
            builder.append('a');
        }
        unitObject = builder.toString();
    }

    public String generateObject(int sizeKBytes){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<sizeKBytes; i++){
            builder.append(unitObject);
        }
        return builder.toString();
    }

    public void generateFiles(SimulationConfig config){
        generateUnitObject(1024);
        int filesize = config.fileSizeKBMin;
        while (filesize < config.fileSizeKBMax){
            String filename = config.fileDirectory + "/" + filesize;
            String object =generateObject(filesize);
            writeToFile(filename,object);
            filesize += config.fileSizeKBStep;
        }
    }
}
