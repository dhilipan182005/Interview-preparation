import java.util.*;

class Vehicle {
    private String vehicleName;
    private String ownerName;

    public Vehicle(String vehicleName, String ownerName) {
        this.vehicleName = vehicleName;
        this.ownerName = ownerName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return vehicleName + " owned by " + ownerName;
    }
}

class Slot {
    private int slotId;
    private Vehicle vehicle;

    public Slot(int slotId) {
        this.slotId = slotId;
        this.vehicle = null;
    }

    public boolean isEmpty() {
        return vehicle == null;
    }

    public boolean park(Vehicle v) {
        if (isEmpty()) {
            this.vehicle = v;
            return true;
        }
        return false;
    }

    public boolean remove() {
        if (!isEmpty()) {
            this.vehicle = null;
            return true;
        }
        return false;
    }

    public int getSlotId() {
        return slotId;
    }

    public Vehicle getVehicle() {
        return vehicle;
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

class ParkingLot {
    private List<Slot> slots;

    public ParkingLot(int totalSlots) {
        slots = new ArrayList<>();
        for (int i = 1; i <= totalSlots; i++) {
            slots.add(new Slot(i));
        }
    }

    public boolean parkVehicle(Vehicle v) {
        for (Slot slot : slots) {
            if (slot.isEmpty()) {
                slot.park(v);
                System.out.println("Vehicle " + v + " parked in Slot " + slot.getSlotId());
                return true;
            }
        }
        System.out.println("Parking Lot Full! Cannot park " + v);
        return false;
    }

    public boolean removeVehicle(String vehicleName) {
        for (Slot slot : slots) {
            if (!slot.isEmpty() && slot.getVehicle().getVehicleName().equalsIgnoreCase(vehicleName)) {
                slot.remove();
                System.out.println("Vehicle " + vehicleName + " removed from Slot " + slot.getSlotId());
                return true;
            }
        }
        System.out.println("Vehicle " + vehicleName + " not found in Parking Lot.");
        return false;
    }

    public void displayStatus() {
        System.out.println("\n--- Parking Lot Status ---");
        for (Slot slot : slots) {
            System.out.println(slot);
        }
    }
}

public class ParkingSystemV1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ParkingLot lot = new ParkingLot(100);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Display Status");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter vehicle number : ");
                    String vName = sc.nextLine();
                    System.out.print("Enter owner name: ");
                    String oName = sc.nextLine();
                    Vehicle v = new Vehicle(vName, oName);
                    lot.parkVehicle(v);
                    break;

                case 2:
                    System.out.print("Enter vehicle number to remove: ");
                    String removeName = sc.nextLine();
                    lot.removeVehicle(removeName);
                    break;

                case 3:
                    lot.displayStatus();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}