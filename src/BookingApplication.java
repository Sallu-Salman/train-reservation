import constants.Berth;
import dto.PassengerDTO;
import entity.Cabin;
import entity.Passenger;
import entity.Seat;
import entity.Ticket;
import repository.Database;

import java.util.*;

import static helper.Utilities.*;

public class BookingApplication {

    public static void main(String[] args) {
        driverCode();
        application: while(true) {
            System.out.println("Welcome to IRCTC\n");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Ticket");
            System.out.println("4. View Chart");
            System.out.println();
            System.out.print("Enter your choice: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    processTicketBooking();
                    break;
                case 2:
                    // cancel ticket
                    break;
                case 3:
                    //view ticket
                    break;
                case 4:
                    viewChart();
                    break;
                default:
                    break application;
            }
        }
    }

    private static void viewChart() {
        System.out.println();
        System.out.println("Chart\n=====\n");
        for(Cabin cabin: Database.train) {
            System.out.println(cabin);
        }
    }

    private static void processTicketBooking() {
        System.out.println("Ticket Booking\n");
        System.out.print("Enter adult passengers count (age > 5) : ");
        int passengerCount = readInt();

        if(!isBookingPossibleForCount(passengerCount)) {
            System.out.println("Booking cancelled!\nPassengers count exceeded available tickets!\n");
            return;
        }

        List<PassengerDTO> passengers = new ArrayList<>();
        for(int i = 0; i < passengerCount; i++) {
            PassengerDTO passenger = new PassengerDTO();
            passengers.add(passenger);
            System.out.println("Passenger " + (i+1));
            System.out.print("Name: ");
            passenger.name = readStr();
            System.out.print("Age: ");
            passenger.age = readInt();
            if(passenger.age < 5) {
                System.out.println("Passenger with age less than 5 will not be charged!");
                i--;
                continue;
            }

            System.out.print("Gender: ");
            passenger.gender = readStr();

            System.out.print("Preference [U/M/L/S/NIL]: ");
            passenger.preferredBerth = getBerthForShorthand(readStr());

            if(!checkBerthAvailability(passenger.preferredBerth)) {
                System.out.println("Preferred seat not available!");
                System.out.print("Enter 'Y' to continue booking or 'N' to cancel: ");
                String continueBooking = readStr();

                if(!continueBooking.equals("Y")) {
                    System.out.println("Booking Cancelled!");
                    return;
                }

                passenger.availableBerth = Berth.GENERAL_BERTH;
            }
            else {
                passenger.availableBerth = passenger.preferredBerth;
            }
        }

        preprocessBerthPreference(passengers);
        Ticket ticket = confirmBooking(passengers);
        System.out.println("\nBooking successful!\n");
        System.out.println(ticket);
    }

    private static Ticket confirmBooking(List<PassengerDTO> passengers) {
        Map<Berth, Queue<PassengerDTO>> berthMap = new HashMap<>();
        Ticket ticket = new Ticket();

        for(PassengerDTO pas: passengers) {
            if(pas.age < 5) {
                ticket.passengers.add(createPassengerForPassengerDTO(pas));
                continue;
            }

            if(!berthMap.containsKey(pas.availableBerth)) {
                berthMap.put(pas.availableBerth, new LinkedList<>());
            }

            berthMap.get(pas.availableBerth).offer(pas);
        }

        int startCabin = getNearbySeats(berthMap);

        bookCNFseats(berthMap, ticket, startCabin);
        if(berthMap.isEmpty()) {
            return ticket;
        }

        Queue<PassengerDTO> notConfirmedPassengers = new LinkedList<>();
        berthMap.values().forEach(notConfirmedPassengers::addAll);

        bookRACseats(notConfirmedPassengers, ticket);
        if(notConfirmedPassengers.isEmpty()) {
            return ticket;
        }

        appendPassengersToWaitingList(notConfirmedPassengers, ticket);
        return ticket;
    }

    private static int getNearbySeats(Map<Berth, Queue<PassengerDTO>> berthMap) {
        Map<Berth, Integer> countMap = new HashMap<>();

        for(Berth berth: berthMap.keySet()) {
            countMap.put(berth, berthMap.get(berth).size());
        }

        //sliding window technique

        int minNearby = Integer.MAX_VALUE;
        int startCabin = 0;

        int i = 0, j = 0, n = Database.train.size();

        while(j < n) {
            adjustCountMap(countMap, j, -1);

            while(isCountMapEmpty(countMap) && i <= j) {
                if(minNearby > (j-i+1)) {
                    startCabin = i;
                    minNearby = j - i + 1;
                }
                adjustCountMap(countMap, i, 1);
                i++;
            }
            j++;
        }

        return startCabin;
    }

    private static void adjustCountMap(Map<Berth, Integer> countMap, int cabinIndex, int sign) {
        for(Seat seat: Database.train.get(cabinIndex).sleeperSeats) {
            if(seat.passenger == null) {
                if(countMap.containsKey(seat.berth)) {
                    countMap.put(seat.berth, countMap.get(seat.berth) + sign);
                }
                else if(countMap.containsKey(Berth.GENERAL_BERTH)) {
                    countMap.put(Berth.GENERAL_BERTH, countMap.get(Berth.GENERAL_BERTH) + sign);
                }
            }
        }
    }

    private static boolean isCountMapEmpty(Map<Berth, Integer> countMap) {
        for(Berth berth: countMap.keySet()) {
            if(countMap.get(berth) > 0) {
                return false;
            }
        }

        return true;
    }


    private static void appendPassengersToWaitingList(Queue<PassengerDTO> notConfirmedPassengers, Ticket ticket) {
        while (!notConfirmedPassengers.isEmpty()) {
            Passenger passenger = createPassengerForPassengerDTO(notConfirmedPassengers.poll());
            Database.waitingList.offer(passenger);
            ticket.passengers.add(passenger);
        }
    }

    private static void bookRACseats(Queue<PassengerDTO> passengerDTOQueue, Ticket ticket) {
        for (Cabin cabin: Database.train) {
            for (Seat seat: cabin.RACSeats) {
                if(seat.passenger == null) {
                    Passenger passenger = createPassengerForPassengerDTO(passengerDTOQueue.poll());
                    allotSeatForPassenger(passenger, seat);
                    ticket.passengers.add(passenger);

                    if(passengerDTOQueue.isEmpty()) {
                        return;
                    }
                }
            }
        }
    }

    private static void bookCNFseats(Map<Berth, Queue<PassengerDTO>> berthMap, Ticket ticket, int startCabin) {
        for(int i = startCabin; i < Database.train.size(); i++) {
            Cabin cabin = Database.train.get(i);
            cabin.sleeperSeats.forEach(seat -> {
                if(seat.passenger == null) {
                    if(berthMap.containsKey(seat.berth)) {
                        Passenger passenger = createPassengerForPassengerDTO(Objects.requireNonNull(berthMap.get(seat.berth).poll()));
                        if(berthMap.get(seat.berth).isEmpty()) {
                            berthMap.remove(seat.berth);
                        }
                        allotSeatForPassenger(passenger, seat);
                        ticket.passengers.add(passenger);
                    }
                    else if(berthMap.containsKey(Berth.GENERAL_BERTH) && !berthMap.get(Berth.GENERAL_BERTH).isEmpty()){
                        Passenger passenger = createPassengerForPassengerDTO(Objects.requireNonNull(berthMap.get(Berth.GENERAL_BERTH).poll()));
                        if(berthMap.get(Berth.GENERAL_BERTH).isEmpty()) {
                            berthMap.remove(Berth.GENERAL_BERTH);
                        }
                        allotSeatForPassenger(passenger, seat);
                        ticket.passengers.add(passenger);
                    }
                }
            });
        }
    }

    private static void allotSeatForPassenger(Passenger passenger, Seat seat) {
        passenger.allotedSeat = seat;
        seat.passenger = passenger;
    }

    private static Passenger createPassengerForPassengerDTO(PassengerDTO passengerDTO) {
        Passenger passenger = new Passenger();
        passenger.name = passengerDTO.name;
        passenger.age = passengerDTO.age;
        passenger.preferredBerth = passengerDTO.preferredBerth;

        return passenger;
    }

    private static void preprocessBerthPreference(List<PassengerDTO> passengers) {
        long availableLowerBerths = Database.getLowerBerthAvailability();
        long childPassengers = passengers.stream().filter(p -> p.age < 5).count();

        for(PassengerDTO pas: passengers) {
            if(availableLowerBerths <= 0) {
                return;
            }
            if(pas.age >= 5 && pas.availableBerth.equals(Berth.GENERAL_BERTH)) {
                if(pas.gender.equals("female") && childPassengers > 0) {
                    pas.availableBerth = Berth.LOWER_BERTH;
                    childPassengers--;
                    availableLowerBerths--;
                }
                else if(pas.age > 60){
                    pas.availableBerth = Berth.LOWER_BERTH;
                    availableLowerBerths--;
                }
            }
        }

    }

    private static boolean checkBerthAvailability(Berth preferredBerth) {
        return switch (preferredBerth) {
            case UPPER_BERTH -> Database.getUpperBerthAvailability();
            case MIDDLE_BERTH -> Database.getMiddleBerthAvailability();
            case LOWER_BERTH -> Database.getLowerBerthAvailability();
            case SIDE_UPPER_BERTH -> Database.getSideUpperBerthAvailability();
            default -> 1;
        } > 0;
    }

    private static boolean isBookingPossibleForCount(int count) {
        return Database.getTotalAvailability() >= count;
    }

    private static Berth getBerthForShorthand(String str) {
        char letter = str.charAt(0);

        return switch (letter) {
            case 'U' -> Berth.UPPER_BERTH;
            case 'M' -> Berth.MIDDLE_BERTH;
            case 'L' -> Berth.LOWER_BERTH;
            case 'S' -> Berth.SIDE_UPPER_BERTH;
            default -> Berth.GENERAL_BERTH;
        };
    }

}




















