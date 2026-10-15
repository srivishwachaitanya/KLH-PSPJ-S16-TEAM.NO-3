
import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingManagementSystem {

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
            }
            System.out.println("----------------------------------");
        }
    }

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

    // Park a vehicle
    static void parkVehicle() {
        System.out.println("\n--- VEHICLE REGISTRATION ---");

        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid customer name.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();

        if (!phone.matches("[0-9]{10}")) {
            System.out.println("Enter a valid 10-digit phone number.");
            return;
        }

        System.out.print("Enter vehicle registration number: ");
        String vehicleNumber = sc.nextLine().trim().toUpperCase();

        if (vehicleNumber.isEmpty() || vehicleNumber.contains("|")) {
            System.out.println("Invalid vehicle number.");
            return;
        }

        // Prevent duplicate active vehicle registration
        for (ParkingRecord r : records) {
            if (r.status.equals("ACTIVE")
                    && r.vehicleNumber.equalsIgnoreCase(vehicleNumber)) {
                System.out.println("This vehicle is already parked.");
                System.out.println("Existing token: " + r.token);
                return;
            }
        }

        System.out.println("\nVehicle Type:");
        System.out.println("1. Two-Wheeler");
        System.out.println("2. Four-Wheeler");

        int type = readInt("Choose vehicle type: ");

        if (type != 1 && type != 2) {
            System.out.println("Invalid vehicle type.");
            return;
        }

        String vehicleType = (type == 1)
                ? "Two-Wheeler" : "Four-Wheeler";

        System.out.println("Enter expected parking duration (hours).");
        int duration = readInt("Duration: ");

        if (duration <= 0 || duration > 720) {
            System.out.println("Duration must be between 1 and 720 hours.");
            return;
        }

        // Find a suitable spot according to duration
        int[] result = findSpot(type, duration);

        if (result == null) {
            System.out.println("Sorry! No suitable parking spot available.");
            return;
        }

        int levelIndex = result[0];
        int slotIndex = result[1];

        occupied[levelIndex][slotIndex] = true;

        String token = "PK" + System.currentTimeMillis()
                + tokenCounter++;

        ParkingRecord record = new ParkingRecord(
                token, name, phone, vehicleNumber, vehicleType,
                duration, levels[levelIndex], slotIndex + 1,
                System.currentTimeMillis(), "ACTIVE", 0.0);

        records.add(record);
        saveRecords();

        System.out.println("\n========== PARKING TOKEN ==========");
        record.display();
        System.out.println("Keep this token number for vehicle exit.");
        System.out.println("===================================");
    }

    // Allocate spots based on parking duration
    static int[] findSpot(int type, int duration) {
        int startLevel = (type == 1) ? 0 : 2;
        int endLevel = startLevel + 2;

        // Short stay: near exit; medium: middle; long: farther away
        int preferredSlot;

        if (duration <= 2) {
            preferredSlot = 0;
        } else if (duration <= 5) {
            preferredSlot = 2;
        } else {
            preferredSlot = 4;
        }

        // First search at or farther than preferred position
        for (int s = preferredSlot; s < SLOTS_PER_LEVEL; s++) {
            for (int l = startLevel; l < endLevel; l++) {
                if (!occupied[l][s]) {
                    return new int[]{l, s};
                }
            }
        }

        // If necessary, use a closer available spot
        for (int s = 0; s < preferredSlot; s++) {
            for (int l = startLevel; l < endLevel; l++) {
                if (!occupied[l][s]) {
                    return new int[]{l, s};
                }
            }
        }

        return null;
    }

    // Show availability for every level
    static void showAvailableSlots() {
        System.out.println("\n--- PARKING SLOT AVAILABILITY ---");

        for (int l = 0; l < levels.length; l++) {
            int available = 0;

            System.out.println("\nLevel " + levels[l]
                    + (l < 2 ? " (Two-Wheelers)" : " (Four-Wheelers)"));

            for (int s = 0; s < SLOTS_PER_LEVEL; s++) {
                if (!occupied[l][s]) {
                    available++;
                }

                System.out.println("Spot " + (s + 1) + " : "
                        + (occupied[l][s] ? "OCCUPIED" : "AVAILABLE")
                        + (s == 0 ? " (Nearest to exit)" : ""));
            }

            System.out.println("Available spots: " + available
                    + "/" + SLOTS_PER_LEVEL);
        }
    }

    // Search an active vehicle using its token
    static void searchToken() {
        System.out.print("\nEnter token number: ");
        String token = sc.nextLine().trim();

        ParkingRecord r = findRecord(token);

        if (r == null) {
            System.out.println("No record found for this token.");
        } else {
            r.display();

            if (r.status.equals("ACTIVE")) {
                System.out.println("Vehicle is currently parked.");
            } else {
                System.out.println("This parking session is completed.");
            }
        }
    }

    // Search all stored records
    static void viewAllDetails() {
        System.out.println("\n--- CUSTOMER AND VEHICLE DETAILS ---");

        if (records.isEmpty()) {
            System.out.println("No parking records available.");
            return;
        }

        for (ParkingRecord r : records) {
            r.display();
        }
    }

    // Find record by token
    static ParkingRecord findRecord(String token) {
        for (ParkingRecord r : records) {
            if (r.token.equalsIgnoreCase(token)) {
                return r;
            }
        }
        return null;
    }

    // Vehicle exit and bill generation
    static void removeVehicle() {
        System.out.println("\n--- VEHICLE EXIT ---");

        System.out.print("Enter parking token number: ");
        String token = sc.nextLine().trim();

        ParkingRecord r = findRecord(token);

        if (r == null || !r.status.equals("ACTIVE")) {
            System.out.println("Active parking record not found.");
            return;
        }

        long exitTime = System.currentTimeMillis();

        // Charge for actual time, rounding up to the next hour.
        // The expected duration is used for slot allocation only.
        long elapsedMillis = exitTime - r.entryTime;
        long hours = Math.max(1,
                (long) Math.ceil(elapsedMillis / 3600000.0));

        double rate = r.vehicleType.equals("Two-Wheeler")
                ? TWO_WHEELER_RATE : FOUR_WHEELER_RATE;

        r.bill = hours * rate;
        r.status = "COMPLETED";

        int levelIndex = Arrays.asList(levels).indexOf(r.level);
        if (levelIndex >= 0) {
            occupied[levelIndex][r.slot - 1] = false;
        }

        saveRecords();

        System.out.println("\n========== PARKING BILL ==========");
        System.out.println("Token Number    : " + r.token);
        System.out.println("Customer        : " + r.name);
        System.out.println("Vehicle Number  : " + r.vehicleNumber);
        System.out.println("Vehicle Type    : " + r.vehicleType);
        System.out.println("Parking Location: " + r.level
                + " - Spot " + r.slot);
        System.out.println("Entry Time      : "
                + formatTime(r.entryTime));
        System.out.println("Exit Time       : " + formatTime(exitTime));
        System.out.println("Charged Hours   : " + hours);
        System.out.printf("Rate per Hour   : Rs. %.2f%n", rate);
        System.out.printf("TOTAL BILL      : Rs. %.2f%n", r.bill);
        System.out.println("Payment Status  : TO BE PAID");
        System.out.println("==================================");
        System.out.println("Spot released successfully.");
        System.out.println("Thank you! Visit again.");

        System.out.println("\nNote: This is a bill calculation demo.");
        System.out.println("No actual payment is collected.");
    }

    static String formatTime(long time) {
        return LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(time),
                java.time.ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    // Dashboard summary
    static void showDashboardSummary() {
        int active = 0;
        int completed = 0;
        int available = 0;
        double revenue = 0;

        for (ParkingRecord r : records) {
            if (r.status.equals("ACTIVE")) {
                active++;
            } else {
                completed++;
                revenue += r.bill;
            }
        }

        for (int l = 0; l < occupied.length; l++) {
            for (int s = 0; s < occupied[l].length; s++) {
                if (!occupied[l][s]) {
                    available++;
                }
            }
        }

        System.out.println("\n========== PARKING SUMMARY ==========");
        System.out.println("Total Parking Spots : "
                + (levels.length * SLOTS_PER_LEVEL));
        System.out.println("Available Spots     : " + available);
        System.out.println("Vehicles Parked     : " + active);
        System.out.println("Completed Sessions  : " + completed);
        System.out.printf("Recorded Bills      : Rs. %.2f%n", revenue);
        System.out.println("=====================================");
    }

    // Save every record to a text file
    static void saveRecords() {
        try {
            ArrayList<String> lines = new ArrayList<>();

            for (ParkingRecord r : records) {
                lines.add(r.toFileString());
            }

            Files.write(Path.of(FILE), lines);
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    // Load records and restore occupied spots at startup
    static void loadRecords() {
        Path path = Path.of(FILE);

        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                try {
                    String[] p = line.split("\\|", -1);

                    if (p.length != 11) {
                        continue;
                    }

                    ParkingRecord r = new ParkingRecord(
                            p[0], p[1], p[2], p[3], p[4],
                            Integer.parseInt(p[5]), p[6],
                            Integer.parseInt(p[7]),
                            Long.parseLong(p[8]), p[9],
                            Double.parseDouble(p[10]));

                    int levelIndex = Arrays.asList(levels)
                            .indexOf(r.level);

                    if (levelIndex < 0
                            || r.slot < 1
                            || r.slot > SLOTS_PER_LEVEL
                            || r.duration < 1
                            || (!r.status.equals("ACTIVE")
                            && !r.status.equals("COMPLETED"))) {
                        continue;
                    }

                    // Ignore conflicting active records in a damaged file
                    if (r.status.equals("ACTIVE")
                            && occupied[levelIndex][r.slot - 1]) {
                        continue;
                    }

                    records.add(r);

                    if (r.status.equals("ACTIVE")) {
                        occupied[levelIndex][r.slot - 1] = true;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping a malformed saved record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Could not load records: " + e.getMessage());
        }
    }
}
