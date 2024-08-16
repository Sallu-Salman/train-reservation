package entity;

import constants.Berth;

public class Seat {
    public int id;
    public Berth berth;
    public Passenger passenger;

    static int idGenerator = 1;

    public Seat() {
        this.id = idGenerator++;
    }

    public Seat(Berth berth) {
        this();
        this.berth = berth;
    }
}
