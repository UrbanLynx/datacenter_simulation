package Controller;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ruby on 11/17/15.
 */

/**
 * BasicController does not invoke Varys scheduler.
 * Just implement simulation Controller parsing a simulation file and
 * sending messages at specified times to slave processes
 * that will be running on Mininet hosts.
 */
public class BasicController {

    ArrayList<SlaveEntry> slaves;
    ArrayList<SimEntry> simEvents;

    public BasicController() {
        // needs to connect to all slaves

        //
        simEvents = new ArrayList<SimEntry>();
    }

    // TODO: implement actual functionality, pass in File, or filename. For now hard coded.
    // takes a simulation file and resturns a list of SimEvents
    // return an error if a host not in its list of slaves is in simulation file
    public ArrayList<SimEntry> parseSimFile() {
        simEvents.add(new SimEntry("10.0.0.1", "10.0.0.3", 10, 1000));      // send from host 10.0.0.1 to host 10.0.0.3 at t=10 ms, 1000 bytes
        simEvents.add(new SimEntry("10.0.0.3", "10.0.0.1", 2000, 1000));
    }

    public void executeSimulation() {

    }

    public static void main(String[] args) {

        Controller controller = new Controller(args[0]);

        ArrayList<SimEntry> simEvents = controller.parseSimFile();

        Iterator<SimEntry> it = simEvents.iterator();
        while ( it.hasNext() ) {
            SimEntry event = it.next();
            // wait until appropriate time, then
            // create messages and send to each host

        }

    }

}
