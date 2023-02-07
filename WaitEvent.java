package cs2030.simulator;

import cs2030.util.Pair;
import java.util.Optional;


class WaitEvent extends Event {
    private final Server server;
    

    WaitEvent(Customer customer, double time, Server server) {
        super(customer, time);
        this.server = server;
        
    }
    
    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server serverFromShop = shop.getList().filter(x -> x.getID() == this.server.getID())
                                .get(0);
        int serverindex = shop.getServerIndex(serverFromShop);
        if (serverFromShop.isHuman()) {
            if (serverFromShop.waitQueue().get(0).getID() == this.getCustomer().getID()) {
                return Pair.<Optional<Event>, Shop>of(
                    Optional.<Event>of(new ServeEvent(this.getCustomer(),
                    serverFromShop.getAvail(), serverFromShop)),
                    new Shop(shop.getList().set(serverindex, serverFromShop), shop.getQ()));
            } else {
                return Pair.<Optional<Event>, Shop>of(
                    Optional.<Event>of(new PendingEvent(this.getCustomer(),
                    serverFromShop.getAvail(), serverFromShop)),
                    shop);
            }
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
        return this.getCustomer().getServiceTime().get();
    }

    public boolean isValid() {
        return true;
    }

    public boolean isDone() {
        return false;
    }

    public boolean isServed() {
        return false;
    }
    
    public boolean isLeft() {
        return false;
    }

    public boolean isPending() {
        return false;
    }

    public boolean isWait() {
        return true;
    }
    
    public double waitingTime() {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getCustomer()
            .toString() + " waits at " + this.server.toString();
    }
}