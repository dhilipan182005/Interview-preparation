import java.util.*;

//VEHICLE
class Vehicle {
    private String vehicleNumber;
    private String ownerName;

    public Vehicle(String vehicleNumber, String ownerName) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return vehicleNumber + " (" + ownerName + ")";
    }
}

//SLOT
class Slot {
    private int slotId;
    private Vehicle vehicle; // Correct: object reference

    public Slot(int slotId) {
        this.slotId = slotId;
        this.vehicle = null;
    }

    public boolean isEmpty() {
        return vehicle == null;
    }

    public boolean parkVehicle(Vehicle v) {
        if (isEmpty()) {
            vehicle = v;
            return true;
        }
        return false;
    }

    public boolean removeVehicle() {
        if (!isEmpty()) {
            vehicle = null;
            return true;
        }
        return false;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getSlotId() {
        return slotId;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Slot " + slotId + ": Empty";
        } else {
            return "Slot " + slotId + ": Occupied by " + vehicle;
        }
    }
}

//PARKING LOT
class ParkingLot {
    private List<Slot> slots;

    public ParkingLot(int capacity) {
        slots = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            slots.add(new Slot(i));
        }
    }

    // Park Vehicle
    public boolean parkVehicle(Vehicle v) {
        // Prevent duplicate vehicle
        for (Slot s : slots) {
            if (!s.isEmpty() &&
                s.getVehicle().getVehicleNumber().equalsIgnoreCase(v.getVehicleNumber())) {
                System.out.println("Vehicle already parked!");
                return false;
            }
        }

        // Find empty slot
        for (Slot s : slots) {
            if (s.isEmpty()) {
                s.parkVehicle(v);
                System.out.println("Parked at Slot " + s.getSlotId());
                return true;
            }
        }

        System.out.println("Parking Lot Full!");
        return false;
    }

    // Remove Vehicle
    public boolean removeVehicle(String vehicleNumber) {
        for (Slot s : slots) {
            if (!s.isEmpty() &&
                s.getVehicle().getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {

                s.removeVehicle();
                System.out.println("Vehicle removed from Slot " + s.getSlotId());
                return true;
            }
        }

        System.out.println("Vehicle not found!");
        return false;
    }

    // Display Status
    public void displayStatus() {
        System.out.println("\n--- Parking Lot Status ---");
        for (Slot s : slots) {
            System.out.println(s);
        }
    }
}

// MAIN
public class day1_Parking_System_V1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ParkingLot lot = new ParkingLot(100

        );

        while (true) {
            System.out.println("\n1. Park Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Display Status");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter vehicle number: ");
                String vNum = sc.nextLine();

                System.out.print("Enter owner name: ");
                String owner = sc.nextLine();

                Vehicle v = new Vehicle(vNum, owner);
                lot.parkVehicle(v);

            } else if (choice == 2) {
                System.out.print("Enter vehicle number to remove: ");
                String vNum = sc.nextLine();

                lot.removeVehicle(vNum);

            } else if (choice == 3) {
                lot.displayStatus();

            } else if (choice == 4) {
                System.out.println("Exiting...");
                sc.close();
                break;

            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}