package entity;

import constants.Berth;

import java.util.ArrayList;
import java.util.List;

public class Cabin {
    public int id;
    public List<Seat> sleeperSeats;
    public List<Seat> RACSeats;

    private static int idGenerator = 1;

    public Cabin(){
        this.id = idGenerator++;
        sleeperSeats = new ArrayList<>();
        RACSeats = new ArrayList<>();

        this.sleeperSeats.add(new SleeperSeat(Berth.UPPER_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.UPPER_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.MIDDLE_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.MIDDLE_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.LOWER_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.LOWER_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.SIDE_UPPER_BERTH));

        this.RACSeats.add(new RACSeat(Berth.LOWER_BERTH));
        this.RACSeats.add(new RACSeat(Berth.LOWER_BERTH));
    }
}
