package cs2030.simulator;

import cs2030.util.ImList;
import java.util.function.Supplier;


public class Server {
    private final int id;
    private final ImList<Customer> wait;
    private final double nextAvail;
    private final Supplier<Double> rest;
    //private final boolean hasWaiting;
    
    public Server(int id) {
        this.id = id;
        this.nextAvail = 0;
        //this.hasWaiting = false;
        this.wait = ImList.<Customer>of();
        this.rest = () -> 0.0;
    }

    public Server(int id, Supplier<Double> rest) {
        this.id = id;
        this.nextAvail = 0;
        //this.hasWaiting = false;
        this.wait = ImList.<Customer>of();
        this.rest = rest;
    }


    public Server(int id, double nextAvail, ImList<Customer> wait) {
        this.id = id;
        this.nextAvail = nextAvail;
        this.wait = ImList.<Customer>of(wait);
        this.rest = () -> 0.0;
    }

    public Server(int id, double nextAvail, ImList<Customer> wait, Supplier<Double> rest) {
        this.id = id;
        this.nextAvail = nextAvail;
        this.wait = ImList.<Customer>of(wait);
        this.rest = rest;
    }

    public int getID() {
        return this.id;
    }

    public double getAvail() {
        return this.nextAvail;
    }
    

    public Server serve(double time) {
        return new Server(this.id, time, this.wait);
    }


    public Server newWait(Customer newWait) {
        return new Server(this.id, this.nextAvail, this.wait.add(newWait));
    }
        
    public Server updateQ(ImList<Customer> wait) {
        return this;
    }

    public Supplier<Double> getRestSupplier() {
        return this.rest;
    }

    public ImList<Customer> waitQueue() {
        return this.wait;
    }

    public Server remove(Customer customer) {
        return new Server(this.id, this.nextAvail,
                this.wait.remove(this.wait.indexOf(customer)).second());
    }
    
    public int waitLength() {
        return this.wait.size();
    }

    public double rest() {
        return this.rest.get();
    }

    public boolean isHuman() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s", this.id);
    }
}