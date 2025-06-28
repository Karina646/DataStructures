import java.util.*;

public class ArrayMinistrySolution {
    private int[][] bosses; // bosses[i] - начальники чиновника i
    private int[][][] bribeOptions; // bribeOptions[i][j] - j-й вариант виз для чиновника i
    private int[][] bribeCosts; // bribeCosts[i][j] - стоимость j-го варианта для чиновника i
    private int[] bribeCounts; // Количество вариантов для каждого чиновника

    public ArrayMinistrySolution(int N) {
        bosses = new int[N+1][];
        bribeOptions = new int[N+1][][];
        bribeCosts = new int[N+1][];
        bribeCounts = new int[N+1];

        // Инициализация
        for (int i = 1; i <= N; i++) {
            bosses[i] = new int[0];
            bribeOptions[i] = new int[15][]; // Максимум 15 вариантов
            bribeCosts[i] = new int[15];
        }
    }

    public void setBoss(int official, int boss) {
        if (boss == 0) return; // Главный чиновник

        bosses[official] = new int[]{boss};
    }

    public void addBribeOption(int official, int[] requiredSubordinates, int cost) {
        int count = bribeCounts[official];
        bribeOptions[official][count] = requiredSubordinates;
        bribeCosts[official][count] = cost;
        bribeCounts[official]++;
    }

    public int calculateMinCost() {
        int N = bosses.length - 1;
        int[] minCosts = new int[N+1];
        boolean[] calculated = new boolean[N+1];

        // Вычисляем минимальные стоимости снизу вверх
        for (int i = 1; i <= N; i++) {
            calculateMinCost(i, minCosts, calculated);
        }

        return minCosts[1]; // Минимальная стоимость для главного чиновника
    }

    private void calculateMinCost(int official, int[] minCosts, boolean[] calculated) {
        if (calculated[official]) return;

        int minCost = Integer.MAX_VALUE;

        // Перебираем все варианты взяток для этого чиновника
        for (int i = 0; i < bribeCounts[official]; i++) {
            int cost = bribeCosts[official][i];
            int[] required = bribeOptions[official][i];

            // Добавляем стоимости всех требуемых подчиненных
            for (int sub : required) {
                calculateMinCost(sub, minCosts, calculated);
                cost += minCosts[sub];
            }

            if (cost < minCost) {
                minCost = cost;
            }
        }

        minCosts[official] = minCost;
        calculated[official] = true;
    }
}