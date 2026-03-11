import java.util.*;

class Transaction {

    int id;
    int amount;
    String merchant;
    String account;
    long timestamp;

    public Transaction(int id, int amount, String merchant, String account, long timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.timestamp = timestamp;
    }
}

public class Problem9 {

    List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public List<int[]> findTwoSum(int target) {

        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                result.add(new int[]{
                        map.get(complement).id,
                        t.id
                });
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Two-Sum within time window (seconds)
    public List<int[]> findTwoSumWithinWindow(int target, long windowSeconds) {

        Map<Integer, List<Transaction>> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                for (Transaction prev : map.get(complement)) {

                    if (Math.abs(t.timestamp - prev.timestamp) <= windowSeconds) {

                        result.add(new int[]{prev.id, t.id});
                    }
                }
            }

            map.computeIfAbsent(t.amount, k -> new ArrayList<>()).add(t);
        }

        return result;
    }

    // Duplicate detection (same amount + merchant, different accounts)
    public List<List<Transaction>> detectDuplicates() {

        Map<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        List<List<Transaction>> duplicates = new ArrayList<>();

        for (List<Transaction> group : map.values()) {

            Set<String> accounts = new HashSet<>();

            for (Transaction t : group)
                accounts.add(t.account);

            if (accounts.size() > 1)
                duplicates.add(group);
        }

        return duplicates;
    }

    // K-Sum using recursion
    public List<List<Integer>> findKSum(int k, int target) {

        List<List<Integer>> result = new ArrayList<>();

        backtrack(0, k, target, new ArrayList<>(), result);

        return result;
    }

    private void backtrack(int index, int k, int target,
                           List<Integer> path,
                           List<List<Integer>> result) {

        if (k == 0 && target == 0) {

            result.add(new ArrayList<>(path));
            return;
        }

        if (k == 0 || index >= transactions.size())
            return;

        for (int i = index; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            path.add(t.id);

            backtrack(i + 1, k - 1,
                    target - t.amount,
                    path,
                    result);

            path.remove(path.size() - 1);
        }
    }

    // Demo
    public static void main(String[] args) {

        Problem9 analyzer = new Problem9();

        analyzer.addTransaction(new Transaction(1, 500, "Store A", "acc1", 1000));
        analyzer.addTransaction(new Transaction(2, 300, "Store B", "acc2", 1100));
        analyzer.addTransaction(new Transaction(3, 200, "Store C", "acc3", 1200));
        analyzer.addTransaction(new Transaction(4, 500, "Store A", "acc4", 1300));

        System.out.println("Two-Sum target=500:");

        for (int[] pair : analyzer.findTwoSum(500))
            System.out.println(Arrays.toString(pair));

        System.out.println("\nTwo-Sum within 1 hour:");

        for (int[] pair : analyzer.findTwoSumWithinWindow(500, 3600))
            System.out.println(Arrays.toString(pair));

        System.out.println("\nDuplicate Transactions:");

        for (List<Transaction> group : analyzer.detectDuplicates()) {

            for (Transaction t : group)
                System.out.print(t.id + " ");

            System.out.println();
        }

        System.out.println("\nK-Sum (k=3, target=1000):");

        for (List<Integer> ids : analyzer.findKSum(3, 1000))
            System.out.println(ids);
    }
}