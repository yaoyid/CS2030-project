package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.Pair;
import java.util.Optional;
import java.util.List;

public class EventStub extends Event {
    //private final Customer customer;
    //private final double time;

    EventStub(Customer customer, double time) {
        super(customer,time);
    }

    public Server getServer() {
        return new Server(0);
    }

    public double getServetime() {
        return 0.0;
    }

    public boolean isServed() {
        return false;
    }

    public boolean isDone() {
        return false;
    }
    
    public boolean isLeft() {
        return false;
    }

    public boolean isWait() {
        return false;
    }

    public boolean isPending() {
        return false;
    }
    
    public boolean isValid() {
        return false;
    }
    
    public double waitingTime() {
        return 0;
    }

    /*public int compareTo(EventStub b) {
        return Double.compare(this.time, b.time);
    }*/

    /*protected Customer getCustomer() {
        return this.customer;
    }

    protected double getTime() {
        return this.time;
    }*/

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.<Optional<Event>, Shop>of(Optional.empty(), shop);
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.getTime());
    }

}