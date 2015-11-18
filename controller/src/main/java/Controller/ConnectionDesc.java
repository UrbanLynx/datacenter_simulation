package Controller;

import java.net.Socket;
import java.io.*;

/**
 * Created by ruby on 11/17/15.
 */
public class ConnectionDesc {

    SlaveDesc description;
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    ConnectionDesc(SlaveDesc description,
                   Socket socket,
                   ObjectOutputStream oos, ObjectInputStream ois) {
        this.description = description;
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

}
