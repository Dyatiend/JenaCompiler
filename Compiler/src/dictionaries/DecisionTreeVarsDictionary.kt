package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import dictionaries.util.COLUMNS_SEPARATOR
import models.DecisionTreeVarModel
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

// TODO: вычислять по дереву
/**
 * Словарь переменных дерева мысли
 */
object DecisionTreeVarsDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список переменных дерева мысли
     */
    private val decisionTreeVars: MutableList<DecisionTreeVarModel> = mutableListOf()

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
        val parser = CSVParserBuilder().withSeparator(COLUMNS_SEPARATOR).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                val name = row[0]
                val className = row[1]
                val classModel = ClassesDictionary.get(className)

                require(classModel != null) {
                    "Класс $className не объявлен в словаре."
                }

                decisionTreeVars.add(
                    DecisionTreeVarModel(
                        name = name,
                        classModel = classModel
                    )
                )
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Получить модель переменной по имени
     * @param name Имя переменной
     */
    private fun get(name: String) = decisionTreeVars.firstOrNull { it.name == name }

    fun exist(name: String) = false

    /**
     * Получить класс переменной дерева мысли
     * @param name Имя переменной
     * @return Имя класса переменной
     */
    fun getClass(name: String) = get(name)?.classModel
}