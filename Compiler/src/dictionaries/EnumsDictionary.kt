package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import dictionaries.util.COLUMNS_SEPARATOR
import dictionaries.util.LIST_ITEMS_SEPARATOR
import models.EnumModel
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Словарь перечислений
 */
object EnumsDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список перечислений
     */
    private val enums: MutableList<EnumModel> = mutableListOf()

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Инициализирует словарь данными
     * @param path Путь с фалу с данными для словаря
     */
    internal fun init(path: String) {
        // Очищаем старые значения
        enums.clear()

        // Создаем объекты
        val parser = CSVParserBuilder().withSeparator(COLUMNS_SEPARATOR).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                val name = row[0]
                val values = row[1].split(LIST_ITEMS_SEPARATOR).filter { it.isNotBlank() }
                val isLiner = row[3].toBoolean()
                val linerPredicate = row[4].ifBlank { null }

                require(!isLiner || linerPredicate != null) {
                    "Для линейного перечисления $name не указан линейный предикат."
                }

                enums.add(
                    EnumModel(
                        name = name,
                        values = values,
                        linerPredicate = linerPredicate
                    )
                )
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Получить модель перечисления по имени
     * @param name Имя перечисления
     */
    private fun get(name: String) = enums.firstOrNull { it.name == name }

    /**
     * Существует ли перечисление
     * @param name Имя перечисления
     */
    fun exist(name: String) = enums.any { it.name == name }

    /**
     * Содержит ли перечисление указанное значение
     * @param name Имя перечисления
     * @param value Значение
     */
    fun containsValue(name: String, value: String) = get(name)?.containsValue(value)

    /**
     * Получить список всех значений перечисления
     * @param name Имя перечисления
     * @return Список всех значений
     */
    fun values(name: String) = get(name)?.values

    /**
     * Является ли перечисление линейным
     * @param name Имя перечисления
     */
    fun isLiner(name: String) = get(name)?.linerPredicate != null

    /**
     * Получить линейный предикат перечисления
     * @param name Имя перечисления
     * @return Линейный предикат перечисления, задающий порядок
     */
    fun linerPredicate(name: String) = get(name)?.linerPredicate
}