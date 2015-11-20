package Simulation.Master.Utils;

/**
 * Created by ruby on 11/17/15.
 */
public class SlaveDesc {

    public String hostName;
    public int portNumber;

    public SlaveDesc(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public String toString() {
        return ( super.toString() + " " + hostName + ":" + portNumber );
    }

}
