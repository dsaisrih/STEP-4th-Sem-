import java.util.*;

class Client {
    String id;
    int riskScore;
    double accountBalance;

    public Client(String id, int riskScore, double accountBalance) {
        this.id = id;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return id + ":" + riskScore;
    }
}

public class Problem2 {

    // 🔹 Bubble Sort (Ascending riskScore)
    public static void bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    // Swap
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swaps++;
                    swapped = true;

                    // Visualize swap
                    System.out.println("Swap: " + arr[j].id + " <-> " + arr[j + 1].id);
                }
            }

            if (!swapped) break; // optimization
        }

        System.out.println("Bubble Sorted (Asc): " + Arrays.toString(arr));
        System.out.println("Total Swaps: " + swaps);
    }

    // 🔹 Insertion Sort (Descending riskScore + accountBalance)
    public static void insertionSort(Client[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 && compare(arr[j], key) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.println("Insertion Sorted (Desc): " + Arrays.toString(arr));
    }

    // 🔹 Comparator: riskScore DESC, then accountBalance DESC
    private static int compare(Client c1, Client c2) {
        if (c1.riskScore != c2.riskScore) {
            return Integer.compare(c2.riskScore, c1.riskScore); // DESC
        }
        return Double.compare(c2.accountBalance, c1.accountBalance); // DESC
    }

    // 🔹 Top 10 highest risk clients
    public static void topRiskClients(Client[] arr, int topN) {
        System.out.print("Top " + topN + " High-Risk Clients: ");

        for (int i = 0; i < Math.min(topN, arr.length); i++) {
            System.out.print(arr[i].id + "(" + arr[i].riskScore + ") ");
        }
        System.out.println();
    }

    // 🔹 Driver
    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 2000),
                new Client("clientB", 50, 3000)
        };

        // Bubble Sort (Asc)
        bubbleSort(clients);

        // Insertion Sort (Desc)
        insertionSort(clients);

        // Top 3 (or Top 10 for real case)
        topRiskClients(clients, 3);
    }
}