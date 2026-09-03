import java.util.Scanner;

class ParkingSpot {

    int spotNumber;
    boolean occupied;
    String vehicleNumber;
    String vehicleType;
    int parkingHours;
    int parkingFee;
    boolean paid;

    ParkingSpot(int spotNumber) {
        this.spotNumber = spotNumber;
        this.occupied = false;
        this.vehicleNumber = "";
        this.vehicleType = "";
        this.parkingHours = 0;
        this.parkingFee = 0;
        this.paid = false;
    }

    void parkVehicle(String vehicleNumber, String vehicleType,
                     int parkingHours, int parkingFee) {

        this.occupied = true;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.parkingHours = parkingHours;
        this.parkingFee = parkingFee;
        this.paid = true;
    }

    void removeVehicle() {

        this.occupied = false;
        this.vehicleNumber = "";
        this.vehicleType = "";
        this.parkingHours = 0;
        this.parkingFee = 0;
        this.paid = false;
    }
}


class ParkingLevel {

    int levelNumber;
    ParkingSpot[] spots;

    ParkingLevel(int levelNumber, int numberOfSpots) {

        this.levelNumber = levelNumber;

        spots = new ParkingSpot[numberOfSpots];

        for (int i = 0; i < numberOfSpots; i++) {
            spots[i] = new ParkingSpot(i + 1);
        }
    }

    void displayLevelStatus() {

        System.out.println("\n---------- LEVEL " + levelNumber + " ----------");

        for (ParkingSpot spot : spots) {

            if (spot.occupied) {

                System.out.println(
                        "Slot " + spot.spotNumber +
                        " : OCCUPIED | Vehicle: " +
                        spot.vehicleNumber +
                        " | Type: " +
                        spot.vehicleType +
                        " | Hours: " +
                        spot.parkingHours +
                        " | Fee: Rs." +
                        spot.parkingFee
                );

            } else {

                System.out.println(
                        "Slot " + spot.spotNumber +
                        " : EMPTY"
                );
            }
        }
    }

    int availableSpots() {

        int count = 0;

        
        for (ParkingSpot spot : spots) {

            if (!spot.occupied) {
                count++;
            }
        }

        return count;
    }
}


public class MultiLevelParkingSystem1 {

    static Scanner sc = new Scanner(System.in);

    static ParkingLevel[] levels;

    public static void main(String[] args) {

        /*
         * Creating 3 parking levels
         *
         * Level 1 -> 5 slots
         * Level 2 -> 8 slots
         * Level 3 -> 10 slots
         */

        levels = new ParkingLevel[3];

        levels[0] = new ParkingLevel(1, 5);
        levels[1] = new ParkingLevel(2, 8);
        levels[2] = new ParkingLevel(3, 10);

        int choice;


        do {

            displayMenu();

            System.out.print("Enter your choice: ");

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
                    displayParkingStatus();
                    break;

                case 5:
                    showAvailableSlots();
                    break;

                case 6:
                    System.out.println("\nThank you for using the Parking Management System!");
                    break;

                default:
                    System.out.println("\nInvalid choice! Please try again.");
            }

        } while (choice != 6);

        sc.close();
    }


    // ==============================
    // MAIN MENU
    // ==============================

    static void displayMenu() {

        System.out.println("\n==========================================");
        System.out.println("       MULTI-LEVEL PARKING SYSTEM");
        System.out.println("==========================================");

        System.out.println("1. Park Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Search Vehicle");
        System.out.println("4. Display Parking Status");
        System.out.println("5. Show Available Slots");
        System.out.println("6. Exit");

        System.out.println("==========================================");
    }


    // ==============================
    // PARK VEHICLE
    // ==============================

    static void parkVehicle() {

        System.out.println("\n========== PARK VEHICLE ==========");

        System.out.print("Enter vehicle number: ");
        String vehicleNumber = sc.nextLine();

        // Check whether the vehicle is already parked
        if (findVehicle(vehicleNumber) != null) {

            System.out.println("Vehicle is already parked!");

            return;
        }

        // Vehicle type
        System.out.println("\nSelect Vehicle Type:");

        System.out.println("1. Cycle");
        System.out.println("2. Bike");
        System.out.println("3. Car");
        System.out.println("4. Bus");

        System.out.print("Enter vehicle type: ");
        int typeChoice = sc.nextInt();
        sc.nextLine();

        String vehicleType;
        int ratePerHour;

        switch (typeChoice) {

            case 1:
                vehicleType = "Cycle";
                ratePerHour = 10;
                break;

            case 2:
                vehicleType = "Bike";
                ratePerHour = 20;
                break;

            case 3:
                vehicleType = "Car";
                ratePerHour = 40;
                break;

            case 4:
                vehicleType = "Bus";
                ratePerHour = 80;
                break;

            default:
                System.out.println("Invalid vehicle type!");

                return;
        }


        // Display levels
        System.out.println("\nAvailable Parking Levels:");

        for (ParkingLevel level : levels) {

            System.out.println(
                    "Level " + level.levelNumber +
                    " -> " + level.availableSpots() +
                    " free slots"
            );
        }


        // User selects level
        System.out.print("\nChoose parking level: ");

        int levelChoice = sc.nextInt();

        if (levelChoice < 1 || levelChoice > levels.length) {

            System.out.println("Invalid level!");

            return;
        }

        ParkingLevel selectedLevel = levels[levelChoice - 1];


        // Display slots

        System.out.println("\nSlots in Level " + levelChoice + ":");

        for (ParkingSpot spot : selectedLevel.spots) {

            if (spot.occupied) {

                System.out.println(
                        "Slot " + spot.spotNumber + " -> OCCUPIED"
                );

            } else {

                System.out.println(
                        "Slot " + spot.spotNumber + " -> EMPTY"
                );
            }
        }


        // Select exact slot
        System.out.print("\nChoose slot number: ");

        int slotChoice = sc.nextInt();

        if (slotChoice < 1 ||
            slotChoice > selectedLevel.spots.length) {

            System.out.println("Invalid slot number!");

            return;
        }

        ParkingSpot selectedSpot =
                selectedLevel.spots[slotChoice - 1];


        // Check whether slot is occupied
        if (selectedSpot.occupied) {

            System.out.println(
                    "Sorry! This slot is already occupied."
            );

            return;
        }


        // Parking duration
        System.out.print("\nEnter parking time in hours: ");

        int hours = sc.nextInt();

        if (hours <= 0) {

            System.out.println("Parking time must be greater than 0.");

            return;
        }


        // Calculate fee
        int totalFee = hours * ratePerHour;


        // Display bill

        System.out.println("\n================================");
        System.out.println("          PARKING BILL");
        System.out.println("================================");

        System.out.println("Vehicle Number : " + vehicleNumber);
        System.out.println("Vehicle Type   : " + vehicleType);
        System.out.println("Level          : " + levelChoice);
        System.out.println("Slot           : " + slotChoice);
        System.out.println("Parking Hours  : " + hours);
        System.out.println("Rate / Hour    : Rs." + ratePerHour);
        System.out.println("Total Fee      : Rs." + totalFee);

        System.out.println("================================");


        // Payment
        System.out.print("\nEnter payment amount: Rs.");

        int payment = sc.nextInt();

        if (payment < totalFee) {

            System.out.println(
                    "Payment failed! Insufficient amount."
            );

            System.out.println(
                    "Required amount: Rs." + totalFee
            );

            return;
        }


        // Save vehicle information
        selectedSpot.parkVehicle(
                vehicleNumber,
                vehicleType,
                hours,
                totalFee
        );


        // Receipt

        System.out.println("\n================================");
        System.out.println("       PAYMENT SUCCESSFUL");
        System.out.println("================================");

        System.out.println("Vehicle Number : " + vehicleNumber);
        System.out.println("Vehicle Type   : " + vehicleType);
        System.out.println("Level          : " + levelChoice);
        System.out.println("Slot           : " + slotChoice);
        System.out.println("Parking Time   : " + hours + " hour(s)");
        System.out.println("Amount Paid    : Rs." + payment);
        System.out.println("Parking Fee    : Rs." + totalFee);
        System.out.println("================================");
        System.out.println("Vehicle parked successfully!");
    }


    // ==============================
    // REMOVE VEHICLE
    // ==============================


    static void removeVehicle() {

        System.out.println("\n========== REMOVE VEHICLE ==========");

        System.out.print("Enter vehicle number: ");

        String vehicleNumber = sc.nextLine();


        for (ParkingLevel level : levels) {

            for (ParkingSpot spot : level.spots) {

                if (spot.occupied &&
                    spot.vehicleNumber.equalsIgnoreCase(vehicleNumber)) {

                    System.out.println("\nVehicle found!");

                    System.out.println(
                            "Level : " + level.levelNumber
                    );

                    System.out.println(
                            "Slot  : " + spot.spotNumber
                    );

                    System.out.println(
                            "Vehicle Type : " + spot.vehicleType
                    );

                    System.out.println(
                            "Paid Amount : Rs." + spot.parkingFee
                    );


                    System.out.print(
                            "\nDo you want to remove this vehicle? (Y/N): "
                    );

                    String answer = sc.nextLine();

                    if (answer.equalsIgnoreCase("Y")) {

                        spot.removeVehicle();

                        System.out.println(
                                "\nVehicle removed successfully!"
                        );

                    } else {

                        System.out.println(
                                "\nVehicle removal cancelled."
                        );
                    }

                    return;
                }
            }
        }

        System.out.println("Vehicle not found!");
    }


    // ==============================
    // SEARCH VEHICLE
    // ==============================

    static void searchVehicle() {

        System.out.println("\n========== SEARCH VEHICLE ==========");

        System.out.print("Enter vehicle number: ");

        String vehicleNumber = sc.nextLine();


        for (ParkingLevel level : levels) {

            for (ParkingSpot spot : level.spots) {

                if (spot.occupied &&
                    spot.vehicleNumber.equalsIgnoreCase(vehicleNumber)) {

                    System.out.println("\nVehicle Found!");

                    System.out.println("--------------------------------");
                    System.out.println("Vehicle Number : " + spot.vehicleNumber);
                    System.out.println("Vehicle Type   : " + spot.vehicleType);
                    System.out.println("Parking Level  : " + level.levelNumber);
                    System.out.println("Parking Slot   : " + spot.spotNumber);
                    System.out.println("Parking Hours  : " + spot.parkingHours);
                    System.out.println("Parking Fee    : Rs." + spot.parkingFee);
                    System.out.println("Payment Status : " + (spot.paid ? "PAID" : "NOT PAID"));
                    System.out.println("--------------------------------");

                    return;
                }
            }
        }

        System.out.println("Vehicle not found!");
    }


    // ==============================
    // DISPLAY PARKING STATUS
    // ==============================

    static void displayParkingStatus() {

        System.out.println("\n========== PARKING STATUS ==========");

        for (ParkingLevel level : levels) {

            level.displayLevelStatus();
        }
    }


    // ==============================
    // AVAILABLE SLOTS
    // ==============================


    static void showAvailableSlots() {

        System.out.println("\n========== AVAILABLE SLOTS ==========");

        int totalAvailable = 0;

        for (ParkingLevel level : levels) {

            int available = level.availableSpots();

            System.out.println(
                    "Level " + level.levelNumber +
                    " : " + available +
                    " slots available"
            );

            totalAvailable += available;
        }

        System.out.println("-------------------------------------");

        System.out.println(
                "Total Available Slots : " + totalAvailable
        );
    }


    // ==============================
    // FIND VEHICLE
    // ==============================



    static ParkingSpot findVehicle(String vehicleNumber) {

        for (ParkingLevel level : levels) {

            for (ParkingSpot spot : level.spots) {

                if (spot.occupied &&
                    spot.vehicleNumber.equalsIgnoreCase(vehicleNumber)) {

                    return spot;
                }
            }
        }

        return null;
    }
}