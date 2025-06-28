#include <algorithm>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <unordered_map>
#include <chrono>
#include <Windows.h>

using namespace std;

// Функция для настройки консоли Windows
void initConsole() {
    SetConsoleOutputCP(1251); // Устанавливаем кодировку Windows-1251 для русских букв
    SetConsoleCP(1251);
}

// Функция для создания карты символов (упрощенная версия для однобайтовых символов)
unordered_map<char, int> createCharMap(const string& word) {
    unordered_map<char, int> charMap;
    for (char c : word) {
        charMap[c]++;
    }
    return charMap;
}

int main() {
    initConsole(); // Инициализация консоли

    // Загрузка словаря
    cout << "Загрузка словаря..." << endl;
    vector<string> dictionary;
    ifstream in("nouns.txt");

    if (!in.is_open()) {
        cerr << "Ошибка: не удалось открыть файл словаря nouns.txt" << endl;
        system("pause");
        return 1;
    }

    string word;
    while (getline(in, word)) {
        if (!word.empty()) {
            dictionary.push_back(word);
        }
    }

    // Ввод слова для поиска
    string inputWord;
    cout << "Введите слово (в нижнем регистре): ";
    cin >> inputWord;

    // Создаем карту символов для входного слова
    auto inputMap = createCharMap(inputWord);

    // Поиск совпадений
    vector<string> matchedWords;
    auto searchStart = chrono::steady_clock::now();

    for (const auto& word : dictionary) {
        if (word.size() > inputWord.size()) continue;

        auto wordMap = createCharMap(word);
        bool canForm = true;

        for (const auto& pair : wordMap) {
            if (inputMap[pair.first] < pair.second) {
                canForm = false;
                break;
            }
        }

        if (canForm) {
            matchedWords.push_back(word);
        }
    }

    // Проверка времени выполнения
    auto searchEnd = chrono::steady_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(searchEnd - searchStart);

    if (duration > chrono::seconds(2)) {
        cout << "Предупреждение: время поиска превысило 2 секунды!" << endl;
    }

    // Сортировка результатов по убыванию длины
    sort(matchedWords.begin(), matchedWords.end(),
        [](const string& a, const string& b) {
            return a.size() > b.size() || (a.size() == b.size() && a < b);
        });

    // Вывод результатов
    cout << "\nНайдено " << matchedWords.size() << " слов:\n";
    for (const auto& word : matchedWords) {
        cout << word << endl;
    }

    cout << "\nВремя поиска: " << duration.count() << " мс" << endl;

    system("pause");
    return 0;
}