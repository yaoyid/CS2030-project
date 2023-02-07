package cs2030.simulator;


import cs2030.util.Pair;
import java.util.Optional;


class ArriveEvent extends Event {
    private final int qMax;


    ArriveEvent(Customer customer, double time) {
        super(customer, time);
        this.qMax = 1;
    }

    ArriveEvent(Customer customer, double time, int qMax) {
        super(customer, time);
        this.qMax = qMax;
    }
    
    public boolean isValid() {
        return true;
    }

    
    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        if (shop.getList().filter(x -> (x.waitLength() < this.qMax)).size() == 0) {
            //return new leave event
            return Pair.<Optional<Event>, Shop>of(
                Optional.<Event>of(new LeaveEvent(this.getCustomer(), this.getTime())), shop);
        } else if (shop.getList().filter(x -> (x.getAvail() <= this.getTime())).size() > 0) {
            //create serve event
            Server server = shop.getList().filter(x -> (x.getAvail() <= this.getTime()))
                            .get(0);
            int serverindex = shop.getServerIndex(server);
            return Pair.<Optional<Event>, Shop>of(
                    Optional.<Event>of(new ServeEvent(this.getCustomer(), this.getTime(), server)),
                    new Shop(shop.getList().set(serverindex, server)));
        } else {
            //create wait event
            Server server = shop.getList().filter(x -> (x.waitLength() < this.qMax)).get(0);
            int serverindex = shop.getServerIndex(server);
            if (server.isHuman()) {
                Server updateServer = new Server(server.getID(), server.getAvail(),
                                    server.waitQueue().add(this.getCustomer()), 
                                    server.getRestSupplier());
                            
                Shop updateShop = new Shop(shop.getList().set(serverindex, updateServer),
                                    shop.getQ());
                return Pair.<Optional<Event>, Shop>of(
                    Optional.<Event>of(new WaitEvent(this.getCustomer(), this.getTime(), server)),
                    updateShop);
            } else {
                shop = shop.newCustomerInQ(this.getCustomer());
                shop = shop.updateAll();
                Server counter = shop.getList().get(serverindex);
                return Pair.<Optional<Event>, Shop>of(
                    Optional.<Event>of(new WaitEvent(this.getCustomer(), this.getTime(), counter)),
                    shop);
            }
            
        }
    }

    public Server getServer() {
        return new Server(0);
    }

    public double getServetime() {
        return this.getCustomer().getServiceTime().get();
    }

    public boolean isServed() {
        return false;
    }

    public boolean isPending() {
        return false;
    }
    
    public boolean isLeft() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public boolean isWait() {
        return false;
    }
    
    public double waitingTime() {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getCustomer()
            .toString() + " arrives";
    }
}
