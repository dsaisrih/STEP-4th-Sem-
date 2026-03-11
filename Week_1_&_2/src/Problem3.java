import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class Problem3 {

    private int capacity;

    // LinkedHashMap maintains LRU order
    private LinkedHashMap<String, DNSEntry> cache;

    private int cacheHits = 0;
    private int cacheMisses = 0;

    public Problem3(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > Problem3.this.capacity;
            }
        };

        startCleanupThread();
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        long start = System.nanoTime();

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                cacheHits++;
                System.out.println("Cache HIT → " + entry.ipAddress);
                return entry.ipAddress;
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED");
            }
        }

        cacheMisses++;

        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 300));

        long end = System.nanoTime();

        System.out.println("Cache MISS → Queried upstream → " + ip +
                " (Lookup time: " + (end - start) / 1_000_000.0 + " ms)");

        return ip;
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {

        try {
            Thread.sleep(100); // simulate 100ms network delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    // Cache statistics
    public void getCacheStats() {

        int total = cacheHits + cacheMisses;
        double hitRate = total == 0 ? 0 : ((double) cacheHits / total) * 100;

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    // Background cleanup thread
    private void startCleanupThread() {

        Thread cleaner = new Thread(() -> {

            while (true) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (this) {

                    Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

                    while (iterator.hasNext()) {

                        Map.Entry<String, DNSEntry> entry = iterator.next();

                        if (entry.getValue().isExpired()) {
                            iterator.remove();
                        }
                    }
                }
            }

        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

    // Test program
    public static void main(String[] args) throws InterruptedException {

        Problem3 dnsCache = new Problem3(5);

        dnsCache.resolve("google.com");

        dnsCache.resolve("google.com");

        dnsCache.resolve("openai.com");

        dnsCache.resolve("google.com");

        dnsCache.getCacheStats();
    }
}