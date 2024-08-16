package dto;

import constants.Berth;
import entity.Seat;

public class PassengerDTO {
    public String name;
    public int age;
    public Berth preferredBerth;
    public Seat allotedSeat;
    public String gender;
    public Berth availableBerth;
}
