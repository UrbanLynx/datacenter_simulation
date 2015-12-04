package Simulation.Master;

import Simulation.Communicators.Utils;
import Simulation.Communicators.VarysRegistrator;
import Simulation.Data.SimTask;
import Simulation.Logger.SimLogger;
import Simulation.Master.Utils.TaskProgress;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

/**
 * Created by stanislavmushits on 02/12/15.
 */
public class TaskListener {
    private Map<Integer, TaskProgress> taskProgresses = new HashMap<Integer, TaskProgress>();
    private final CountDownLatch latch = new CountDownLatch(1);
    private final CountDownLatch finishLatch = new CountDownLatch(1);

    public void addTask(SimTask task){
        task.systemStartTime = System.currentTimeMillis();
        TaskProgress progress = new TaskProgress(task);
        taskProgresses.put(task.id, progress);
    }
    public void addVarysCoflow(SimTask task, VarysRegistrator registrator){
        taskProgresses.get(task.id).registrator = registrator;
    }

    public void listenForSlaves(final int port){
        Thread clientThread = new Thread(){
            @Override
            public void run(){
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Utils.safePrintln("Master listening for reports on port"+port);
                    while (latch.getCount() == 1 || taskProgresses.size() > 0){
                        Socket socket = serverSocket.accept();


                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        SimTask task = (SimTask) inputStream.readObject();
                        inputStream.close();
                        socket.close();

                        Utils.safePrintln("Master got report for task " +task.id + " from " + task.currentSlaveTaskIndex);
                        TaskProgress currentTask = taskProgresses.get(task.id);
                        currentTask.removeReducer(task);
                        checkFinishedTasks(currentTask);
                    }
                    serverSocket.close();
                    finishLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        clientThread.start();

    }

    public void noMoreTasks(){
        latch.countDown();
    }

    public void waitForFinish(){
        try {
            Utils.safePrintln("Master waits for finishing");
            noMoreTasks();
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkFinishedTasks(TaskProgress currentTask) {
        if (currentTask.isFinished()){
            long currentTime = System.currentTimeMillis();
            Utils.logger.log(SimLogger.LogLevel.COMPLETE, currentTask.task.id + " " +
                    (currentTime - currentTask.task.systemStartTime) + "\n");
            taskProgresses.remove(currentTask.task.id);

        }
    }

}
