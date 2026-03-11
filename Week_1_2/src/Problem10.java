import java.util.*;

class VideoData {
    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class Problem10 {

    private final int L1_CAPACITY = 10000;
    private final int L2_CAPACITY = 100000;

    // L1 Cache (Memory)
    private LinkedHashMap<String, VideoData> L1Cache =
            new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > L1_CAPACITY;
                }
            };

    // L2 Cache (SSD simulation)
    private LinkedHashMap<String, VideoData> L2Cache =
            new LinkedHashMap<>(L2_CAPACITY, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > L2_CAPACITY;
                }
            };

    // L3 Database
    private Map<String, VideoData> database = new HashMap<>();

    // Access count for promotion
    private Map<String, Integer> accessCount = new HashMap<>();

    // Statistics
    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;

    public Problem10() {

        // simulate database videos
        for (int i = 1; i <= 200000; i++) {
            database.put("video_" + i,
                    new VideoData("video_" + i, "VideoContent_" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 lookup
        if (L1Cache.containsKey(videoId)) {

            L1Hits++;

            System.out.println("L1 Cache HIT (0.5ms)");

            return L1Cache.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 lookup
        if (L2Cache.containsKey(videoId)) {

            L2Hits++;

            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2Cache.get(videoId);

            promoteToL1(video);

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database lookup
        if (database.containsKey(videoId)) {

            L3Hits++;

            System.out.println("L3 Database HIT (150ms)");

            VideoData video = database.get(videoId);

            L2Cache.put(videoId, video);

            accessCount.put(videoId, 1);

            return video;
        }

        return null;
    }

    private void promoteToL1(VideoData video) {

        String id = video.videoId;

        int count = accessCount.getOrDefault(id, 0) + 1;

        accessCount.put(id, count);

        if (count > 3) {

            L1Cache.put(id, video);

            System.out.println("Promoted to L1");
        }
    }

    public void updateVideo(String videoId, VideoData newData) {

        database.put(videoId, newData);

        L1Cache.remove(videoId);
        L2Cache.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = (double) L1Hits / total * 100;
        double l2Rate = (double) L2Hits / total * 100;
        double l3Rate = (double) L3Hits / total * 100;

        System.out.println("\n===== Cache Statistics =====");

        System.out.println("L1 Hit Rate: " + String.format("%.2f", l1Rate) + "%");
        System.out.println("L2 Hit Rate: " + String.format("%.2f", l2Rate) + "%");
        System.out.println("L3 Hit Rate: " + String.format("%.2f", l3Rate) + "%");

        System.out.println("============================");
    }

    public static void main(String[] args) {

        Problem10 cache = new Problem10();

        cache.getVideo("video_123");

        System.out.println();

        cache.getVideo("video_123");

        System.out.println();

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}