package entity;

import constants.Berth;

public class Passenger {
    public int id;
    public String name;
    public int age;
    public Berth preferredBerth;
    public Seat allotedSeat;

    static int idGenerator = 1;

    public Passenger() {
        this.id = idGenerator++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Passenger ID: ").append(id);
        sb.append("\n");
        sb.append("Passenger name: ").append(name);
        sb.append("\n");
        sb.append("Passenger age: ").append(age);
        sb.append("\n");

        if(age < 5) {
            return sb.toString();
        }

        sb.append("Status: ").append((allotedSeat == null? "WL" : allotedSeat instanceof SleeperSeat? "CNF" : "RAC"));
        sb.append("\n");
        if(allotedSeat != null) {
            sb.append("Seat number: ").append(allotedSeat.id);
            sb.append("\n");
            if(allotedSeat instanceof SleeperSeat) {
                sb.append("Berth: ").append(allotedSeat.berth);
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
