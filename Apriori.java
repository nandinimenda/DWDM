import java.util.*;

public class Apriori {

    private List<Set<String>> transactions;
    private double minSupport;
    private double minConfidence;

    public Apriori(List<Set<String>> transactions, double minSupport, double minConfidence) {
        this.transactions = transactions;
        this.minSupport = minSupport;
        this.minConfidence = minConfidence;
    }

    public void run() {
        Map<Set<String>, Integer> freqItemsets = new HashMap<>();
        Map<Set<String>, Integer> candidate;

        // Step 1: Generate 1-itemsets
        candidate = generateInitialCandidates();
        while (!candidate.isEmpty()) {
            // Step 2: Count support
            Map<Set<String>, Integer> validItemsets = getFrequentItemsets(candidate);

            // Store frequent itemsets
            freqItemsets.putAll(validItemsets);

            // Step 3: Generate next level candidates
            candidate = generateNextCandidates(validItemsets.keySet());
        }

        // Print Frequent Itemsets
        System.out.println("Frequent Itemsets:");
        for (Set<String> itemset : freqItemsets.keySet()) {
            double support = (double) freqItemsets.get(itemset) / transactions.size();
            if (support >= minSupport) {
                System.out.println(itemset + " -> support: " + support);
            }
        }
    }

    private Map<Set<String>, Integer> generateInitialCandidates() {
        Map<Set<String>, Integer> candidates = new HashMap<>();
        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                Set<String> temp = new HashSet<>();
                temp.add(item);
                candidates.put(temp, 0);
            }
        }
        return candidates;
    }

    private Map<Set<String>, Integer> getFrequentItemsets(Map<Set<String>, Integer> candidates) {
        for (Set<String> transaction : transactions) {
            for (Set<String> candidate : candidates.keySet()) {
                if (transaction.containsAll(candidate)) {
                    candidates.put(candidate, candidates.get(candidate) + 1);
                }
            }
        }

        Map<Set<String>, Integer> result = new HashMap<>();
        for (Set<String> itemset : candidates.keySet()) {
            double support = (double) candidates.get(itemset) / transactions.size();
            if (support >= minSupport) {
                result.put(itemset, candidates.get(itemset));
            }
        }
        return result;
    }

    private Map<Set<String>, Integer> generateNextCandidates(Set<Set<String>> prevFreqItemsets) {
        Map<Set<String>, Integer> candidates = new HashMap<>();
        List<Set<String>> list = new ArrayList<>(prevFreqItemsets);

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Set<String> union = new HashSet<>(list.get(i));
                union.addAll(list.get(j));
                if (union.size() == list.get(i).size() + 1) {
                    candidates.put(union, 0);
                }
            }
        }
        return candidates;
    }

    public static void main(String[] args) {
        List<Set<String>> transactions = new ArrayList<>();
        transactions.add(new HashSet<>(Arrays.asList("milk", "bread", "butter")));
        transactions.add(new HashSet<>(Arrays.asList("milk", "bread")));
        transactions.add(new HashSet<>(Arrays.asList("milk", "butter")));
        transactions.add(new HashSet<>(Arrays.asList("bread", "butter")));

        Apriori apriori = new Apriori(transactions, 0.5, 0.7);
        apriori.run();
    }
}