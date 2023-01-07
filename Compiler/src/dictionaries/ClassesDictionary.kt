package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import compiler.Operator
import dictionaries.util.COLUMNS_SEPARATOR
import models.ClassModel
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Словарь классов
 */
object ClassesDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список классов
     */
    private val classes: MutableList<ClassModel> = mutableListOf()

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Инициализирует словарь данными
     * @param path Путь с фалу с данными для словаря
     */
    internal fun init(path: String) {
        // Очищаем старые значения
        classes.clear()

        // Создаем объекты
        val parser = CSVParserBuilder().withSeparator(COLUMNS_SEPARATOR).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                val name = row[0]
                val parent = row[1].ifBlank { null }
                val calcExpr = if (row[2].isNotBlank()) Operator.fromXMLString(row[2]) else null

                classes.add(
                    ClassModel(
                        name = name,
                        parent = get(parent),
                        calcExpr = calcExpr
                    )
                )
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Получить модель класса по имени
     * @param name Имя класса
     */
    fun get(name: String?) = classes.firstOrNull { it.name == name }

    /**
     * Существует ли класс
     * @param name Имя класса
     */
    fun exist(name: String) = classes.any { it.name == name }

    /**
     * Является ли класс родителем другого
     * @param child Ребенок
     * @param parent Родитель
     */
    fun isParentOf(child: String, parent: String): Boolean {
        val childModel = get(child)
        val parentModel = get(parent)
        if (!exist(child) || !exist(parent) || childModel?.parent == null) return false
        return if (childModel.parent == parentModel) true else isParentOf(childModel.parent.name, parentModel!!.name)
    }

    /**
     * Вычисляемый ли класс
     * @param name Имя класса
     */
    fun isCalculable(name: String) = get(name)?.calcExpr != null

    /**
     * Получить выражение для вычисления класса
     * @param name Имя класса
     * @return Выражение для вычисления
     */
    fun calcExpr(name: String) = get(name)?.calcExpr
}