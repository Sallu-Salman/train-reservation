package helper;

import constants.AvailabilityCount;
import constants.Berth;
import entity.RACSeat;
import entity.SleeperSeat;
import repository.Database;

import java.util.Scanner;

public class Utilities {
    static Scanner scan = new Scanner(System.in);

    public static void driverCode() {
        int cabinCount = 4;
        AvailabilityCount.TOTAL_WAITING_LIST = 1;
        for(int i = 0; i < cabinCount; i++) {
            Database.addCabin();
        }
    }

    public static int readInt() {
        return Integer.parseInt(scan.nextLine());
    }

    public static String readStr() {
        return scan.nextLine();
    }

    public static void halt() {
        System.out.println("Press Enter to continue...");
        scan.nextLine();
    }
}
