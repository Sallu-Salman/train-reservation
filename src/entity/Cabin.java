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
//        this.sleeperSeats.add(new SleeperSeat(Berth.UPPER_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.MIDDLE_BERTH));
//        this.sleeperSeats.add(new SleeperSeat(Berth.MIDDLE_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.LOWER_BERTH));
//        this.sleeperSeats.add(new SleeperSeat(Berth.LOWER_BERTH));
        this.sleeperSeats.add(new SleeperSeat(Berth.SIDE_UPPER_BERTH));

        this.RACSeats.add(new RACSeat(Berth.LOWER_BERTH));
        this.RACSeats.add(new RACSeat(Berth.LOWER_BERTH));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cabin-").append(id).append("\n");
        sb.append("--------\n");
        sb.append("Upper Berth - ").append(sleeperSeats.stream().filter(s -> s.berth.equals(Berth.UPPER_BERTH) && s.passenger == null).count()).append("\n");
        sb.append("Lower Berth - ").append(sleeperSeats.stream().filter(s -> s.berth.equals(Berth.LOWER_BERTH) && s.passenger == null).count()).append("\n");
        sb.append("Middle Berth - ").append(sleeperSeats.stream().filter(s -> s.berth.equals(Berth.MIDDLE_BERTH) && s.passenger == null).count()).append("\n");
        sb.append("SideUpper Berth - ").append(sleeperSeats.stream().filter(s -> s.berth.equals(Berth.SIDE_UPPER_BERTH) && s.passenger == null).count()).append("\n");
        sb.append("RAC seats - ").append(RACSeats.stream().filter(s -> s.passenger == null).count()).append("\n");
        sb.append("-".repeat(20)).append("\n");

        return sb.toString();
    }
}
