import java.util.*;

public class StandardLibraryMinistrySolution {
    private static class Official {
        int id;
        Official boss;
        List<BribeOption> bribeOptions = new ArrayList<>();

        Official(int id) {
            this.id = id;
        }
    }

    private static class BribeOption {
        List<Integer> requiredSubordinates;
        int cost;

        BribeOption(List<Integer> requiredSubordinates, int cost) {
            this.requiredSubordinates = requiredSubordinates;
            this.cost = cost;
        }
    }

    private Map<Integer, Official> officials = new HashMap<>();

    public StandardLibraryMinistrySolution(int N) {
        for (int i = 1; i <= N; i++) {
            officials.put(i, new Official(i));
        }
    }

    public void setBoss(int official, int boss) {
        if (boss == 0) return;
        officials.get(official).boss = officials.get(boss);
    }

    public void addBribeOption(int official, List<Integer> requiredSubordinates, int cost) {
        officials.get(official).bribeOptions.add(
                new BribeOption(new ArrayList<>(requiredSubordinates), cost));
    }

    public int calculateMinCost() {
        Map<Integer, Integer> minCosts = new HashMap<>();
        Set<Integer> calculated = new HashSet<>();

        for (int id : officials.keySet()) {
            calculateMinCost(officials.get(id), minCosts, calculated);
        }

        return minCosts.get(1);
    }

    private void calculateMinCost(Official official, Map<Integer, Integer> minCosts, Set<Integer> calculated) {
        if (calculated.contains(official.id)) return;

        int minCost = Integer.MAX_VALUE;

        for (BribeOption option : official.bribeOptions) {
            int cost = option.cost;

            for (int sub : option.requiredSubordinates) {
                calculateMinCost(officials.get(sub), minCosts, calculated);
                cost += minCosts.get(sub);
            }

            if (cost < minCost) {
                minCost = cost;
            }
        }

        minCosts.put(official.id, minCost);
        calculated.add(official.id);
    }
}