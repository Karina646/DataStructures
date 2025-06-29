using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.IO;
using DocumentFormat.OpenXml.Packaging;
using DocumentFormat.OpenXml.Wordprocessing;

namespace WordFinderTool
{
    
    // Класс с основной логикой программы
    class Application
    {
        static void Main(string[] args)
        {
            // Путь к документу (Война и мир)
            string documentPath = @"C:\Users\user\source\repos\TextSearchEngine\TextSearchEngine\voina-i-mir.docx";

            // Создаем экземпляр поискового инструмента
            var finder = new WordFinder();

            Console.WriteLine("Подготовка поискового индекса...");
            finder.Setup(documentPath);
            Console.WriteLine("Индекс подготовлен. Можно выполнять поиск.");

            // Основной цикл взаимодействия с пользователем
            while (true)
            {
                Console.Write("Введите текст для поиска (минимум 3 символа): ");
                string userQuery = Console.ReadLine();

                // Выход при пустом вводе
                if (string.IsNullOrWhiteSpace(userQuery))
                {
                    break;
                }

                try
                {
                    // Замер времени выполнения поиска
                    var timer = System.Diagnostics.Stopwatch.StartNew();
                    var searchResults = finder.FindMatches(userQuery);
                    timer.Stop();

                    // Вывод результатов и времени выполнения
                    Console.WriteLine($"Найдено ({timer.ElapsedMilliseconds} мс):");
                    foreach (var match in searchResults)
                    {
                        Console.WriteLine(match);
                    }
                }
                catch (ArgumentException error)
                {
                    // Обработка ошибки недопустимого запроса
                    Console.WriteLine(error.Message);
                }

                Console.WriteLine();  // Разделитель между запросами
            }
        }
    }
}