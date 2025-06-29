using DocumentFormat.OpenXml.Packaging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace WordFinderTool
{
    // Главный класс инструмента для поиска слов
    public class WordFinder
    {
        // Коллекция для хранения количества повторений слов (слово -> частота)
        private Dictionary<string, int> _termCounts;

        // Упорядоченный перечень слов по их встречаемости
        private List<KeyValuePair<string, int>> _orderedTerms;

        // Метод подготовки поискового инструмента
        public void Setup(string documentLocation)
        {
            // Извлечение текстового содержимого из DOCX
            string documentContent = ExtractContentFromDocument(documentLocation);

            // Анализ текста и создание индекса
            AnalyzeContent(documentContent);
        }

        // Метод извлечения текста из DOCX документа
        private string ExtractContentFromDocument(string documentLocation)
        {
            // Используем OpenXML для работы с документами Word
            using (WordprocessingDocument wordDoc = WordprocessingDocument.Open(documentLocation, false))
            {
                // Получаем основной раздел документа
                var mainSection = wordDoc.MainDocumentPart.Document.Body;
                // Возвращаем весь текст документа
                return mainSection.InnerText;
            }
        }

        // Метод анализа текста и создания поискового индекса
        private void AnalyzeContent(string content)
        {
            // Инициализация коллекции без учета регистра
            _termCounts = new Dictionary<string, int>(StringComparer.OrdinalIgnoreCase);

            // Разделение текста на отдельные слова
            var extractedTerms = Regex.Split(content, @"\W+")
                // Исключаем слишком короткие слова (меньше 3 символов)
                .Where(term => term.Length >= 3)
                // Приводим к единому регистру
                .Select(term => term.ToLower());

            // Подсчет повторений каждого слова
            foreach (var term in extractedTerms)
            {
                // Если слово уже присутствует в коллекции, увеличиваем счетчик
                if (_termCounts.ContainsKey(term))
                {
                    _termCounts[term]++;
                }
                else
                {
                    // Иначе добавляем новую запись
                    _termCounts[term] = 1;
                }
            }

            // Сортировка слов по частоте встречаемости
            _orderedTerms = _termCounts
                .OrderByDescending(entry => entry.Value)  // Сначала по частоте (по убыванию)
                .ThenBy(entry => entry.Key)               // Затем по алфавиту
                .ToList();                               // Преобразуем в список
        }

        // Метод выполнения поиска
        public IEnumerable<string> FindMatches(string searchTerm, int maxResults = 20)
        {
            // Проверка длины поискового запроса
            if (searchTerm.Length < 3)
            {
                throw new ArgumentException("Минимальная длина запроса - 3 символа");
            }

            // Приводим запрос к нижнему регистру
            string normalizedTerm = searchTerm.ToLower();

            // Поиск совпадений в индексе
            return _orderedTerms
                .Where(entry => entry.Key.Contains(normalizedTerm))  // Ищем вхождения подстроки
                .Take(maxResults)                                   // Ограничиваем количество результатов
                .Select(entry => entry.Key);                       // Выбираем только слова
        }
    }
}
