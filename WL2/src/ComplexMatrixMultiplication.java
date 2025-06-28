import java.util.Random;

public class ComplexMatrixMultiplication {
    static class Complex {
        float real;
        float imag;

        public Complex(float real, float imag) {
            this.real = real;
            this.imag = imag;
        }

        public Complex add(Complex other) {
            return new Complex(this.real + other.real, this.imag + other.imag);
        }

        public Complex multiply(Complex other) {
            float real = this.real * other.real - this.imag * other.imag;
            float imag = this.real * other.imag + this.imag * other.real;
            return new Complex(real, imag);
        }
    }

    private static final int SIZE = 2048;
    private static final int TESTS = 1;
    private static final int BLOCK_SIZE = 32;

    public static void main(String[] args) {
        System.out.println("Автор: Степанова Карина Эдуардовна");
        System.out.println("Группа: 090301-ПОВб-з23");
        Complex[][] matrixA = generateRandomMatrix(SIZE);
        Complex[][] matrixB = generateRandomMatrix(SIZE);
        Complex[][] result = new Complex[SIZE][SIZE];

        // Тестирование наивного метода
        System.out.println("Тестирование наивного умножения...");
        double naiveTime = testMultiplication(matrixA, matrixB, result, ComplexMatrixMultiplication::naiveMultiply);
        double naiveFlops = calculateFlops(naiveTime);
        System.out.printf("Наивный метод: время %.3f с, производительность %.2f MFlops%n", naiveTime, naiveFlops);

        // Тестирование BLAS-подобного метода
        System.out.println("Тестирование BLAS-подобного умножения...");
        double blasTime = testMultiplication(matrixA, matrixB, result, ComplexMatrixMultiplication::blasLikeMultiply);
        double blasFlops = calculateFlops(blasTime);
        System.out.printf("BLAS-подобный метод: время %.3f с, производительность %.2f MFlops%n", blasTime, blasFlops);

        // Тестирование оптимизированного метода
        System.out.println("Тестирование оптимизированного умножения...");
        double optimizedTime = testMultiplication(matrixA, matrixB, result, ComplexMatrixMultiplication::optimizedMultiply);
        double optimizedFlops = calculateFlops(optimizedTime);
        System.out.printf("Оптимизированный метод: время %.3f с, производительность %.2f MFlops%n", optimizedTime, optimizedFlops);

        System.out.printf("Производительность оптимизированного метода составляет %.1f%% от BLAS-подобного%n",
                (optimizedFlops / blasFlops) * 100);
    }

    private static Complex[][] generateRandomMatrix(int size) {
        Random random = new Random();
        Complex[][] matrix = new Complex[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = new Complex(random.nextFloat(), random.nextFloat());
            }
        }
        return matrix;
    }

    private static double testMultiplication(Complex[][] a, Complex[][] b, Complex[][] result,
                                             MatrixMultiplier multiplier) {
        double totalTime = 0;
        for (int i = 0; i < TESTS; i++) {
            long startTime = System.nanoTime();
            multiplier.multiply(a, b, result);
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime) / 1e9;
        }
        return totalTime / TESTS;
    }

    private static double calculateFlops(double time) {
        double c = 2 * Math.pow(SIZE, 3);
        return c / (time * 1e6);
    }

    interface MatrixMultiplier {
        void multiply(Complex[][] a, Complex[][] b, Complex[][] result);
    }

    private static void naiveMultiply(Complex[][] a, Complex[][] b, Complex[][] result) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < SIZE; k++) {
                    sum = sum.add(a[i][k].multiply(b[k][j]));
                }
                result[i][j] = sum;
            }
        }
    }

    private static void blasLikeMultiply(Complex[][] a, Complex[][] b, Complex[][] result) {
        for (int i = 0; i < SIZE; i++) {
            for (int k = 0; k < SIZE; k++) {
                Complex aik = a[i][k];
                for (int j = 0; j < SIZE; j++) {
                    if (k == 0) {
                        result[i][j] = new Complex(0, 0);
                    }
                    result[i][j] = result[i][j].add(aik.multiply(b[k][j]));
                }
            }
        }
    }

    private static void optimizedMultiply(Complex[][] a, Complex[][] b, Complex[][] result) {
        for (int i = 0; i < SIZE; i += BLOCK_SIZE) {
            for (int j = 0; j < SIZE; j += BLOCK_SIZE) {
                for (int ii = i; ii < Math.min(i + BLOCK_SIZE, SIZE); ii++) {
                    for (int jj = j; jj < Math.min(j + BLOCK_SIZE, SIZE); jj++) {
                        result[ii][jj] = new Complex(0, 0);
                    }
                }

                for (int k = 0; k < SIZE; k += BLOCK_SIZE) {
                    for (int ii = i; ii < Math.min(i + BLOCK_SIZE, SIZE); ii++) {
                        for (int kk = k; kk < Math.min(k + BLOCK_SIZE, SIZE); kk++) {
                            Complex aik = a[ii][kk];
                            for (int jj = j; jj < Math.min(j + BLOCK_SIZE, SIZE); jj++) {
                                result[ii][jj] = result[ii][jj].add(aik.multiply(b[kk][jj]));
                            }
                        }
                    }
                }
            }
        }
    }
}