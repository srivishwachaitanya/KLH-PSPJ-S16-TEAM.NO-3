import java.util.ArrayList;
import java.util.Scanner;

class Vehicle {

    String token;
    String name;
    String phone;
    String vehicleNumber;
    String vehicleType;
    int duration;
    int level;
    int slot;

    Vehicle(String token, String name, String phone,
            String vehicleNumber, String vehicleType,
            int duration, int level, int slot) {

        this.token = token;
        this.name = name;
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.duration = duration;
        this.level = level;
        this.slot = slot;
    }

    void displayDetails() {

        System.out.println("\n----- Vehicle Details -----");
        System.out.println("Token Number  : " + token);
        System.out.println("User Name     : " + name);
        System.out.println("Phone Number  : " + phone);
        System.out.println("Vehicle Number: " + vehicleNumber);
        System.out.println("Vehicle Type  : " + vehicleType);
        System.out.println("Parking Time  : " + duration + " hours");
        System.out.println("Level         : " + level);
        System.out.println("Parking Slot  : " + slot);
    }
}


public class MultiLevelParkingSystem2 {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Vehicle> vehicles = new ArrayList<>();

    static int tokenNumber = 1001;

    // 2-wheeler levels
    static boolean[] twoWheelerLevel1 = new boolean[6];
    static boolean[] twoWheelerLevel2 = new boolean[6];

    // 4-wheeler levels
    static boolean[] fourWheelerLevel3 = new boolean[6];
    static boolean[] fourWheelerLevel4 = new boolean[6];


    // ---------------- WELCOME SCREEN ----------------

    static void welcomeScreen() {

        System.out.println("\n======================================");
        System.out.println("      MULTI-LEVEL PARKING SYSTEM");
        System.out.println("======================================");
        System.out.println("          Welcome User!");
        System.out.println("======================================");
    }


    // ---------------- HOME DASHBOARD ----------------

    static void dashboard() {

        System.out.println("\n======================================");
        System.out.println("           HOME DASHBOARD");
        System.out.println("======================================");

        System.out.println("1. Park Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Search Vehicle");
        System.out.println("4. View User Details");
        System.out.println("5. View Available Slots");
        System.out.println("6. Exit");

        System.out.print("\nEnter your choice: ");
    }


    // ---------------- PARK VEHICLE ----------------

    static void parkVehicle() {

        System.out.println("\n========== PARK VEHICLE ==========");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        System.out.print("Enter vehicle number: ");
        String vehicleNumber = sc.nextLine();

        System.out.println("\nSelect Vehicle Type");
        System.out.println("1. Two Wheeler");
        System.out.println("2. Four Wheeler");

        System.out.print("Enter choice: ");
        int typeChoice = sc.nextInt();

        String vehicleType;

        if (typeChoice == 1) {
            vehicleType = "Two Wheeler";
        }
        else if (typeChoice == 2) {
            vehicleType = "Four Wheeler";
        }
        else {
            System.out.println("Invalid vehicle type!");
            return;
        }

        System.out.print("Enter parking duration (hours): ");
        int duration = sc.nextInt();

        if (duration <= 0) {
            System.out.println("Invalid duration!");
            return;
        }

        int level = -1;
        int slot = -1;


        // ------------- TWO WHEELER -------------

        if (vehicleType.equals("Two Wheeler")) {

            /*
             * Short duration vehicles are placed
             * closer to the exit.
             *
             * Slot 1 is closest to exit.
             */

            if (duration <= 2) {

                // Level 1 - closest level
                slot = findSlot(twoWheelerLevel1);

                if (slot != -1) {
                    level = 1;
                    twoWheelerLevel1[slot] = true;
                }
            }

            else {

                // First try Level 2 for longer duration
                slot = findSlot(twoWheelerLevel2);

                if (slot != -1) {
                    level = 2;
                    twoWheelerLevel2[slot] = true;
                }

                else {

                    slot = findSlot(twoWheelerLevel1);

                    if (slot != -1) {
                        level = 1;
                        twoWheelerLevel1[slot] = true;
                    }
                }
            }
        }


        // ------------- FOUR WHEELER -------------

        else {

            if (duration <= 2) {

                // Level 3 is closer to exit
                slot = findSlot(fourWheelerLevel3);

                if (slot != -1) {
                    level = 3;
                    fourWheelerLevel3[slot] = true;
                }
            }

            else {

                // Level 4 is farther from exit
                slot = findSlot(fourWheelerLevel4);

                if (slot != -1) {
                    level = 4;
                    fourWheelerLevel4[slot] = true;
                }

                else {

                    slot = findSlot(fourWheelerLevel3);

                    if (slot != -1) {
                        level = 3;
                        fourWheelerLevel3[slot] = true;
                    }
                }
            }
        }


        // No slot available

        if (slot == -1) {

            System.out.println("\nSorry! No parking slot available.");
            return;
        }


        // Create token

        String token = "P" + tokenNumber;
        tokenNumber++;


        Vehicle vehicle = new Vehicle(
                token,
                name,
                phone,
                vehicleNumber,
                vehicleType,
                duration,
                level,
                slot + 1
        );

        vehicles.add(vehicle);


        // -------- TOKEN --------

        System.out.println("\n======================================");
        System.out.println("             PARKING TOKEN");
        System.out.println("======================================");

        System.out.println("Token Number   : " + token);
        System.out.println("Name           : " + name);
        System.out.println("Vehicle Number : " + vehicleNumber);
        System.out.println("Vehicle Type   : " + vehicleType);
        System.out.println("Duration       : " + duration + " hours");
        System.out.println("Parking Level  : " + level);
        System.out.println("Parking Slot   : " + (slot + 1));

        System.out.println("--------------------------------------");
        System.out.println("Please remember your token number.");
        System.out.println("======================================");
    }


    // ---------------- FIND EMPTY SLOT ----------------

    static int findSlot(boolean[] slots) {

        for (int i = 0; i < slots.length; i++) {

            if (!slots[i]) {
                return i;
            }
        }

        return -1;
    }


    // ---------------- REMOVE VEHICLE ----------------

    static void removeVehicle() {

        System.out.println("\n========== REMOVE VEHICLE ==========");

        System.out.print("Enter token number: ");
        String token = sc.nextLine();

        Vehicle found = null;

        for (Vehicle v : vehicles) {

            if (v.token.equalsIgnoreCase(token)) {
                found = v;
                break;
            }
        }


        if (found == null) {

            System.out.println("Vehicle not found!");
            return;
        }


        // Calculate parking fee

        int rate;

        if (found.vehicleType.equals("Two Wheeler")) {
            rate = 20;
        }
        else {
            rate = 40;
        }

        int fee = found.duration * rate;


        // -------- BILL --------

        System.out.println("\n======================================");
        System.out.println("             PARKING BILL");
        System.out.println("======================================");

        System.out.println("Token Number   : " + found.token);
        System.out.println("User Name      : " + found.name);
        System.out.println("Vehicle Number : " + found.vehicleNumber);
        System.out.println("Vehicle Type   : " + found.vehicleType);
        System.out.println("Parking Level  : " + found.level);
        System.out.println("Parking Slot   : " + found.slot);
        System.out.println("Duration       : " + found.duration + " hours");
        System.out.println("Rate           : Rs." + rate + " per hour");
        System.out.println("--------------------------------------");
        System.out.println("Total Amount   : Rs." + fee);
        System.out.println("======================================");


        System.out.print("Enter payment amount: ");
        int payment = sc.nextInt();

        if (payment < fee) {

            System.out.println("Insufficient payment!");
            return;
        }

        System.out.println("Payment successful.");

        if (payment > fee) {
            System.out.println("Change          : Rs." + (payment - fee));
        }


        // Free the parking slot

        freeSlot(found);


        // Remove vehicle from list

        vehicles.remove(found);

        System.out.println("\nVehicle removed successfully.");
    }


    // ---------------- FREE SLOT ----------------

    static void freeSlot(Vehicle vehicle) {

        int level = vehicle.level;
        int slot = vehicle.slot - 1;

        if (level == 1) {
            twoWheelerLevel1[slot] = false;
        }

        else if (level == 2) {
            twoWheelerLevel2[slot] = false;
        }

        else if (level == 3) {
            fourWheelerLevel3[slot] = false;
        }

        else if (level == 4) {
            fourWheelerLevel4[slot] = false;
        }
    }


    // ---------------- SEARCH VEHICLE ----------------

    static void searchVehicle() {

        System.out.println("\n========== SEARCH VEHICLE ==========");

        System.out.print("Enter token number: ");
        String token = sc.nextLine();

        for (Vehicle v : vehicles) {

            if (v.token.equalsIgnoreCase(token)) {

                v.displayDetails();
                return;
            }
        }

        System.out.println("Vehicle not found!");
    }


    // ---------------- VIEW USER DETAILS ----------------

    static void viewUserDetails() {

        System.out.println("\n========== USER DETAILS ==========");

        if (vehicles.size() == 0) {

            System.out.println("No vehicles are currently parked.");
            return;
        }

        for (Vehicle v : vehicles) {

            v.displayDetails();

            System.out.println("--------------------------------");
        }
    }


    // ---------------- AVAILABLE SLOTS ----------------

    static void viewAvailableSlots() {

        System.out.println("\n======================================");
        System.out.println("        AVAILABLE PARKING SLOTS");
        System.out.println("======================================");


        System.out.println("\nLevel 1 - Two Wheeler");
        displaySlots(twoWheelerLevel1);


        System.out.println("\nLevel 2 - Two Wheeler");
        displaySlots(twoWheelerLevel2);


        System.out.println("\nLevel 3 - Four Wheeler");
        displaySlots(fourWheelerLevel3);


        System.out.println("\nLevel 4 - Four Wheeler");
        displaySlots(fourWheelerLevel4);
    }


    // ---------------- DISPLAY SLOTS ----------------

    static void displaySlots(boolean[] slots) {

        for (int i = 0; i < slots.length; i++) {

            if (slots[i]) {
                System.out.print("[X] ");
            }
            else {
                System.out.print("[" + (i + 1) + "] ");
            }
        }

        System.out.println();
    }


    // ---------------- MAIN METHOD ----------------

    public static void main(String[] args) {

        welcomeScreen();

        int choice;

        do {

            dashboard();

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    parkVehicle();
                    break;

                case 2:
                    removeVehicle();
                    break;

                case 3:
                    searchVehicle();
                    break;

                case 4:
                    viewUserDetails();
                    break;

                case 5:
                    viewAvailableSlots();
                    break;

                case 6:
                    System.out.println("\nThank you for using Parking Management System!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }
}