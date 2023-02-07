package cs2030.simulator;

import java.util.Comparator;


public  class ServerComparator implements  Comparator<Server> {
    public int compare(Server a, Server b) {
        if (Double.compare(a.getAvail(), b.getAvail()) == 0) {
            return Integer.compare(a.getID(), b.getID());
        }
        return Double.compare(a.getAvail(), b.getAvail());
    }
}