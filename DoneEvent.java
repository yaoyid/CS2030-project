package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;

class DoneEvent extends Event {
    private final Server server;
    //private final double servetime;

    DoneEvent(Customer customer, double time, Server server) {
        super(customer, time);
        this.server = server;
        //this.servetime = servetime;
    }
    
    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {

        Server serverFromShop = shop.getList().filter(x -> x.getID() == this.server.getID())
                                .get(0);

        int serverindex = shop.getServerIndex(serverFromShop);
        if (server.isHuman()) {    
            Server updateServer = new Server(serverFromShop.getID(),serverFromShop.getAvail() +
                                    serverFromShop.rest(),
                                    serverFromShop.waitQueue(), 
                                    serverFromShop.getRestSupplier());

            return Pair.<Optional<Event>, Shop>of(
                Optional.<Event>empty(), 
                new Shop(shop.getList().set(serverindex, updateServer), shop.getQ()));
        } else {
            return Pair.<Optional<Event>, Shop>of(
                Optional.<Event>empty(), 
                shop);
        }

    }

    public Server getServer() {
        return this.server;
    }

    public double getServetime() {
        return 0;
    }

    public boolean isServed() {
        return false;
    }
    
    public boolean isLeft() {
        return false;
    }

    public boolean isWait() {
        return false;
    }
    
    public boolean isValid() {
        return true;
    }

    public boolean isDone() {
        return true;
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
            .toString() + " done serving by " + this.server.toString();
    }
}