package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.PQ;
import cs2030.util.Pair;
import java.util.List;
import java.util.Optional;

public class Simulate3 {
    private final Shop shop;
    private final PQ<Event> queue;

    public Simulate3(int numOfServers, List<Double> queue) {
        //this.queue = new PQ<EventStub>(new EventComparator(), makeEvents(queue));
        this.queue = makeQueue(queue);
        this.shop = makeshop(numOfServers);
    }

    public String run() { 
        //PQ<Event> pq = this.queue;
        Shop shops = this.shop;
        //Shop shops = test.getShop()
        Pair<Event, PQ<Event>> sorted = this.queue.poll();
        //Pair<Event, PQ<Event>> sorted = test.getQ().poll();
        String string = String.format("%s\n", sorted.first());
        while (!sorted.second().isEmpty()) {    
            //get next for the first valid event in PQ
            Optional<Event> next = sorted.first().execute(shops).first();
            //get the rest of the PQ
            PQ<Event> newPQ = sorted.second();
            shops = sorted.first().execute(shops).second();
            //System.out.println(shops.printServers());
            
            //if next is not null--> then add back to the queue
            if (next.orElse(new EventStub(new Customer(0, 0.0), 0.0)).getTime() != 0.0) {
                Event nextEvent = next.orElse(new EventStub(new Customer(0, 0.0), 0.0));
                newPQ = sorted.second().add(nextEvent);

            }
            
            //new next gotten using poll()
            sorted = newPQ.poll();
            
            //new shop
            //shops = sorted.first().execute(shops).second();

            //add next event from the PQ
            string += String.format("%s\n", sorted.first());
                    
        }
        if (sorted.first().execute(shops).first()
            .orElse(new EventStub(new Customer(0, 0.0), 0.0)).getTime() != 0.0) {
            string += String.format("%s\n", sorted.first().execute(shops)
                    .first().orElse(new EventStub(new Customer(0, 0.0), 0.0)));
        }
        
        string += "-- End of Simulation --";
        return string;
    } 

    public PQ<Event> getQ() {
        return this.queue;
    }

    public Shop getShop() {
        return this.shop;
    }

    public static PQ<Event> makeQueue(List<Double> queue) {
        PQ<Event> pq = new PQ<Event>(new EventComparator());
        for (int i = 0; i < queue.size(); i++) {
            pq = pq.add(new ArriveEvent(new Customer(i + 1, queue.get(i)), queue.get(i)));
        }
        return pq;
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