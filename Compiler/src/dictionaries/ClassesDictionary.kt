package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import compiler.Operator
import dictionaries.util.COL_DELIMITER
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Словарь классов
 */
object ClassesDictionary {

    // +++++++++++++++++++++++++++++++++ Константы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Имя переменной в выражении для вычисления класса
     */
    const val CALCULATION_VAR_NAME = "obj"

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список классов
     *
     * key - класс,
     * val - предок
     */
    private var classes: MutableMap<String, String?> = HashMap()

    /**
     * Вычисляемые классы
     *
     * Класс вычисляется путем вычисления boolean выражения над объектом
     *
     * key - класс,
     * val - выражение для вычисления
     */
    private var calculations: MutableMap<String, Operator> = HashMap()

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Инициализирует словарь данными
     * @param path Путь с фалу с данными для словаря
     */
    internal fun init(path: String) {
        // Очищаем старые значения
        classes.clear()
        calculations.clear()

        // Создаем объекты
        val parser = CSVParserBuilder().withSeparator(COL_DELIMITER).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                // Считываем классы
                classes[row[0]] = row[1].ifBlank { null }

                // Если класс вычисляемый
                if (row[2].isNotBlank()) {
                    // Считываем XML и стоки с собираем дерево
                    val calculation = Operator.fromXMLString(row[2])

                    // Проверяем, что дерево построено
                    require(calculation != null) { "Некорректное выражения для вычисления класса ${row[0]}." }

                    // Записываем
                    calculations[row[0]] = calculation
                }
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Существует ли класс
     * @param className Имя класса
     * @return true - если существует, иначе - false
     */
    fun exist(className: String) = classes.containsKey(className)

    /**
     * Является ли класс родителем другого
     * @param parent Родитель
     * @param child Ребенок
     * @return true - если является, иначе - false
     */
    fun isParentOf(parent: String, child: String): Boolean {
        if (!exist(parent) || !exist(child)) return false
        return if (classes[child] == parent) true else isParentOf(parent, classes[child]!!)
    }

    /**
     * Вычисляемый ли класс
     * @param className Имя класса
     * @return true - если вычисляемый, иначе - false
     */
    fun isCalculable(className: String) = calculations.containsKey(className)

    /**
     * Как вычислить класс
     * @param className Имя класса
     * @return Выражение для вычисления
     */
    fun calculation(className: String) = calculations[className]
}