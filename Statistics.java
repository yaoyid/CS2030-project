package cs2030.simulator;

public class Statistics {
    private final int numServed;
    private final int numLeft;
    private final double totalWaitingTime;

    Statistics() {
        this.numServed = 0;
        this.numLeft = 0;
        this.totalWaitingTime = 0;
    }

    Statistics(int numServed, int numLeft, double totalWaitingTime) {
        this.numServed = numServed;
        this.numLeft = numLeft;
        this.totalWaitingTime = totalWaitingTime;
    }

    Statistics addServed(Event a) {
        if (a.isServed()) {
            return new Statistics(this.numServed + 1, this.numLeft, this.totalWaitingTime);
        }
        return this;
    }

    Statistics addLeft(Event a) {
        if (a.isLeft()) {
            return new Statistics(this.numServed, this.numLeft + 1, this.totalWaitingTime);
        }
        return this;  
    }

    Statistics addTime(Event a) {
        if (a.isServed()) {
            return new Statistics(this.numServed, this.numLeft,
                    this.totalWaitingTime + a.waitingTime());
        }
        return this; 
        
    }

    Statistics update(Event a) {
        Statistics updateStatistics = this;
        updateStatistics = updateStatistics.addLeft(a);
        updateStatistics = updateStatistics.addServed(a);
        updateStatistics = updateStatistics.addTime(a);
        return updateStatistics;
    }
    
    public double averageWaitingTime() {
        if (this.numServed == 0) {
            return 0;
        } else {
            return this.totalWaitingTime / this.numServed;
        }
    }

    @Override
    public String toString() {
        return String.format("[%.3f %d %d]", this.averageWaitingTime(),
            this.numServed, 
            this.numLeft);
    }
}