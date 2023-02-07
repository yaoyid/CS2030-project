package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.PQ;
import cs2030.util.Pair;
import cs2030.util.Lazy;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


public class Simulate8 {
    private final Shop shop;
    //private final List<Pair<Double, Supplier<Double>>> arriveList;
    private final PQ<Event> queue;
    //private final Statistics stats;

    public Simulate8(int numOfServers, int numOfSelfChecks, 
                    List<Pair<Double, Supplier<Double>>> arriveList,
                    int qMax, Supplier<Double> rest) {
        //this.queue = new PQ<EventStub>(new EventComparator(), makeEvents(queue));
        this.queue = makeEvents(arriveList, qMax);
        this.shop = makeshop(numOfServers, numOfSelfChecks, rest);
        //this.stats = new Statistics();
    }

    public String run() { 
        //System.out.println();
        Statistics record = new Statistics();
        
        Shop shops = this.shop;
        //System.out.println(shops.printServers());
        //Shop shops = sim.getShop()
        PQ<Event> pq = this.queue;
        Pair<Event, PQ<Event>> sorted = pq.poll();
        //Pair<Event, PQ<Event>> sorted = sim.getQ().poll();
        String string = String.format("%s\n", sorted.first());
        record = record.update(sorted.first());
        

        while (!sorted.second().isEmpty()) {  
            //get the rest of the PQ
            PQ<Event> newPQ = sorted.second();
            Pair<Optional<Event>, Shop> nextMove = sorted.first().execute(shops);
            //get next for the first valid event in PQ
            Optional<Event> next = nextMove.first();
            
            shops = nextMove.second();
           
            if (sorted.first().isDone()) {
                //done server before adding the time

                Server old = sorted.first().getServer();
                Server serverEvent = shops.getList()
                                        .filter(x -> x.getID() == old.getID()).get(0);
                int serverindex = shops.getServerIndex(serverEvent);    
                    
                PQ<Event> counter = new PQ<Event>(new EventComparator());
                PQ<Event> copy = newPQ;
                //get updated server from shop
                if (old.isHuman()) {    
                    while (!copy.isEmpty()) {
                        Event addNext = copy.poll().first();
                        //if serve event by the same server--> update
                        if (addNext.isServed() && 
                            addNext.getServer().getID() == serverEvent.getID()) {
                            Event update = new ServeEvent(addNext.getCustomer(), 
                                            serverEvent.getAvail(), serverEvent);
                            counter = counter.add(update);
                        } else {
                            counter = counter.add(addNext);
                        }
                        copy = copy.poll().second();
                    }
                } else {
                    if (serverEvent.waitLength() > 0) {
                        Customer id = serverEvent.waitQueue().get(0);
                        Event addNext = new ServeEvent(id, serverEvent.getAvail(), serverEvent);
                        counter = counter.add(addNext);
                        while (!copy.isEmpty()) {
                            Event move = copy.poll().first();
                            if (move.getCustomer().getID() != addNext.getCustomer().getID()) {
                                counter = counter.add(move);
                            }
                            copy = copy.poll().second();
                        }
                    } else {
                        counter = newPQ;
                    }
                }
                
                newPQ = counter;
            }
            //if next is not null--> then add back to the queue
            if (next.orElse(new EventStub(new Customer(0, 0.0), 0.0)).isValid()) {
                Event nextEvent = next.orElse(new EventStub(new Customer(0, 0.0),
                                0.0));
                // serverEvent = nextEvent.getServer();
                
                newPQ = sorted.second().add(nextEvent);

            } 
            
            //new next gotten using poll()
            sorted = newPQ.poll();
            
            //new shop
            //shops = sorted.first().execute(shops).second();

            //add next event from the PQ
            if (!sorted.first().isPending()) {
                string += String.format("%s\n", sorted.first());
            }
            record = record.update(sorted.first());
                    
        }
        Event last = sorted.first();
        Pair<Optional<Event>, Shop> lastMove = last.execute(shops);
        //get next for the first valid event in PQ
        Optional<Event> lastN = lastMove.first();
        shops = lastMove.second();
        while (lastN
            .orElse(new EventStub(new Customer(0, 0.0), 0.0)).isValid()) {
            last = lastN
                    .orElse(new EventStub(new Customer(0, 0.0), 0.0));
            string += String.format("%s\n", last);
            record = record.update(lastN
                    .orElse(new EventStub(new Customer(0, 0.0), 0.0)));
            lastMove = last.execute(shops);
            //get next for the first valid event in PQ
            lastN = lastMove.first();
            shops = lastMove.second();
        }
        string += record.toString();
        return string;
    } 

    public static PQ<Event> makeEvents(List<Pair<Double, Supplier<Double>>> arriveList, int qMax) {
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 0; i < arriveList.size(); i++) {
            Lazy<Double> lazy = Lazy.<Double>of(arriveList.get(i).second());
            double arriveAt = arriveList.get(i).first();
            
            Customer newCustomer = new Customer(i + 1, arriveAt, lazy);
            //Event
            Event newArrive = new ArriveEvent(newCustomer, newCustomer.getTime(), qMax);
            pq = pq.add(newArrive);
        }
        return pq;
    } 

    

    public Shop getShop() {
        return this.shop;
    }

    public PQ<Event> getQ() {
        return this.queue;
    }

    public Statistics countStatistics(PQ<Event> leaveAndServe) {
        Statistics stats = new Statistics();
        while (!leaveAndServe.isEmpty()) {
            stats = stats.update(leaveAndServe.poll().first());
            leaveAndServe = leaveAndServe.poll().second();
        }
        return stats;
    }

    public static Shop makeshop(int numOfServers, int numOfSelfChecks, Supplier<Double> rest) {
        ImList<Server> servers = ImList.<Server>of(List.of());
        for (int i = 0; i < numOfServers; i++) {
            servers = servers.add(new Server(i + 1, rest));
        }

        for (int j = 0; j < numOfSelfChecks; j++) {
            servers = servers.add(new SelfCheckout(j + numOfServers + 1));
        }

        return new Shop(servers);
    }

    @Override
    public String toString() {
        return String.format("Queue : %s; Shop: %s", this.queue, this.shop);
    }
}