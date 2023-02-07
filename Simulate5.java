package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.PQ;
import cs2030.util.Pair;
import cs2030.util.Lazy;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Simulate5 {
    private final Shop shop;
    //private final List<Pair<Double, Supplier<Double>>> arriveList;
    private final PQ<Event> queue;
    //private final Statistics stats;

    public Simulate5(int numOfServers, List<Pair<Double, Supplier<Double>>> arriveList) {
        //this.queue = new PQ<EventStub>(new EventComparator(), makeEvents(queue));
        this.queue = makeEvents(arriveList);
        this.shop = makeshop(numOfServers);
        //this.stats = new Statistics();
    }

    public String run() { 
        Statistics record = new Statistics();
        Shop shops = this.shop;
        //Shop shops = sim.getShop()
        PQ<Event> pq = this.queue;
        Pair<Event, PQ<Event>> sorted = pq.poll();
        //Pair<Event, PQ<Event>> sorted = sim.getQ().poll();
        String string = String.format("%s\n", sorted.first());
        record = record.update(sorted.first());

        while (!sorted.second().isEmpty()) {    
            //get next for the first valid event in PQ
            Optional<Event> next = sorted.first().execute(shops).first();
            //get the rest of the PQ
            PQ<Event> newPQ = sorted.second();
            shops = sorted.first().execute(shops).second();
            //System.out.println(shops.printServers());
            
            //if next is not null--> then add back to the queue
            if (next.orElse(new EventStub(new Customer(0, 0.0), 0.0)).isValid()) {
                Event nextEvent = next.orElse(new EventStub(new Customer(0, 0.0), 0.0));
                newPQ = sorted.second().add(nextEvent);

            } 
            
            //new next gotten using poll()
            sorted = newPQ.poll();
            
            //new shop
            //shops = sorted.first().execute(shops).second();

            //add next event from the PQ
            string += String.format("%s\n", sorted.first());
            record = record.update(sorted.first());
                    
        }
        if (sorted.first().execute(shops).first()
            .orElse(new EventStub(new Customer(0, 0.0), 0.0)).isValid()) {
            string += String.format("%s\n", sorted.first().execute(shops)
                    .first().orElse(new EventStub(new Customer(0, 0.0), 0.0)));
            record = record.update(sorted.first().execute(shops)
                    .first().orElse(new EventStub(new Customer(0, 0.0), 0.0)));
        }
        string += record.toString();
        return string;
    } 

    public static PQ<Event> makeEvents(List<Pair<Double, Supplier<Double>>> arriveList) {
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 0; i < arriveList.size(); i++) {
            Lazy<Double> lazy = Lazy.<Double>of(arriveList.get(i).second());
            double arriveAt = arriveList.get(i).first();
            
            Customer newCustomer = new Customer(i + 1, arriveAt, lazy);
            //Event
            Event newArrive = new ArriveEvent(newCustomer, newCustomer.getTime());
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

    public static Shop makeshop(int numOfServers) {
        ImList<Server> servers = ImList.<Server>of(List.of());
        for (int i = 0; i < numOfServers; i++) {
            servers = servers.add(new Server(i + 1));
        }

        return new Shop(servers);
    }

    @Override
    public String toString() {
        return String.format("Queue : %s; Shop: %s", this.queue, this.shop);
    }
}