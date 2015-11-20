package Simulation.Master.Utils;

import Simulation.Master.Utils.SlaveDesc;

import java.net.Socket;
import java.io.*;

/**
 * Created by ruby on 11/17/15.
 */
public class ConnectionDesc {

    public SlaveDesc description;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public ConnectionDesc(SlaveDesc description,
                          Socket socket,
                          ObjectOutputStream oos, ObjectInputStream ois) {
        this.description = description;
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

}
