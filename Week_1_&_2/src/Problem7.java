import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfQuery = false;
}

class Query {
    String text;
    int frequency;

    Query(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }
}

public class Problem7 {

    private TrieNode root;

    // query -> frequency
    private Map<String, Integer> frequencyMap;

    public Problem7() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

    // Insert query into Trie
    public void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEndOfQuery = true;

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
    }

    // Update frequency after a new search
    public void updateFrequency(String query) {
        insertQuery(query);
    }

    // Search top 10 suggestions for prefix
    public List<Query> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        dfs(node, prefix, results);

        // Min heap for top 10
        PriorityQueue<Query> pq =
                new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        for (String query : results) {

            int freq = frequencyMap.get(query);

            pq.offer(new Query(query, freq));

            if (pq.size() > 10)
                pq.poll();
        }

        List<Query> topResults = new ArrayList<>();

        while (!pq.isEmpty())
            topResults.add(pq.poll());

        Collections.reverse(topResults);

        return topResults;
    }

    // DFS to collect queries
    private void dfs(TrieNode node, String prefix, List<String> results) {

        if (node.isEndOfQuery)
            results.add(prefix);

        for (char c : node.children.keySet()) {

            dfs(node.children.get(c),
                    prefix + c,
                    results);
        }
    }

    // Demo
    public static void main(String[] args) {

        Problem7 system = new Problem7();

        system.insertQuery("java tutorial");
        system.insertQuery("javascript");
        system.insertQuery("java download");
        system.insertQuery("java tutorial");
        system.insertQuery("java 21 features");

        List<Query> results = system.search("jav");

        System.out.println("Suggestions for 'jav':");

        int rank = 1;

        for (Query q : results) {

            System.out.println(rank + ". " + q.text +
                    " (" + q.frequency + " searches)");

            rank++;
        }

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        System.out.println("\nUpdated frequency for 'java 21 features': "
                + system.frequencyMap.get("java 21 features"));
    }
}