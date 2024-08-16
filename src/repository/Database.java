package repository;

import constants.AvailabilityCount;
import constants.Berth;
import entity.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Database {
    public static Queue<Passenger> waitingList = new LinkedList<>();
    public static List<Cabin> train = new ArrayList<>();

    public static void addCabin() {
        train.add(new Cabin());
    }

    public static long getUpperBerthAvailability() {
        long count = 0;
        for(Cabin cabin: train) {
            count += cabin
                        .sleeperSeats
                        .stream()
                        .filter(seat -> seat.berth.equals(Berth.UPPER_BERTH) && seat.passenger == null)
                        .count();
        }
        return count;
    }

    public static long getLowerBerthAvailability() {
        long count = 0;
        for(Cabin cabin: train) {
            count += cabin
                    .sleeperSeats
                    .stream()
                    .filter(seat -> seat.berth.equals(Berth.LOWER_BERTH) && seat.passenger == null)
                    .count();
        }
        return count;
    }

    public static long getMiddleBerthAvailability() {
        long count = 0;
        for(Cabin cabin: train) {
            count += cabin
                    .sleeperSeats
                    .stream()
                    .filter(seat -> seat.berth.equals(Berth.MIDDLE_BERTH) && seat.passenger == null)
                    .count();
        }
        return count;
    }

    public static long getSideUpperBerthAvailability() {
        long count = 0;
        for(Cabin cabin: train) {
            count += cabin
                    .sleeperSeats
                    .stream()
                    .filter(seat -> seat.berth.equals(Berth.SIDE_UPPER_BERTH) && seat.passenger == null)
                    .count();
        }
        return count;
    }

    public static long getRACAvailability() {
        long count = 0;
        for(Cabin cabin: train) {
            count += cabin
                    .RACSeats
                    .stream()
                    .filter(seat -> seat.passenger == null)
                    .count();
        }
        return count;
    }

    public static long getWaitingListAvailability() {
        return AvailabilityCount.TOTAL_WAITING_LIST - waitingList.size();
    }

    public static long getTotalAvailability() {
        long count = 0;
        for(Cabin cabin: train) {
            count += cabin
                    .sleeperSeats
                    .stream()
                    .filter(seat -> seat.passenger == null)
                    .count();
            count += cabin
                    .RACSeats
                    .stream()
                    .filter(seat -> seat.passenger == null)
                    .count();
        }
        return count + getWaitingListAvailability();
    }
}
