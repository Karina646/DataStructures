#include <iostream>
#include <complex>
#include <random>
#include <chrono>
#include <vector>
#include <fstream>
#include <mkl.h>
#include <Windows.h>
#include <immintrin.h>

using namespace std;
using namespace std::chrono;
using complex_f = complex<float>;

// Генерация случайной матрицы
void generate_random_matrix(vector<complex_f>& matrix, int n) {
    random_device rd;
    mt19937 gen(rd());
    uniform_real_distribution<float> dist(-1.0f, 1.0f);

    for (int i = 0; i < n * n; ++i) {
        matrix[i] = complex_f(dist(gen), dist(gen));
    }
}

// 1. Наивное умножение матриц по формуле из линейной алгебры
void naive_matrix_mult(const vector<complex_f>& A, const vector<complex_f>& B, vector<complex_f>& C, int n) {
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            complex_f sum(0.0f, 0.0f);
            for (int k = 0; k < n; ++k) {
                sum += A[i * n + k] * B[k * n + j];
            }
            C[i * n + j] = sum;
        }
    }
}

// 2. Умножение матриц с использованием cblas_cgemm
void blas_matrix_mult(const vector<complex_f>& A, const vector<complex_f>& B, vector<complex_f>& C, int n) {
    const complex_f alpha(1.0f, 0.0f);
    const complex_f beta(0.0f, 0.0f);
    cblas_cgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans,
        n, n, n, &alpha,
        A.data(), n,
        B.data(), n,
        &beta,
        C.data(), n);
}

// 3. Оптимизированное умножение матриц (блочный алгоритм)
void optimized_matrix_mult(const vector<complex_f>& A, const vector<complex_f>& B, vector<complex_f>& C, int n, int block_size = 64) {
    // Инициализация результата нулями
    fill(C.begin(), C.end(), complex_f(0.0f, 0.0f));

    for (int ii = 0; ii < n; ii += block_size) {
        for (int kk = 0; kk < n; kk += block_size) {
            for (int jj = 0; jj < n; jj += block_size) {
                // Границы блоков
                int i_end = min(ii + block_size, n);
                int k_end = min(kk + block_size, n);
                int j_end = min(jj + block_size, n);

                // Основные вычисления
                for (int i = ii; i < i_end; ++i) {
                    const complex_f* A_ptr = &A[i * n + kk];
                    complex_f* C_ptr = &C[i * n + jj];

                    for (int k = kk; k < k_end; ++k) {
                        const complex_f a = *A_ptr++; // A[i][k]
                        const complex_f* B_ptr = &B[k * n + jj];

                        // Развертка внутреннего цикла (loop unrolling)
                        int j = jj;
                        for (; j + 3 < j_end; j += 4) {
                            C_ptr[0] += a * B_ptr[0];
                            C_ptr[1] += a * B_ptr[1];
                            C_ptr[2] += a * B_ptr[2];
                            C_ptr[3] += a * B_ptr[3];
                            C_ptr += 4;
                            B_ptr += 4;
                        }
                        // Обработка оставшихся элементов
                        for (; j < j_end; ++j) {
                            *C_ptr++ += a * *B_ptr++;
                        }
                        C_ptr -= (j_end - jj);
                    }
                }
            }
        }
    }
}



int main() {
    SetConsoleOutputCP(1251); // Устанавливаем кодировку Windows-1251 для русских букв
    SetConsoleCP(1251);
    
    cout << "Автор: Степанова Карина Эдуардовна" << endl;
    cout << "Группа: 090301-ПОВб-з23" << endl;

    const int n = 2048;
    const long long complexity = 2LL * n * n * n; // 2*n^3 операций

    // Выделение памяти для матриц
    vector<complex_f> A(n * n);
    vector<complex_f> B(n * n);
    vector<complex_f> C_naive(n * n);
    vector<complex_f> C_blas(n * n);
    vector<complex_f> C_optimized(n * n);

    // Генерация случайных матриц
    cout << "Генерация случайных матриц..." << endl;
    generate_random_matrix(A, n);
    generate_random_matrix(B, n);

    // 1. Наивное умножение
    cout << "Выполнение наивного умножения матриц..." << endl;
    auto start = high_resolution_clock::now();
    naive_matrix_mult(A, B, C_naive, n);
    auto end = high_resolution_clock::now();
    double naive_time = duration_cast<duration<double>>(end - start).count();
    double naive_perf = complexity / (naive_time * 1e6); // MFlops

    // 2. BLAS умножение
    cout << "Выполнение умножения матриц с использованием BLAS..." << endl;
    start = high_resolution_clock::now();
    blas_matrix_mult(A, B, C_blas, n);
    end = high_resolution_clock::now();
    double blas_time = duration_cast<duration<double>>(end - start).count();
    double blas_perf = complexity / (blas_time * 1e6); // MFlops 

    // 3. Оптимизированное умножение
    cout << "Выполнение оптимизированного умножения матриц..." << endl;
    start = high_resolution_clock::now();
    optimized_matrix_mult(A, B, C_optimized, n);
    end = high_resolution_clock::now();
    double optimized_time = duration_cast<duration<double>>(end - start).count();
    double optimized_perf = complexity / (optimized_time * 1e6); // MFlops

    // Вывод результатов
    cout << "\nРезультаты производительности:" << endl;
    cout << "1. Наивная реализация:" << endl;
    cout << "   Время: " << naive_time << " сек" << endl;
    cout << "   Производительность: " << naive_perf << " MFlops" << endl;

    cout << "2. Реализация BLAS:" << endl;
    cout << "   Время: " << blas_time << " сек" << endl;
    cout << "   Производительность: " << blas_perf << " MFlops" << endl;

    cout << "3. Оптимизированная реализация:" << endl;
    cout << "   Время: " << optimized_time << " сек" << endl;
    cout << "   Производительность: " << optimized_perf << " MFlops" << endl;
    cout << "   Процент от производительности BLAS: " << (optimized_perf / blas_perf * 100) << "%" << endl;

    return 0;
}