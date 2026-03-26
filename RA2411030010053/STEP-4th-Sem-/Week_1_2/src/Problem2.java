import java.util.*;

public class Problem2 {

    // Stores product stock
    private Map<String, Integer> inventory;

    // Waiting list for products (FIFO)
    private Map<String, Queue<Integer>> waitingList;

    public Problem2() {
        inventory = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product to inventory
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            inventory.put(productId, stock - 1);

            return "Success: Purchase confirmed. Remaining stock = "
                    + (stock - 1);
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.offer(userId);

            return "Out of stock. Added to waiting list. Position #"
                    + queue.size();
        }
    }

    // Process waiting list when stock is refilled
    public synchronized void restockProduct(String productId, int quantity) {

        int currentStock = inventory.getOrDefault(productId, 0);
        inventory.put(productId, currentStock + quantity);

        Queue<Integer> queue = waitingList.get(productId);

        while (inventory.get(productId) > 0 && !queue.isEmpty()) {

            int userId = queue.poll();
            int stock = inventory.get(productId);

            inventory.put(productId, stock - 1);

            System.out.println("User " + userId +
                    " from waiting list purchased the item.");
        }
    }

    // Demo
    public static void main(String[] args) {

        Problem2 manager = new Problem2();

        manager.addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " +
                manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 101));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 102));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 103));

        // Stock finished
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 104));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 105));

        // Restock
        manager.restockProduct("IPHONE15_256GB", 2);

        System.out.println("Stock after restock: "
                + manager.checkStock("IPHONE15_256GB"));
    }
}