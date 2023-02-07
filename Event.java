package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;

public abstract class Event {
    private final Customer customer;
    private final double time;

    Event(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
    }

    public int compareTo(Event b) {
        if (Double.compare(this.time, b.time) == 0) {
            return Integer.compare(this.getCustomer().getID(), b.getCustomer().getID());
        }
        return Double.compare(this.time, b.time);
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public double getTime() {
        return this.time;
    }

    abstract boolean isServed();

    abstract boolean isLeft();

    abstract boolean isWait();

    abstract boolean isDone();

    abstract boolean isPending();

    abstract boolean isValid();

    abstract double waitingTime();

    abstract Server getServer();

    abstract Pair<Optional<Event>, Shop> execute(Shop shop);

    abstract double getServetime();

    @Override
    public String toString() {
        return String.format("%.3f", this.getTime());
    }
}

