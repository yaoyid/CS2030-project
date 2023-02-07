package cs2030.simulator;

import cs2030.util.ImList;
import cs2030.util.PQ;
import cs2030.util.Pair;
import java.util.List;

public class Simulate2 {
    private final Shop shop;
    private final PQ<EventStub> queue;

    public Simulate2(int numOfServers, List<Double> queue) {
        //this.queue = new PQ<EventStub>(new EventComparator(), makeEvents(queue));
        this.queue = makeQueue(queue);
        this.shop = makeshop(numOfServers);
    }

    public String run() {
        String string = String.format("%s\n", this.queue.poll().first());
        Pair<EventStub, PQ<EventStub>> sorted = this.queue.poll();
        while (!sorted.second().isEmpty()) {
            sorted = sorted.second().poll();
            string += String.format("%s\n", sorted.first());
        }
        string += "-- End of Simulation --";
        return string;
    }

    public static ImList<EventStub> makeEvents(List<Double> queue) {

        ImList<EventStub> events = ImList.<EventStub>of(List.of());
        for (int i = 0; i < queue.size(); i++) {
            events = events.add(new EventStub(new Customer(i + 1, queue.get(i)), queue.get(i)));
        }

        return events;
    }

    public static PQ<EventStub> makeQueue(List<Double> queue) {
        PQ<EventStub> pq = new PQ<EventStub>(new EventComparator());
        for (int i = 0; i < queue.size(); i++) {
            pq = pq.add(new EventStub(new Customer(i + 1, queue.get(i)), queue.get(i)));
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