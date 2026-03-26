import java.util.*;

public class Problem1 {

    // Stores registered usernames
    private Map<String, Integer> usernameToUserId;

    // Tracks how many times a username was attempted
    private Map<String, Integer> usernameAttempts;

    public Problem1 () {
        usernameToUserId = new HashMap<>();
        usernameAttempts = new HashMap<>();
    }

    // Check if username is available
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        usernameAttempts.put(
                username,
                usernameAttempts.getOrDefault(username, 0) + 1
        );

        // Username available if not in map
        return !usernameToUserId.containsKey(username);
    }

    // Register a new username
    public void registerUsername(String username, int userId) {
        usernameToUserId.put(username, userId);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // Add numbers to username
        for (int i = 1; i <= 5; i++) {
            String newUsername = username + i;

            if (!usernameToUserId.containsKey(newUsername)) {
                suggestions.add(newUsername);
            }
        }

        // Replace underscore with dot
        if (username.contains("_")) {
            String modified = username.replace("_", ".");

            if (!usernameToUserId.containsKey(modified)) {
                suggestions.add(modified);
            }
        }

        return suggestions;
    }

    // Get the most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : usernameAttempts.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted;
    }

    // Test the system
    public static void main(String[] args) {

        Problem1 checker = new Problem1();

        checker.registerUsername("john_doe", 101);
        checker.registerUsername("admin", 1);

        System.out.println("Check john_doe: "
                + checker.checkAvailability("john_doe"));

        System.out.println("Check jane_smith: "
                + checker.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: "
                + checker.suggestAlternatives("john_doe"));

        System.out.println("Most attempted username: "
                + checker.getMostAttempted());
    }
}