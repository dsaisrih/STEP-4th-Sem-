class Asset {
    String name;
    double returnRate;
    double volatility;

    Asset(String name, double returnRate, double volatility) {
        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }
}

public class Problem4 {

    // 🔷 MERGE SORT (ASCENDING by returnRate, STABLE)
    static void mergeSort(Asset[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    static void merge(Asset[] arr, int l, int m, int r) {
        int n1 = m - l + 1, n2 = r - m;

        Asset[] L = new Asset[n1];
        Asset[] R = new Asset[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[l + i];
        for (int j = 0; j < n2; j++) R[j] = arr[m + 1 + j];

        int i = 0, j = 0, k = l;

        while (i < n1 && j < n2) {
            if (L[i].returnRate <= R[j].returnRate) // stable
                arr[k++] = L[i++];
            else
                arr[k++] = R[j++];
        }

        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // 🔷 QUICK SORT (DESC by returnRate, ASC by volatility)
    static void quickSort(Asset[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(Asset[] arr, int low, int high) {

        // 🔥 Median-of-3 Pivot Selection
        int mid = (low + high) / 2;
        Asset pivot = medianOfThree(arr[low], arr[mid], arr[high]);

        // Move pivot to end
        int pivotIndex = high;
        for (int i = low; i <= high; i++) {
            if (arr[i] == pivot) {
                swap(arr, i, high);
                break;
            }
        }

        int i = low - 1;

        for (int j = low; j < high; j++) {
            // DESC by returnRate, ASC by volatility
            if (arr[j].returnRate > pivot.returnRate ||
                    (arr[j].returnRate == pivot.returnRate &&
                            arr[j].volatility < pivot.volatility)) {

                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    // 🔷 MEDIAN OF THREE
    static Asset medianOfThree(Asset a, Asset b, Asset c) {
        if ((a.returnRate > b.returnRate && a.returnRate < c.returnRate) ||
                (a.returnRate < b.returnRate && a.returnRate > c.returnRate))
            return a;

        else if ((b.returnRate > a.returnRate && b.returnRate < c.returnRate) ||
                (b.returnRate < a.returnRate && b.returnRate > c.returnRate))
            return b;

        else
            return c;
    }

    // 🔷 SWAP
    static void swap(Asset[] arr, int i, int j) {
        Asset temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 🔷 PRINT
    static void print(Asset[] arr) {
        for (Asset a : arr) {
            System.out.print(a.name + ":" + a.returnRate + "% ");
        }
        System.out.println();
    }

    // 🔷 MAIN
    public static void main(String[] args) {

        Asset[] assets = {
                new Asset("AAPL", 12, 5),
                new Asset("TSLA", 8, 9),
                new Asset("GOOG", 15, 4)
        };

        // 🔹 Merge Sort (Ascending)
        mergeSort(assets, 0, assets.length - 1);
        System.out.print("Merge Sort (Ascending): ");
        print(assets);

        // 🔹 Quick Sort (Descending + Volatility ASC)
        quickSort(assets, 0, assets.length - 1);
        System.out.print("Quick Sort (Desc + Volatility ASC): ");
        print(assets);
    }
}