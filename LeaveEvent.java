package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;


class LeaveEvent extends Event {
    //private final double servetime;

    LeaveEvent(Customer customer, double time) {
        super(customer, time);
        //this.servetime = servetime;
    }
    
    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        return Pair.<Optional<Event>, Shop>of(
            Optional.<Event>empty(), shop);
    }

    public Server getServer() {
        return new Server(0);
    }

    public boolean isValid() {
        return true;
    }

    public double getServetime() {
        return 0;
    }

    public boolean isDone() {
        return false;
    }

    public boolean isServed() {
        return false;
    }
    
    public boolean isLeft() {
        return true;
    }

    public boolean isWait() {
        return false;
    }
    
    public boolean isPending() {
        return false;
    }
    
    public double waitingTime() {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getCustomer()
            .toString() + " leaves";
    }
}