package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import dictionaries.util.COL_DELIMITER
import dictionaries.util.LIST_DELIMITER
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
     *
     * key - имя перечисления,
     * val - возможные значения
     */
    private var enums: MutableMap<String, List<String>> = HashMap()

    /**
     * Список линейных перечислений
     *
     * key - имя перечисления,
     * val - имя связи в RDF которая задает порядок
     */
    private var linerEnums: MutableMap<String, String> = HashMap()

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
        val parser = CSVParserBuilder().withSeparator(COL_DELIMITER).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                // Проверяем, что enum содержит хоть одно значение
                require(row[1].split(LIST_DELIMITER).isNotEmpty()) { "Enum ${row[0]} не содержит значений." }

                // Добавляем в список enum и его значения
                enums[row[0]] = row[1].split(LIST_DELIMITER)

                // Если enum линейный
                if (row[2].toBoolean()) {
                    // Проверяем, что связь, задающая порядок, указана
                    require(row[3].isNotBlank()) { "Для линейного enum ${row[0]} не указана связь, задающая порядок." }

                    // Добавляем в список линейных enum
                    linerEnums[row[0]] = row[3]
                }
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Существует ли enum
     * @param name Имя enum
     * @return true - если существует, иначе - false
     */
    fun exist(name: String) = enums.containsKey(name)

    /**
     * Содержит ли enum указанное значение
     * @param name Имя enum
     * @param value Значение
     * @return true - если enum содержит такое значение, иначе - false
     */
    fun containsValue(name: String, value: String) = enums[name]?.contains(value) ?: false

    /**
     * Получить список всех значений enum
     * @param name Имя enum
     * @return Список всех значений
     */
    fun values(name: String) = enums[name]

    /**
     * Является ли enum линейным
     * @param name Имя enum
     * @return true - если является, иначе - false
     */
    fun isLiner(name: String) = linerEnums.containsKey(name)

    /**
     * Получить линейный предикат перечисления
     * @param name Имя enum
     * @return Линейный предикат перечисления, задающий порядок
     */
    fun linerPredicate(name: String) = linerEnums[name]
}