import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MultiLevelParkingManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static final String FILE = "parking_records.txt";
    static final int SLOTS_PER_LEVEL = 5;

    // Parking levels: T = two-wheeler, F = four-wheeler
    static String[] levels = {"T1", "T2", "F1", "F2"};
    static boolean[][] occupied = new boolean[4][SLOTS_PER_LEVEL];

    static ArrayList<ParkingRecord> records = new ArrayList<>();
    static int tokenCounter = 1;

    static final double TWO_WHEELER_RATE = 20.0;
    static final double FOUR_WHEELER_RATE = 40.0;

    // Class representing one parking record
    static class ParkingRecord {
        String token, name, phone, vehicleNumber;
        String vehicleType, level, status;
        int duration, slot;
        long entryTime;
        double bill;

        ParkingRecord(String token, String name, String phone,
                      String vehicleNumber, String vehicleType,
                      int duration, String level, int slot,
                      long entryTime, String status, double bill) {
            this.token = token;
            this.name = name;
            this.phone = phone;
            this.vehicleNumber = vehicleNumber;
            this.vehicleType = vehicleType;
            this.duration = duration;
            this.level = level;
            this.slot = slot;
            this.entryTime = entryTime;
            this.status = status;
            this.bill = bill;
        }

        // Convert a record into a line for file storage
        public String toFileString() {
            return token + "|" + name + "|" + phone + "|"
                    + vehicleNumber + "|" + vehicleType + "|"
                    + duration + "|" + level + "|" + slot + "|"
                    + entryTime + "|" + status + "|" + bill;
        }

        public void display() {
            System.out.println("----------------------------------");
            System.out.println("Token Number   : " + token);
            System.out.println("Customer Name  : " + name);
            System.out.println("Phone Number   : " + phone);
            System.out.println("Vehicle Number : " + vehicleNumber);
            System.out.println("Vehicle Type   : " + vehicleType);
            System.out.println("Parking Level  : " + level);
            System.out.println("Parking Spot   : " + slot);
            System.out.println("Duration       : " + duration + " hour(s)");
            System.out.println("Entry Time     : "
                    + LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(entryTime),
                    java.time.ZoneId.systemDefault()));
            System.out.println("Status         : " + status);
            if (status.equals("COMPLETED")) {
                System.out.printf("Parking Bill   : Rs. %.2f%n", bill);
                
    public static void main(String[] args) {
        loadRecords();

        System.out.println("\n======================================");
        System.out.println("    MULTI-LEVEL PARKING MANAGEMENT");
        System.out.println("======================================");
        System.out.println(" Welcome! Manage your parking easily.");
        System.out.println("======================================");

        int choice;

        do {
            showDashboard();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    parkVehicle();
                    break;
                case 2:
                    showAvailableSlots();
                    break;
                case 3:
                    searchToken();
                    break;
                case 4:
                    viewAllDetails();
                    break;
                case 5:
                    removeVehicle();
                    break;
                case 6:
                    showDashboardSummary();
                    break;
                case 7:
                    System.out.println("Thank you for using our system!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7);

        sc.close();
    }


            }
            System.out.println("----------------------------------");
        }
    }
 // Dashboard
    static void showDashboard() {
        System.out.println("\n============== HOME DASHBOARD ==============");
        System.out.println("1. Park a Vehicle");
        System.out.println("2. View Available Parking Slots");
        System.out.println("3. Search Vehicle by Token Number");
        System.out.println("4. View Customer and Vehicle Details");
        System.out.println("5. Remove Vehicle and Generate Bill");
        System.out.println("6. Parking Summary");
        System.out.println("7. Exit");
        System.out.println("============================================");
    }

    // Safe integer input
    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
