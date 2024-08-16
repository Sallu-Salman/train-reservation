package entity;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    public int pnr;
    public List<Passenger> passengers;
    public float amount;

    static int idGenerator = 1;

    public Ticket() {
        this.pnr = idGenerator++;
        passengers = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket\n-------\n");
        sb.append("PNR number: ").append(pnr);
        sb.append("\n\nPassengers\n\n");
        for(Passenger passenger: passengers) {
            sb.append(passenger);
            sb.append("\n");
        }

        return sb.toString();
    }
}
