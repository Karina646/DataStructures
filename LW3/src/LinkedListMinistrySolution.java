import java.util.*;

public class LinkedListMinistrySolution {
    private static class Official {
        int id;
        Official boss;
        List<BribeOption> bribeOptions = new LinkedList<>();

        Official(int id) {
            this.id = id;
        }
    }

    private static class BribeOption {
        List<Integer> requiredSubordinates = new LinkedList<>();
        int cost;

        BribeOption(List<Integer> requiredSubordinates, int cost) {
            this.requiredSubordinates = requiredSubordinates;
            this.cost = cost;
        }
    }

    private Official[] officials;

    public LinkedListMinistrySolution(int N) {
        officials = new Official[N+1];
        for (int i = 1; i <= N; i++) {
            officials[i] = new Official(i);
        }
    }

    public void setBoss(int official, int boss) {
        if (boss == 0) return;
        officials[official].boss = officials[boss];
    }

    public void addBribeOption(int official, int[] requiredSubordinates, int cost) {
        List<Integer> required = new LinkedList<>();
        for (int sub : requiredSubordinates) {
            required.add(sub);
        }
        officials[official].bribeOptions.add(new BribeOption(required, cost));
    }

    public int calculateMinCost() {
        int N = officials.length - 1;
        int[] minCosts = new int[N+1];
        boolean[] calculated = new boolean[N+1];

        for (int i = 1; i <= N; i++) {
            calculateMinCost(officials[i], minCosts, calculated);
        }

        return minCosts[1];
    }

    private void calculateMinCost(Official official, int[] minCosts, boolean[] calculated) {
        if (calculated[official.id]) return;

        int minCost = Integer.MAX_VALUE;

        for (BribeOption option : official.bribeOptions) {
            int cost = option.cost;

            for (int sub : option.requiredSubordinates) {
                calculateMinCost(officials[sub], minCosts, calculated);
                cost += minCosts[sub];
            }

            if (cost < minCost) {
                minCost = cost;
            }
        }

        minCosts[official.id] = minCost;
        calculated[official.id] = true;
    }
}