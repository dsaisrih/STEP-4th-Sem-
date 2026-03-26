import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {

    int maxTokens;
    double refillRate; // tokens per second
    double tokens;
    long lastRefillTime;

    public TokenBucket(int maxTokens, int refillPerHour) {
        this.maxTokens = maxTokens;
        this.refillRate = refillPerHour / 3600.0;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens based on time passed
    private void refill() {

        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        double newTokens = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + newTokens);

        lastRefillTime = now;
    }

    // try consuming a token
    public synchronized boolean allowRequest() {

        refill();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        return (int) tokens;
    }
}

public class Problem6 {

    private int maxRequests = 1000;

    // clientId -> token bucket
    private Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();


    public String checkRateLimit(String clientId) {

        buckets.putIfAbsent(clientId, new TokenBucket(maxRequests, maxRequests));

        TokenBucket bucket = buckets.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {

            return "Allowed (" + bucket.getRemainingTokens() +
                    " requests remaining)";
        } else {

            return "Denied (0 requests remaining, retry later)";
        }
    }


    public Map<String, Object> getRateLimitStatus(String clientId) {

        TokenBucket bucket = buckets.get(clientId);

        Map<String, Object> status = new HashMap<>();

        if (bucket != null) {
            status.put("used", maxRequests - bucket.getRemainingTokens());
            status.put("limit", maxRequests);
        }

        return status;
    }


    // Demo
    public static void main(String[] args) {

        Problem6 limiter = new Problem6();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {

            System.out.println(
                    limiter.checkRateLimit(client)
            );
        }

        System.out.println("\nStatus: " +
                limiter.getRateLimitStatus(client));
    }
}