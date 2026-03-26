import java.util.Arrays;

public class Problem5 {

    static int comparisons = 0;

    // 🔷 LINEAR SEARCH - FIRST OCCURRENCE
    static int linearFirst(String[] arr, String target) {
        comparisons = 0;
        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].equals(target))
                return i;
        }
        return -1;
    }

    // 🔷 LINEAR SEARCH - LAST OCCURRENCE
    static int linearLast(String[] arr, String target) {
        comparisons = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            comparisons++;
            if (arr[i].equals(target))
                return i;
        }
        return -1;
    }

    // 🔷 BINARY SEARCH - FIRST OCCURRENCE
    static int binaryFirst(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;
        comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid].equals(target)) {
                result = mid;
                high = mid - 1; // move left
            } else if (arr[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    // 🔷 BINARY SEARCH - LAST OCCURRENCE
    static int binaryLast(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;
        comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid].equals(target)) {
                result = mid;
                low = mid + 1; // move right
            } else if (arr[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    // 🔷 COUNT OCCURRENCES USING BINARY SEARCH
    static int countOccurrences(String[] arr, String target) {
        int first = binaryFirst(arr, target);
        int last = binaryLast(arr, target);

        if (first == -1) return 0;
        return last - first + 1;
    }

    // 🔷 MAIN METHOD
    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};

        // 🔹 LINEAR SEARCH
        int firstLin = linearFirst(logs, "accB");
        System.out.println("Linear First accB: index " + firstLin +
                " (" + comparisons + " comparisons)");

        int lastLin = linearLast(logs, "accB");
        System.out.println("Linear Last accB: index " + lastLin +
                " (" + comparisons + " comparisons)");

        // 🔹 SORT FOR BINARY SEARCH
        Arrays.sort(logs); // Required

        System.out.println("Sorted Logs: " + Arrays.toString(logs));

        // 🔹 BINARY SEARCH
        int firstBin = binaryFirst(logs, "accB");
        System.out.println("Binary First accB: index " + firstBin +
                " (" + comparisons + " comparisons)");

        int lastBin = binaryLast(logs, "accB");
        System.out.println("Binary Last accB: index " + lastBin +
                " (" + comparisons + " comparisons)");

        int count = countOccurrences(logs, "accB");
        System.out.println("Count of accB: " + count);
    }
}