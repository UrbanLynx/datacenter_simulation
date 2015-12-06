package Simulation.Communicators;

import Simulation.Data.Reducer;
import Simulation.Data.SimTask;
import Simulation.Logger.SimLogger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by stanislavmushits on 23/11/15.
 */
public class Utils {

    public static SimLogger logger;

    public static void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    public static Socket connectTo(String host, int port, int timeout) {
        Socket socket = null;
        boolean scanning=true;
        boolean printedError = false;
        while(scanning)
        {
            try
            {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port));
                scanning=false;
            }
            catch(Exception e) {
                if (!printedError){
                    Utils.logger.log(SimLogger.LogLevel.ERROR, "Connect to "+host+":"+port+" failed, waiting and trying again\n");
                    printedError = true;
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        if (printedError){
            Utils.logger.log(SimLogger.LogLevel.ERROR, "Connect to "+host+":"+port+" success\n");
        }
        return socket;
    }

    public static void reportFinishToMaster(SimTask task){
        try{
            Socket socket = Utils.connectTo(task.masterListenerIp, task.masterListenerPort, 1000);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(task);
            output.close();
            socket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void wait(int millisec){
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getData(long size){
        return new String("a");
    }
}
