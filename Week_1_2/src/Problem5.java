import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    public PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class Problem5 {

    // pageUrl -> visit count
    private Map<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private Map<String, Integer> trafficSources = new HashMap<>();


    // Process incoming page view event
    public synchronized void processEvent(PageEvent event) {

        // Count page views
        pageViews.put(
                event.url,
                pageViews.getOrDefault(event.url, 0) + 1
        );

        // Track unique visitors
        uniqueVisitors
                .computeIfAbsent(event.url, k -> new HashSet<>())
                .add(event.userId);

        // Count traffic source
        trafficSources.put(
                event.source,
                trafficSources.getOrDefault(event.source, 0) + 1
        );
    }

    // Get top 10 pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(
                        (a, b) -> b.getValue() - a.getValue()
                );

        pq.addAll(pageViews.entrySet());

        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        int count = 0;

        while (!pq.isEmpty() && count < 10) {
            result.add(pq.poll());
            count++;
        }

        return result;
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====");

        System.out.println("\nTop Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> page : topPages) {

            String url = page.getKey();
            int views = page.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(
                    rank + ". " + url +
                            " - " + views +
                            " views (" + unique + " unique)"
            );

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);

            double percent = ((double) count / total) * 100;

            System.out.println(
                    source + ": " +
                            String.format("%.2f", percent) + "%"
            );
        }

        System.out.println("===============================");
    }


    // Demo
    public static void main(String[] args) throws InterruptedException {

        Problem5 analytics = new Problem5();

        analytics.processEvent(new PageEvent("/article/breaking-news", "user_1", "google"));
        analytics.processEvent(new PageEvent("/article/breaking-news", "user_2", "facebook"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_3", "google"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_3", "direct"));
        analytics.processEvent(new PageEvent("/tech/ai-news", "user_4", "google"));
        analytics.processEvent(new PageEvent("/tech/ai-news", "user_5", "facebook"));
        analytics.processEvent(new PageEvent("/tech/ai-news", "user_6", "direct"));

        analytics.getDashboard();
    }
}