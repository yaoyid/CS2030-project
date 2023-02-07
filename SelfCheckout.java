package cs2030.simulator;

import cs2030.util.ImList;
import java.util.function.Supplier;

public class SelfCheckout extends Server {
    
    SelfCheckout(int id) {
        super(id, 0, ImList.<Customer>of());
    }

    SelfCheckout(int id, double time) {
        super(id, time, ImList.<Customer>of());
    }

    SelfCheckout(int id, double time, ImList<Customer> queue) {
        super(id, time, queue);
    }

    public SelfCheckout serve(double time) {
        return new SelfCheckout(this.getID(), time, this.waitQueue());
    }

    public SelfCheckout newWait(Customer newWait) {
        ImList<Customer> queue = this.waitQueue().add(newWait);
        return new SelfCheckout(this.getID(), this.getAvail(), queue);
    }

    public SelfCheckout remove(Customer customer) {
        ImList<Customer> queue = this.waitQueue().filter(x -> x.getID() != customer.getID());
        return new SelfCheckout(this.getID(), this.getAvail(), queue);
    }

    @Override
    public SelfCheckout updateQ(ImList<Customer> wait) {
        return new SelfCheckout(this.getID(), this.getAvail(), wait);
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getID());
    }
}