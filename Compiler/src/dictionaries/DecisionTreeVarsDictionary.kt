package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import dictionaries.util.COL_DELIMITER
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

// TODO: может быть вычислен
/**
 * Словарь переменных дерева мысли
 */
object DecisionTreeVarsDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Переменные дерева мысли
     *
     * key - имя переменной,
     * val - класс переменной
     */
    private var decisionTreeVars: MutableMap<String, String> = HashMap()

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Инициализирует словарь данными
     * @param path Путь с фалу с данными для словаря
     */
    internal fun init(path: String) {
        // Очищаем старые значения
        decisionTreeVars.clear()

        // Создаем объекты
        val parser = CSVParserBuilder().withSeparator(COL_DELIMITER).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                // Проверяем, что класс переменной не пустой и существует
                require(row[1].isNotBlank()) { "Не указан класс переменной ${row[0]}." }
                require(ClassesDictionary.exist(row[1])) { "Класс ${row[0]} не объявлен в словаре." }

                // Считываем классы переменных
                decisionTreeVars[row[0]] = row[1]
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Получить класс переменной дерева мысли
     * @param varName Имя переменной
     * @return Класс переменной
     */
    fun getClass(varName: String) = decisionTreeVars[varName]
}