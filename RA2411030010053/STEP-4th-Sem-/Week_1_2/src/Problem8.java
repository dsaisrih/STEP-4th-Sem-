import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        licensePlate = null;
        occupied = false;
    }
}

public class Problem8 {

    private ParkingSpot[] table;
    private int capacity;
    private int occupiedCount = 0;

    private int totalProbes = 0;
    private int totalParks = 0;

    public Problem8(int capacity) {

        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++)
            table[i] = new ParkingSpot();
    }

    // Hash function
    private int hash(String licensePlate) {

        int hash = Math.abs(licensePlate.hashCode());

        return hash % capacity;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {

            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].occupied = true;
        table[index].entryTime = System.currentTimeMillis();

        occupiedCount++;
        totalProbes += probes;
        totalParks++;

        System.out.println(
                "parkVehicle(\"" + licensePlate + "\") → Assigned spot #" +
                        index + " (" + probes + " probes)"
        );
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();

                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedCount--;

                System.out.println(
                        "exitVehicle(\"" + licensePlate + "\") → Spot #" +
                                index +
                                " freed, Duration: " +
                                String.format("%.2f", hours) +
                                "h, Fee: $" +
                                String.format("%.2f", fee)
                );

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot to entrance
    public int findNearestSpot() {

        for (int i = 0; i < capacity; i++) {

            if (!table[i].occupied)
                return i;
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy =
                ((double) occupiedCount / capacity) * 100;

        double avgProbes =
                totalParks == 0 ? 0 :
                        (double) totalProbes / totalParks;

        System.out.println("\n===== Parking Statistics =====");

        System.out.println("Occupancy: " +
                String.format("%.2f", occupancy) + "%");

        System.out.println("Average Probes: " +
                String.format("%.2f", avgProbes));

        System.out.println("==============================");
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {

        Problem8 lot = new Problem8(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        lot.exitVehicle("ABC-1234");

        int nearest = lot.findNearestSpot();

        System.out.println("Nearest available spot: #" + nearest);

        lot.getStatistics();
    }
}