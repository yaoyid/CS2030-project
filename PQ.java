package cs2030.util;

import java.util.PriorityQueue;
import java.util.Comparator;

public class PQ<T> {
    //private final Comparator<? super T> cmp;
    private final PriorityQueue<T> queue;

    public PQ(Comparator<? super T> cmp) {
        //this.cmp = cmp;
        this.queue = new PriorityQueue<T>(cmp);
    }

    public PQ(PriorityQueue<T> queue) {
        //this.cmp = cmp;
        this.queue = queue;
    }

    public PQ<T> add(T elem) {
        PriorityQueue<T> newqueue = new PriorityQueue<T>(this.queue);
        PQ<T> newQueue = new PQ<T>(newqueue);
        newQueue.queue.add(elem);
        return new PQ<T>(newQueue.queue);
    }

    public boolean isEmpty() {
        return (this.queue.size() == 0);
    }

    public Pair<T, PQ<T>> poll() {
        PriorityQueue<T> newqueue = new PriorityQueue<T>(this.queue);
        PQ<T> newQueue = new PQ<T>(newqueue);
        T first = newQueue.queue.poll();
        Pair<T, PQ<T>> x = Pair.<T, PQ<T>>of(first, newQueue);
        return x;
    }

    @Override
    public String toString() {
        return this.queue.toString();
    }
} 