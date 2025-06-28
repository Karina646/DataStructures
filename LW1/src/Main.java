import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Автор: Степанова Карина Эдуардовна");
        System.out.println("Группа: 090301-ПОВб-з23");

        System.out.print("Введите первое число a: ");
        int a = scanner.nextInt();
        System.out.print("Введите второе число b: ");
        int b = scanner.nextInt();
        long startTime = System.currentTimeMillis();
        int[] result = extendedGCD(a, b);
        int d = result[0];
        int x = result[1];
        int y = result[2];

        System.out.println("d = " + d + "; x = " + x + "; y = " + y);
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + " ms");
        Runtime runtime = Runtime.getRuntime();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory: " + memoryUsed / 1024 / 1024 + " MB");
    }

    public static int[] extendedGCD(int a, int b) {
        if (b == 0) {
            return new int[] { a, 1, 0 };
        }
        int[] result = extendedGCD(b, a % b);
        int d = result[0];
        int x = result[2];
        int y = result[1] - (a / b) * result[2];
        return new int[] { d, x, y };
    }
}