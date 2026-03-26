import java.util.Arrays;

public class Problem6 {

    static int comparisons = 0;

    // 🔷 LINEAR SEARCH (UNSORTED)
    static boolean linearSearch(int[] arr, int target) {
        comparisons = 0;
        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target)
                return true;
        }
        return false;
    }

    // 🔷 BINARY SEARCH - INSERTION POINT (LOWER BOUND)
    static int lowerBound(int[] arr, int target) {
        int low = 0, high = arr.length;
        comparisons = 0;

        while (low < high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low; // insertion index
    }

    // 🔷 FLOOR (largest ≤ target)
    static int floor(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int ans = -1;
        comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] <= target) {
                ans = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    // 🔷 CEILING (smallest ≥ target)
    static int ceiling(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int ans = -1;
        comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] >= target) {
                ans = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }

    // 🔷 MAIN METHOD
    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100};
        int target = 30;

        // 🔹 Linear Search (unsorted case)
        boolean found = linearSearch(risks, target);
        System.out.println("Linear Search: " + target +
                (found ? " found" : " not found") +
                " (" + comparisons + " comparisons)");

        // 🔹 Ensure sorted for binary
        Arrays.sort(risks);
        System.out.println("Sorted Risks: " + Arrays.toString(risks));

        // 🔹 Binary Lower Bound (Insertion Point)
        int index = lowerBound(risks, target);
        System.out.println("Insertion Index: " + index +
                " (" + comparisons + " comparisons)");

        // 🔹 Floor & Ceiling
        int f = floor(risks, target);
        System.out.println("Floor(" + target + "): " + f +
                " (" + comparisons + " comparisons)");

        int c = ceiling(risks, target);
        System.out.println("Ceiling(" + target + "): " + c +
                " (" + comparisons + " comparisons)");
    }
}