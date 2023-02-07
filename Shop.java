package cs2030.simulator;


import cs2030.util.ImList;
import java.util.List;

public class Shop {
    private final ImList<Server> servers;
    private final ImList<Customer> selfCheckoutQ;

    Shop(List<Server> servers) {
        this.servers = ImList.<Server>of(servers);
        this.selfCheckoutQ = ImList.<Customer>of();
    }

    Shop(ImList<Server> servers) {
        this.servers = servers;
        this.selfCheckoutQ = ImList.<Customer>of();
    }

    Shop(ImList<Server> servers, ImList<Customer> selfCheckoutQ) {
        this.servers = servers;
        this.selfCheckoutQ = selfCheckoutQ;
    }

    public ImList<Server> getList() {
        return this.servers;
    }

    public ImList<Customer> getQ() {
        return this.selfCheckoutQ;
    }

    public int getServerIndex(Server server) {
        int counter = -1;
        for (int i = 0; i < this.getList().size(); i++) {
            if (this.servers.get(i).getID() == server.getID()) {
                return i;
            }
        }
        return counter;
    }

    Shop update(Server server, Server newserver) {
        ImList<Server> newList = this.getList().filter(x -> x.getID() != server.getID());
        return new Shop(newList.add(newserver));
    }

    Shop newCustomerInQ(Customer customer) {
        return new Shop(this.servers, this.selfCheckoutQ.add(customer));
    }

    Shop removeQ(Customer customer) {
        ImList<Customer> newQ = this.selfCheckoutQ.filter(x -> x.getID() != customer.getID());
        return new Shop(this.servers, newQ);
    }

    Shop updateAll() {
        ImList<Server> list = this.servers;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isHuman()) {
                Server server = list.get(i);
                list = list.set(i, server.updateQ(this.selfCheckoutQ));
            }
        }
        return new Shop(list, this.selfCheckoutQ);
    }

    public String printServers() {
        String string = this.servers.toString();
        for (int i = 0; i < this.servers.size(); i++) {
            string = string + this.servers.get(i).getID() + " " + this.servers.get(i).getAvail()
                     + " " +
                    this.servers.get(i).waitQueue() + " " + '\n';
        }
        return string;
    }


    @Override
    public String toString() {
        return this.servers.toString();
    }
}

