package cs2030.simulator;

import cs2030.util.Lazy;

public class Customer {
    private final int id;
    private final double time;
    private final Lazy<Double> servetime;

    public Customer(int id, double time, Lazy<Double> servetime) {
        this.id = id;
        this.time = time;
        this.servetime = servetime;
    }
    
    Customer(int id, double time) {
        this.id = id;
        this.time = time;
        this.servetime = new Lazy<Double>(() -> 1.0);
    }

    public int getID() {
        return this.id;
    }
    
    public double getTime() {
        return this.time;
    }

    public Lazy<Double> getServiceTime() {
        return this.servetime;
    }

    @Override
    public String toString() {
        return String.format("%s", this.id);
    }
}