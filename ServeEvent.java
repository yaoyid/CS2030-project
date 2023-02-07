package cs2030.simulator;

import cs2030.util.Pair;
import cs2030.util.Lazy;
import java.util.Optional;


class ServeEvent extends Event {
    private final Server server;
    //private final double servetime;


    ServeEvent(Customer customer, double time, Server server, double servetime) {
        super(customer, time);
        this.server = server;
        //this.servetime = customer.getServiceTime().get();
    }

    ServeEvent(Customer customer, double time, Server server) {
        super(customer, time);
        this.server = server;
        //this.servetime = customer.getServiceTime().get();
    }
    
    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server serverFromShop = shop.getList()
                                .filter(x -> x.getID() == this.server.getID()).get(0);
        Server server = this.getServer();
        int serverindex = shop.getServerIndex(server);
        if (serverFromShop.isHuman()) {
            Server updateServer = new Server(server.getID(),this.getTime() + this.getServetime(),
                                    serverFromShop.waitQueue()
                                .filter(x -> x.getID() != this.getCustomer().getID()), 
                                serverFromShop.getRestSupplier());

            return Pair.<Optional<Event>, Shop>of(
                Optional.<Event>of(new DoneEvent(this.getCustomer(), 
                this.getTime() + this.getServetime(),
                updateServer)), 
                new Shop(shop.getList().set(serverindex, updateServer), shop.getQ()));
        } else {
            shop = shop.removeQ(this.getCustomer());
            shop = shop.updateAll();
            serverFromShop = shop.getList().get(serverindex);
            Server updateServer = serverFromShop.serve(this.getTime() + this.getServetime());

            return Pair.<Optional<Event>, Shop>of(
                Optional.<Event>of(new DoneEvent(this.getCustomer(), 
                this.getTime() + this.getServetime(),
                updateServer)), 
                new Shop(shop.getList().set(serverindex, updateServer), shop.getQ()));
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

    public boolean isServed() {
        return true;
    }
    
    public boolean isDone() {
        return false;
    }

    public boolean isLeft() {
        return false;
    }

    public boolean isPending() {
        return false;
    }

    public boolean isWait() {
        return false;
    }
    
    public double waitingTime() {
        return this.getTime() - this.getCustomer().getTime();
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getCustomer()
            .toString() + " serves by " + this.server.toString();
    }
}