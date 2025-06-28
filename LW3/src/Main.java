import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Автор: Степанова Карина Эдуардовна");
        System.out.println("Группа: 090301-ПОВб-з23");
        System.out.println("-------------------------------\n");

        // Тестирование реализации с массивами
        testArrayImplementation();

        // Тестирование реализации со связанными списками
        testLinkedListImplementation();

        // Тестирование реализации со стандартными библиотеками
        testStandardLibraryImplementation();
    }

    private static void testArrayImplementation() {
        System.out.println("Реализация тестового массива...");
        long startTime = System.nanoTime();

        ArrayMinistrySolution ministry = new ArrayMinistrySolution(5); // Пример с 5 чиновниками

        // Настройка структуры чиновников
        ministry.setBoss(1, 0); // Чиновник 1 - главный (не имеет начальника)
        ministry.setBoss(2, 1);
        ministry.setBoss(3, 1);
        ministry.setBoss(4, 2);
        ministry.setBoss(5, 2);

        // Добавление вариантов взяток для каждого чиновника
        ministry.addBribeOption(1, new int[]{}, 100); // Чиновник 1 требует 100 без виз
        ministry.addBribeOption(1, new int[]{2, 3}, 50); // Или 50 с визами 2 и 3

        ministry.addBribeOption(2, new int[]{}, 50);
        ministry.addBribeOption(2, new int[]{4, 5}, 20);

        ministry.addBribeOption(3, new int[]{}, 50);

        ministry.addBribeOption(4, new int[]{}, 10);
        ministry.addBribeOption(5, new int[]{}, 10);

        // Вычисление минимальной стоимости
        int minCost = ministry.calculateMinCost();
        System.out.println("Минимальная стоимость: " + minCost);

        long endTime = System.nanoTime();
        System.out.println("Время выполнения: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void testLinkedListImplementation() {
        System.out.println("\nТестирование реализации связанного списка...");
        long startTime = System.nanoTime();

        LinkedListMinistrySolution ministry = new LinkedListMinistrySolution(5);

        // Аналогичная настройка
        ministry.setBoss(1, 0);
        ministry.setBoss(2, 1);
        ministry.setBoss(3, 1);
        ministry.setBoss(4, 2);
        ministry.setBoss(5, 2);

        ministry.addBribeOption(1, new int[]{}, 100);
        ministry.addBribeOption(1, new int[]{2, 3}, 50);

        ministry.addBribeOption(2, new int[]{}, 50);
        ministry.addBribeOption(2, new int[]{4, 5}, 20);

        ministry.addBribeOption(3, new int[]{}, 50);

        ministry.addBribeOption(4, new int[]{}, 10);
        ministry.addBribeOption(5, new int[]{}, 10);

        int minCost = ministry.calculateMinCost();
        System.out.println("Минимальная стоимость: " + minCost);

        long endTime = System.nanoTime();
        System.out.println("Время выполнения: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void testStandardLibraryImplementation() {
        System.out.println("\nТестирование реализации стандартной библиотеки...");
        long startTime = System.nanoTime();

        StandardLibraryMinistrySolution ministry = new StandardLibraryMinistrySolution(5);

        // Аналогичная настройка
        ministry.setBoss(1, 0);
        ministry.setBoss(2, 1);
        ministry.setBoss(3, 1);
        ministry.setBoss(4, 2);
        ministry.setBoss(5, 2);

        ministry.addBribeOption(1, new ArrayList<>(), 100);
        ministry.addBribeOption(1, Arrays.asList(2, 3), 50);

        ministry.addBribeOption(2, new ArrayList<>(), 50);
        ministry.addBribeOption(2, Arrays.asList(4, 5), 20);

        ministry.addBribeOption(3, new ArrayList<>(), 50);

        ministry.addBribeOption(4, new ArrayList<>(), 10);
        ministry.addBribeOption(5, new ArrayList<>(), 10);

        int minCost = ministry.calculateMinCost();
        System.out.println("Минимальная стоимость: " + minCost);

        long endTime = System.nanoTime();
        System.out.println("Время выполнения: " + (endTime - startTime) / 1_000_000 + " ms");
    }
}