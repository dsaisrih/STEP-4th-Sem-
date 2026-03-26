import java.util.*;

class Problem4 {

    // n-gram size
    private int N = 5;

    // nGram -> set of document IDs
    private Map<String, Set<String>> ngramIndex;

    // documentId -> list of ngrams
    private Map<String, List<String>> documentNgrams;

    public Problem4(int n) {
        this.N = n;
        ngramIndex = new HashMap<>();
        documentNgrams = new HashMap<>();
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {

            ngramIndex
                    .computeIfAbsent(gram, k -> new HashSet<>())
                    .add(docId);
        }
    }

    // Generate n-grams from text
    private List<String> generateNgrams(String text) {

        List<String> grams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            grams.add(gram.toString().trim());
        }

        return grams;
    }

    // Analyze a document for plagiarism
    public void analyzeDocument(String docId) {

        List<String> grams = documentNgrams.get(docId);

        Map<String, Integer> matchCounts = new HashMap<>();

        for (String gram : grams) {

            Set<String> docs = ngramIndex.getOrDefault(gram, new HashSet<>());

            for (String otherDoc : docs) {

                if (!otherDoc.equals(docId)) {

                    matchCounts.put(
                            otherDoc,
                            matchCounts.getOrDefault(otherDoc, 0) + 1
                    );
                }
            }
        }

        System.out.println("Analyzing: " + docId);
        System.out.println("Extracted " + grams.size() + " n-grams\n");

        for (String otherDoc : matchCounts.keySet()) {

            int matches = matchCounts.get(otherDoc);

            double similarity = ((double) matches / grams.size()) * 100;

            System.out.println("Matches with " + otherDoc + ": " + matches);
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED");
            }

            System.out.println();
        }
    }

    // Demo
    public static void main(String[] args) {

        Problem4 detector = new Problem4(5);

        String doc1 = "machine learning is a field of artificial intelligence that allows systems to learn from data";
        String doc2 = "machine learning is a field of artificial intelligence used to build intelligent systems";
        String doc3 = "football is a popular sport played worldwide";

        detector.addDocument("essay_089.txt", doc1);
        detector.addDocument("essay_092.txt", doc2);
        detector.addDocument("essay_123.txt", doc1);

        detector.analyzeDocument("essay_123.txt");
    }
}