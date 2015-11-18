package Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Created by ruby on 11/17/15.
 */
public class SimpleSender {

    public static void main(String[] args) {

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        /*
        int numbytes;
        if ( args.length == 3 ) {
            numbytes = Integer.parseInt(args[2]);
        } else {
            numbytes = 100;
        }
        */

        Socket sock;
        ObjectOutputStream out;
        ObjectInputStream in;

        //byte[] bytes = new byte[numbytes];
        /*
        byte[] bytes = new byte[100];
        new Random().nextBytes(bytes);
        */

        int x = 4;
        try {
            sock = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            //out.write(bytes);
            out.writeInt(x);
            out.flush();
            System.out.println("wrote the integer " + x);
            out.close();
            in.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
