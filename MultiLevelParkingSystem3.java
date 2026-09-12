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

        System.out.println("\n----------------------------------------");
        System.out.println("           VEHICLE DETAILS");
        System.out.println("----------------------------------------");

        System.out.println("Token Number     : " + token);
        System.out.println("User Name        : " + name);
        System.out.println("Phone Number     : " + phone);
        System.out.println("Vehicle Number   : " + vehicleNumber);
        System.out.println("Vehicle Type     : " + vehicleType);
        System.out.println("Parking Duration : " + duration + " hours");
        System.out.println("Parking Level    : Level " + level);
        System.out.println("Parking Slot     : Slot " + slot);

        if (level == 1 || level == 3) {
            System.out.println("Location         : Near Exit Gate");
        } else {
            System.out.println("Location         : Far from Exit Gate");
        }

        System.out.println("----------------------------------------");
    }
}


public class MultiLevelParkingSystem3 {

    static Scanner sc = new Scanner(System.in);

    // Stores all currently parked vehicles
    static ArrayList<Vehicle> vehicles = new ArrayList<>();

    // Token number
    static int nextTokenNumber = 1001;


    /*
     * PARKING LEVELS
     *
     * Level 1 = Two Wheelers - Near Exit
     * Level 2 = Two Wheelers - Far from Exit
     * Level 3 = Four Wheelers - Near Exit
     * Level 4 = Four Wheelers - Far from Exit
     *
     * Each level contains 10 slots.
     *
     * false = Available
     * true  = Occupied
     */

    static boolean[] level1 = new boolean[10];
    static boolean[] level2 = new boolean[10];
    static boolean[] level3 = new boolean[10];
    static boolean[] level4 = new boolean[10];


    // ============================================================
    // WELCOME SCREEN
    // ============================================================

    static void welcomeScreen() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                                                      ");
        System.out.println("        MULTI-LEVEL PARKING MANAGEMENT SYSTEM         ");
        System.out.println("                                                      ");
        System.out.println("======================================================");
        System.out.println("                WELCOME USER!");
        System.out.println("======================================================");
    }


    // ============================================================
    // HOME DASHBOARD
    // ============================================================

    static void dashboard() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                  HOME DASHBOARD");
        System.out.println("======================================================");

        System.out.println("1. Park Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Search Vehicle");
        System.out.println("4. View User Details");
        System.out.println("5. View Available Parking Slots");
        System.out.println("6. View Parking Status");
        System.out.println("7. View Parking Statistics");
        System.out.println("8. Exit");

        System.out.println("======================================================");
        System.out.print("Enter your choice: ");
    }


    // ============================================================
    // PARK VEHICLE
    // ============================================================

    static void parkVehicle() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                    PARK VEHICLE");
        System.out.println("======================================================");


        // USER NAME
        System.out.print("Enter user name: ");
        String name = sc.nextLine();

        if (name.trim().isEmpty()) {

            System.out.println("Name cannot be empty.");
            return;
        }


        // PHONE NUMBER
        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        if (phone.trim().isEmpty()) {

            System.out.println("Phone number cannot be empty.");
            return;
        }


        // VEHICLE NUMBER
        System.out.print("Enter vehicle number: ");
        String vehicleNumber = sc.nextLine();

        if (vehicleNumber.trim().isEmpty()) {

            System.out.println("Vehicle number cannot be empty.");
            return;
        }


        // CHECK DUPLICATE VEHICLE
        for (Vehicle v : vehicles) {

            if (v.vehicleNumber.equalsIgnoreCase(vehicleNumber)) {

                System.out.println();
                System.out.println("This vehicle is already parked.");
                System.out.println("Existing Token: " + v.token);

                return;
            }
        }


        // VEHICLE TYPE
        System.out.println();
        System.out.println("Select Vehicle Type:");

        System.out.println("1. Two Wheeler");
        System.out.println("2. Four Wheeler");

        System.out.print("Enter choice: ");

        int vehicleChoice;

        try {

            vehicleChoice = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {

            System.out.println("Please enter a valid number.");
            return;
        }


        String vehicleType;


        if (vehicleChoice == 1) {

            vehicleType = "Two Wheeler";

        } else if (vehicleChoice == 2) {

            vehicleType = "Four Wheeler";

        } else {

            System.out.println("Invalid vehicle type.");
            return;
        }


        // PARKING DURATION
        System.out.print("Enter parking duration in hours: ");

        int duration;

        try {

            duration = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {

            System.out.println("Please enter a valid duration.");
            return;
        }


        if (duration <= 0) {

            System.out.println("Duration must be greater than 0.");
            return;
        }


        int level = -1;
        int slot = -1;


        // ========================================================
        // TWO WHEELER ALLOCATION
        // ========================================================

        if (vehicleType.equals("Two Wheeler")) {

            /*
             * Short parking:
             * 1 to 2 hours -> Level 1
             *
             * Long parking:
             * More than 2 hours -> Level 2
             */

            if (duration <= 2) {

                slot = findAvailableSlot(level1);

                if (slot != -1) {

                    level = 1;
                    level1[slot] = true;
                }

            } else {

                slot = findAvailableSlot(level2);

                if (slot != -1) {

                    level = 2;
                    level2[slot] = true;

                } else {

                    // If Level 2 is full,
                    // try Level 1.

                    slot = findAvailableSlot(level1);

                    if (slot != -1) {

                        level = 1;
                        level1[slot] = true;
                    }
                }
            }
        }


        // ========================================================
        // FOUR WHEELER ALLOCATION
        // ========================================================

        else {

            /*
             * Short parking:
             * 1 to 2 hours -> Level 3
             *
             * Long parking:
             * More than 2 hours -> Level 4
             */

            if (duration <= 2) {

                slot = findAvailableSlot(level3);

                if (slot != -1) {

                    level = 3;
                    level3[slot] = true;
                }

            } else {

                slot = findAvailableSlot(level4);

                if (slot != -1) {

                    level = 4;
                    level4[slot] = true;

                } else {

                    // If Level 4 is full,
                    // try Level 3.

                    slot = findAvailableSlot(level3);

                    if (slot != -1) {

                        level = 3;
                        level3[slot] = true;
                    }
                }
            }
        }


        // ========================================================
        // CHECK AVAILABILITY
        // ========================================================

        if (slot == -1) {

            System.out.println();
            System.out.println("======================================================");
            System.out.println("SORRY! NO PARKING SLOT IS AVAILABLE.");
            System.out.println("======================================================");

            return;
        }


        // ========================================================
        // GENERATE TOKEN
        // ========================================================

        String token = "P" + nextTokenNumber;

        nextTokenNumber++;


        // ========================================================
        // CREATE VEHICLE OBJECT
        // ========================================================

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


        // STORE VEHICLE
        vehicles.add(vehicle);


        // ========================================================
        // DISPLAY TOKEN
        // ========================================================

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                  PARKING TOKEN");
        System.out.println("======================================================");

        System.out.println("Token Number     : " + token);
        System.out.println("User Name        : " + name);
        System.out.println("Phone Number     : " + phone);
        System.out.println("Vehicle Number   : " + vehicleNumber);
        System.out.println("Vehicle Type     : " + vehicleType);
        System.out.println("Parking Duration : " + duration + " hours");

        System.out.println("------------------------------------------------------");

        System.out.println("Parking Level    : Level " + level);
        System.out.println("Parking Slot     : Slot " + (slot + 1));


        if (level == 1 || level == 3) {

            System.out.println("Location         : Near Exit Gate");

        } else {

            System.out.println("Location         : Far from Exit Gate");
        }


        System.out.println("------------------------------------------------------");

        System.out.println("Please keep your token number safe.");
        System.out.println("Token Number: " + token);

        System.out.println("======================================================");
    }


    // ============================================================
    // FIND AVAILABLE SLOT
    // ============================================================

    static int findAvailableSlot(boolean[] slots) {

        for (int i = 0; i < slots.length; i++) {

            if (!slots[i]) {

                return i;
            }
        }

        return -1;
    }


    // ============================================================
    // REMOVE VEHICLE
    // ============================================================

    static void removeVehicle() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                   REMOVE VEHICLE");
        System.out.println("======================================================");

        System.out.print("Enter token number: ");

        String token = sc.nextLine();


        Vehicle foundVehicle = null;


        // SEARCH BY TOKEN
        for (Vehicle v : vehicles) {

            if (v.token.equalsIgnoreCase(token)) {

                foundVehicle = v;
                break;
            }
        }


        // VEHICLE NOT FOUND
        if (foundVehicle == null) {

            System.out.println();
            System.out.println("Vehicle not found.");
            System.out.println("Please check the token number.");

            return;
        }


        // ========================================================
        // CALCULATE RATE
        // ========================================================

        int rate;


        if (foundVehicle.vehicleType.equals("Two Wheeler")) {

            rate = 20;

        } else {

            rate = 40;
        }


        // CALCULATE TOTAL
        int totalAmount =
                foundVehicle.duration * rate;


        // ========================================================
        // PARKING BILL
        // ========================================================

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                    PARKING BILL");
        System.out.println("======================================================");

        System.out.println("Token Number     : "
                + foundVehicle.token);

        System.out.println("User Name        : "
                + foundVehicle.name);

        System.out.println("Vehicle Number   : "
                + foundVehicle.vehicleNumber);

        System.out.println("Vehicle Type     : "
                + foundVehicle.vehicleType);

        System.out.println("Parking Level    : "
                + foundVehicle.level);

        System.out.println("Parking Slot     : "
                + foundVehicle.slot);

        System.out.println("Duration         : "
                + foundVehicle.duration
                + " hours");

        System.out.println("Rate             : Rs."
                + rate
                + " per hour");

        System.out.println("------------------------------------------------------");

        System.out.println("TOTAL AMOUNT     : Rs."
                + totalAmount);

        System.out.println("======================================================");


        // ========================================================
        // PAYMENT
        // ========================================================

        System.out.print("Enter payment amount: ");

        int payment;

        try {

            payment = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {

            System.out.println("Invalid payment amount.");
            return;
        }


        // INSUFFICIENT PAYMENT

        if (payment < totalAmount) {

            System.out.println();
            System.out.println("Insufficient payment.");
            System.out.println("Vehicle cannot be removed.");

            return;
        }


        int change = payment - totalAmount;


        // ========================================================
        // PAYMENT SUCCESS
        // ========================================================

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                 PAYMENT RECEIPT");
        System.out.println("======================================================");

        System.out.println("Parking Fee      : Rs." + totalAmount);
        System.out.println("Amount Paid      : Rs." + payment);
        System.out.println("Change           : Rs." + change);

        System.out.println("------------------------------------------------------");

        System.out.println("Payment Successful!");
        System.out.println("======================================================");


        // ========================================================
        // FREE PARKING SLOT
        // ========================================================

        freeParkingSlot(foundVehicle);


        // ========================================================
        // REMOVE VEHICLE FROM ARRAYLIST
        // ========================================================

        vehicles.remove(foundVehicle);


        System.out.println();
        System.out.println("Vehicle removed successfully.");
        System.out.println("Parking slot is now available.");
        System.out.println("Thank you for using our parking system.");
    }


    // ============================================================
    // FREE PARKING SLOT
    // ============================================================

    static void freeParkingSlot(Vehicle vehicle) {

        int level = vehicle.level;

        int slot = vehicle.slot - 1;


        if (level == 1) {

            level1[slot] = false;

        } else if (level == 2) {

            level2[slot] = false;

        } else if (level == 3) {

            level3[slot] = false;

        } else if (level == 4) {

            level4[slot] = false;
        }
    }


    // ============================================================
    // SEARCH VEHICLE
    // ============================================================

    static void searchVehicle() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                   SEARCH VEHICLE");
        System.out.println("======================================================");

        System.out.println("1. Search by Token Number");
        System.out.println("2. Search by Vehicle Number");

        System.out.print("Enter choice: ");

        int choice;

        try {

            choice = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {

            System.out.println("Invalid choice.");
            return;
        }


        // ========================================================
        // SEARCH BY TOKEN
        // ========================================================

        if (choice == 1) {

            System.out.print("Enter token number: ");

            String token = sc.nextLine();


            for (Vehicle v : vehicles) {

                if (v.token.equalsIgnoreCase(token)) {

                    v.displayDetails();

                    return;
                }
            }


            System.out.println();
            System.out.println("Vehicle not found.");
        }


        // ========================================================
        // SEARCH BY VEHICLE NUMBER
        // ========================================================

        else if (choice == 2) {

            System.out.print("Enter vehicle number: ");

            String vehicleNumber = sc.nextLine();


            for (Vehicle v : vehicles) {

                if (v.vehicleNumber.equalsIgnoreCase(vehicleNumber)) {

                    v.displayDetails();

                    return;
                }
            }


            System.out.println();
            System.out.println("Vehicle not found.");
        }


        else {

            System.out.println("Invalid choice.");
        }
    }


    // ============================================================
    // VIEW USER DETAILS
    // ============================================================

    static void viewUserDetails() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                    USER DETAILS");
        System.out.println("======================================================");


        if (vehicles.size() == 0) {

            System.out.println("No vehicles are currently parked.");

            return;
        }


        for (Vehicle v : vehicles) {

            v.displayDetails();
        }
    }


    // ============================================================
    // VIEW AVAILABLE PARKING SLOTS
    // ============================================================

    static void viewAvailableSlots() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("              AVAILABLE PARKING SLOTS");
        System.out.println("======================================================");


        // LEVEL 1

        System.out.println();
        System.out.println("LEVEL 1");
        System.out.println("Vehicle Type : Two Wheeler");
        System.out.println("Location     : Near Exit Gate");

        displaySlots(level1);


        // LEVEL 2

        System.out.println();
        System.out.println("LEVEL 2");
        System.out.println("Vehicle Type : Two Wheeler");
        System.out.println("Location     : Far from Exit Gate");

        displaySlots(level2);


        // LEVEL 3

        System.out.println();
        System.out.println("LEVEL 3");
        System.out.println("Vehicle Type : Four Wheeler");
        System.out.println("Location     : Near Exit Gate");

        displaySlots(level3);


        // LEVEL 4

        System.out.println();
        System.out.println("LEVEL 4");
        System.out.println("Vehicle Type : Four Wheeler");
        System.out.println("Location     : Far from Exit Gate");

        displaySlots(level4);
    }


    // ============================================================
    // DISPLAY SLOTS
    // ============================================================

    static void displaySlots(boolean[] slots) {

        System.out.print("Slots: ");

        for (int i = 0; i < slots.length; i++) {

            if (slots[i]) {

                System.out.print("[X] ");

            } else {

                System.out.print("[" + (i + 1) + "] ");
            }
        }


        System.out.println();

        System.out.println(
                "Available Slots: "
                        + countAvailable(slots)
                        + "/"
                        + slots.length
        );
    }


    // ============================================================
    // COUNT AVAILABLE SLOTS
    // ============================================================

    static int countAvailable(boolean[] slots) {

        int count = 0;


        for (int i = 0; i < slots.length; i++) {

            if (!slots[i]) {

                count++;
            }
        }


        return count;
    }


    // ============================================================
    // PARKING STATUS
    // ============================================================

    static void parkingStatus() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                   PARKING STATUS");
        System.out.println("======================================================");


        int totalSlots =
                level1.length
                        + level2.length
                        + level3.length
                        + level4.length;


        int availableSlots =
                countAvailable(level1)
                        + countAvailable(level2)
                        + countAvailable(level3)
                        + countAvailable(level4);


        int occupiedSlots =
                totalSlots - availableSlots;


        System.out.println("Total Parking Slots : "
                + totalSlots);

        System.out.println("Occupied Slots      : "
                + occupiedSlots);

        System.out.println("Available Slots     : "
                + availableSlots);


        System.out.println("------------------------------------------------------");

        System.out.println("Level 1 Available   : "
                + countAvailable(level1));

        System.out.println("Level 2 Available   : "
                + countAvailable(level2));

        System.out.println("Level 3 Available   : "
                + countAvailable(level3));

        System.out.println("Level 4 Available   : "
                + countAvailable(level4));


        System.out.println("======================================================");
    }


    // ============================================================
    // PARKING STATISTICS
    // ============================================================

    static void parkingStatistics() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                PARKING STATISTICS");
        System.out.println("======================================================");


        int twoWheelers = 0;
        int fourWheelers = 0;


        for (Vehicle v : vehicles) {

            if (v.vehicleType.equals("Two Wheeler")) {

                twoWheelers++;

            } else {

                fourWheelers++;
            }
        }


        int totalSlots =
                level1.length
                        + level2.length
                        + level3.length
                        + level4.length;


        int occupiedSlots = vehicles.size();

        int availableSlots =
                totalSlots - occupiedSlots;


        System.out.println("Total Capacity   : "
                + totalSlots);

        System.out.println("Currently Parked : "
                + occupiedSlots);

        System.out.println("Available        : "
                + availableSlots);


        System.out.println("------------------------------------------------------");

        System.out.println("Two Wheelers     : "
                + twoWheelers);

        System.out.println("Four Wheelers    : "
                + fourWheelers);


        System.out.println("======================================================");
    }


    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        welcomeScreen();


        int choice;


        do {

            dashboard();


            try {

                choice = Integer.parseInt(sc.nextLine());

            } catch (Exception e) {

                System.out.println("Please enter a valid number.");
                choice = 0;
            }


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

                    parkingStatus();

                    break;


                case 7:

                    parkingStatistics();

                    break;


                case 8:

                    System.out.println();
                    System.out.println("======================================================");
                    System.out.println("       THANK YOU FOR USING OUR PARKING SYSTEM");
                    System.out.println("======================================================");

                    break;


                default:

                    System.out.println();
                    System.out.println("Invalid choice.");
                    System.out.println("Please select a number from 1 to 8.");
            }


        } while (choice != 8);


        sc.close();
    }
}