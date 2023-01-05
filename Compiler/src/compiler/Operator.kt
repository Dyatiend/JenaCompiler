package compiler

import compiler.literals.*
import compiler.operators.*
import compiler.util.ComparisonResult
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import compiler.util.NamingManager
import dictionaries.RelationshipsDictionary
import org.apache.commons.io.IOUtils
import org.w3c.dom.Node
import org.xml.sax.SAXException
import util.DataType
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Оператор
 */
interface Operator {

    /**
     * Список аргументов
     */
    val args: List<Operator>

    /**
     * Получить аргумент
     * @param index Индекс аргумента
     * @return Аргумент
     */
    fun arg(index: Int) = args[index]

    /**
     * Список типов данных аргументов
     */
    val argsDataTypes: List<List<DataType>>

    /**
     * Является ли количество аргументов бесконечным
     */
    val isArgsCountUnlimited
        get() = false

    /**
     * Тип данных оператора
     */
    val resultDataType: DataType?

    /**
     * Создает копию объекта
     * @return Копия
     */
    fun clone(): Operator

    /**
     * Создает копию объекта с новыми аргументами
     * @param newArgs Новые аргументы
     * @return Копия
     */
    fun clone(newArgs: List<Operator>): Operator

    /**
     * Скомпилировать оператор
     * @return Результат компиляции
     * @see CompilationResult
     */
    fun compile(): CompilationResult

    /**
     * Скомпилировать выражение
     * @return Результат компиляции
     * @see CompilationResult
     */
    fun compileExpression(): CompilationResult {
        // TODO: валидация переменных, вводимых операторами
        // TODO: таблица переменных, вводимых операторами и их валидация
        // TODO?: оптимизация пауз?
        // TODO?: оптимизация правил? (удаление одинаковых строк)

        // Добавляем вспомогательные правила
        var rules = JenaUtil.AUXILIARY_RULES

        // Добавляем вспомогательные правила библиотеки
        rules += RelationshipsDictionary.auxiliaryLibraryRules

        // Добавляем паузу
        rules += JenaUtil.PAUSE_MARK

        // Генерируем имена
        val skolemName = NamingManager.genVarName()
        val resPredicateName = NamingManager.genPredicateName()

        // Упрощаем выражение
        val expr = semantic()

        // Если корневой оператор - булево значение
        if (expr is BooleanLiteral) {
            // Добавляем выражение, равное значению
            val head = expr.compileAsHead()

            // Генерируем правило и добавляем правило к остальным
            rules += JenaUtil.genBooleanRule(head, skolemName, resPredicateName)
        } else {
            // Компилируем оператор
            val result = expr.compile()

            // Добавляем скомпилированные правила в результат
            rules += result.rules

            // Для всех незаконченных правил
            result.heads.forEach { head ->
                // Если есть незаконченное правило
                if (head.isNotEmpty() && resultDataType != null) {
                    // Генерируем правило и добавляем правило к остальным
                    rules += if (resultDataType == DataType.Boolean) {
                        JenaUtil.genBooleanRule(head, skolemName, resPredicateName)
                    } else {
                        JenaUtil.genRule(head, skolemName, resPredicateName, result.value)
                    }
                }
            }
        }

        return CompilationResult(value = resPredicateName, rules = rules)
    }

    /**
     * Семантический анализ дерева
     */
    fun semantic(): Operator {
        val result = simplify()
        result.fillVarsTable()
        return result
    }

    /**
     * Упрощает выражение, удаляя из него отрицания
     * @param isNegative Находится ли текущий оператор под отрицанием
     * @return Упрощенное выражение
     */
    private fun simplify(isNegative: Boolean = false): Operator {
        if (isNegative) {
            return when (this) {
                is LogicalNot -> {
                    arg(0).simplify(false)
                }
                is LogicalOr -> {
                    LogicalAnd(listOf(arg(0).simplify(true), arg(1).simplify(true)))
                }
                is LogicalAnd -> {
                    LogicalOr(listOf(arg(0).simplify(true), arg(1).simplify(true)))
                }
                is ForAllQuantifier -> {
                    val newArgs = args.map { arg -> arg.simplify() }

                    val res = clone(newArgs) as ForAllQuantifier
                    res.isNegative = true

                    res
                }
                is ExistenceQuantifier -> {
                    val newArgs = args.map { arg -> arg.simplify() }

                    val res = clone(newArgs) as ExistenceQuantifier
                    res.isNegative = true

                    res
                }
                is CompareWithComparisonOperator -> {
                    val newArgs = args.map { arg -> arg.simplify() }

                    val res = clone(newArgs) as CompareWithComparisonOperator
                    res.isNegative = true

                    res
                }
                is CheckRelationship -> {
                    val newArgs = args.map { arg -> arg.simplify() }

                    val res = clone(newArgs) as CheckRelationship
                    res.isNegative = true

                    res
                }
                is CheckPropertyValue -> {
                    val newArgs = args.map { arg -> arg.simplify() }

                    val res = clone(newArgs) as CheckPropertyValue
                    res.isNegative = true

                    res
                }
                is CheckClass -> {
                    val newArgs = args.map { arg -> arg.simplify() }

                    val res = clone(newArgs) as CheckClass
                    res.isNegative = true

                    res
                }
                is BooleanLiteral -> {
                    BooleanLiteral(!value.toBoolean())
                }
                else -> {
                    throw IllegalStateException("Отрицание типа $resultDataType невозможно.")
                }
            }
        } else {
            return when (this) {
                is LogicalNot -> {
                    arg(0).simplify(true)
                }
                else -> {
                    val newArgs = args.map { arg -> arg.simplify() }
                    clone(newArgs)
                }
            }
        }
    }

    /**
     * Заполняет таблицу переменных
     */
    private fun fillVarsTable() {
        // TODO
    }

    companion object {

        /**
         * Изменяемая таблица переменных
         *
         * key - Имя переменной
         *
         * val - Список известных классов переменной
         */
        private val mVarsTable: MutableMap<String, MutableList<String>> = HashMap()

        /**
         * Таблица переменных
         *
         * key - Имя переменной
         *
         * val - Список известных классов переменной
         */
        val varsTable: Map<String, List<String>>
            get() = mVarsTable

        /**
         * Создает выражение из строки с XML
         * @param str Строка с XML
         * @return Выражение
         */
        fun fromXMLString(str: String): Operator? {
            try {
                // Создаем DocumentBuilder
                val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                // Создаем DOM документ из строки
                val document = documentBuilder.parse(IOUtils.toInputStream(str, StandardCharsets.UTF_8))

                // Получаем корневой элемент документа
                val xml: Node = document.documentElement

                // Корень выражения
                var root: Node? = null

                // Ищем корень
                val childNodes = xml.childNodes
                for (i in 0 until childNodes.length) {
                    val child = childNodes.item(i)
                    if (child.nodeType == Node.ELEMENT_NODE) {
                        root = if (root == null) {
                            child
                        } else {
                            throw IllegalAccessException("Выражение должно иметь один корневой узел.")
                        }
                    }
                }
                if (root == null) {
                    throw IllegalAccessException("Не найден корневой узел выражения.")
                }

                // Строим дерево
                return build(root)
            } catch (ex: ParserConfigurationException) {
                ex.printStackTrace()
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: SAXException) {
                ex.printStackTrace()
            }
            return null
        }

        /**
         * Создает выражение из XML файла
         * @param path Путь к файлу
         * @return Выражение
         */
        fun fromXMLFile(path: String): Operator? {
            try {
                // Создаем DocumentBuilder
                val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                // Создаем DOM документ из файла
                val document = documentBuilder.parse(path)

                // Получаем корневой элемент документа
                val xml: Node = document.documentElement

                // Корень выражения
                var root: Node? = null

                // Ищем корень
                val childNodes = xml.childNodes
                for (i in 0 until childNodes.length) {
                    val child = childNodes.item(i)
                    if (child.nodeType == Node.ELEMENT_NODE) {
                        root = if (root == null) {
                            child
                        } else {
                            throw IllegalAccessException("Выражение должно иметь один корневой узел.")
                        }
                    }
                }
                if (root == null) {
                    throw IllegalAccessException("Не найден корневой узел выражения.")
                }

                // Строим дерево
                return build(root)
            } catch (ex: ParserConfigurationException) {
                ex.printStackTrace()
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: SAXException) {
                ex.printStackTrace()
            }
            return null
        }

        /**
         * Создает оператор из узла XML
         * @param node XML узел
         * @return Оператор
         */
        private fun build(node: Node): Operator {
            require(node.nodeName == "block") { "Некорректный тип узла." }

            // TODO: синхронизировать с некитом
            when (node.attributes.getNamedItem("type").nodeValue) {
                "object" -> {
                    return ObjectLiteral(node.firstChild.textContent)
                }
                "variable" -> {
                    return Variable(node.firstChild.textContent)
                }
                "class" -> {
                    return ClassLiteral(node.firstChild.textContent)
                }
                "property" -> {
                    return PropertyLiteral(node.firstChild.textContent)
                }
                "relationship" -> {
                    return RelationshipLiteral(node.firstChild.textContent)
                }
                "boolean" -> {
                    return BooleanLiteral(node.firstChild.textContent == "TRUE")
                }
                "integer" -> {
                    return IntegerLiteral(node.firstChild.textContent.toInt())
                }

                "double" -> {
                    return DoubleLiteral(node.firstChild.textContent.toDouble())
                }

                "string" -> {
                    return StringLiteral(node.firstChild.textContent)
                }

                "comparison_result" -> {
                    return ComparisonResultLiteral(ComparisonResult.valueOf(node.firstChild.textContent))
                }

                "enum" -> {
                    TODO()
                }

                "ref_to_decision_tree_var" -> {
                    return DecisionTreeVarLiteral(node.firstChild.textContent)
                }

                "get_class" -> {
                    return GetClass(listOf(build(node.firstChild.firstChild)))
                }

                "get_property_value" -> {
                    return GetPropertyValue(
                        listOf(
                            build(node.firstChild.firstChild),
                            build(node.lastChild.firstChild)
                    ))
                }
                "get_relationship_object" -> {
                    return GetByRelationship(listOf(
                        build(node.firstChild.firstChild),
                        build(node.lastChild.firstChild)
                    ))
                }
                "get_condition_object" -> {
                    return GetByCondition(listOf(
                        build(node.lastChild.firstChild)),
                        node.firstChild.textContent
                    )
                }
                "get_extr_object" -> {
                    return GetExtreme(listOf(
                        build(node.childNodes.item(1).firstChild),
                        build(node.childNodes.item(3).firstChild)),
                        node.childNodes.item(0).textContent,
                        node.childNodes.item(2).textContent
                    )
                }
                "assign_value_to_property" -> {
                    return Assign(listOf(
                        build(node.childNodes.item(0).firstChild),
                        build(node.childNodes.item(1).firstChild),
                        build(node.childNodes.item(2).firstChild)
                    ))
                }
                "assign_value_to_variable_decision_tree" -> {
                    return Assign(listOf(
                        build(node.firstChild.firstChild),
                        build(node.lastChild.firstChild)
                    ))
                }
                "check_object_class" -> {
                    return CheckClass(listOf(
                        build(node.firstChild.firstChild),
                        build(node.lastChild.firstChild)
                    ))
                }
                "check_value_of_property" -> {
                    return CheckPropertyValue(
                        listOf(
                            build(node.childNodes.item(0).firstChild),
                            build(node.childNodes.item(1).firstChild),
                            build(node.childNodes.item(2).firstChild)
                        )
                    )
                }
                "check_relationship" -> {
                    val args = ArrayList<Operator>()
                    val childNodes = node.childNodes
                    for (i in 0 until childNodes.length) {
                        val child = childNodes.item(i)
                        if (child.nodeType == Node.ELEMENT_NODE && child.nodeName != "mutation") {
                            args.add(build(child.firstChild))
                        }
                    }
                    val tmp = args[0]
                    args[0] = args[1]
                    args[1] = tmp
                    return CheckRelationship(args)
                }
                "and" -> {
                    return LogicalAnd(listOf(
                        build(node.firstChild.firstChild),
                        build(node.lastChild.firstChild)
                    ))
                }
                "or" -> {
                    return LogicalOr(listOf(
                        build(node.firstChild.firstChild),
                        build(node.lastChild.firstChild)
                    ))
                }
                "not" -> {
                    return LogicalNot(listOf(
                        build(node.firstChild.firstChild)
                    ))
                }
                "comparison" -> {
                    return CompareWithComparisonOperator(listOf(
                        build(node.childNodes.item(1).firstChild),
                        build(node.childNodes.item(2).firstChild)),
                        CompareWithComparisonOperator.ComparisonOperator.valueOf(node.childNodes.item(0).textContent)
                    )
                }
                "three_digit_comparison" -> {
                    return Compare(listOf(
                        build(node.firstChild.firstChild),
                        build(node.lastChild.firstChild)
                    ))
                }
                "quantifier_of_existence" -> {
                    return ExistenceQuantifier(listOf(
                        build(node.lastChild.firstChild)),
                        node.firstChild.textContent
                    )
                }
                "quantifier_of_generality" -> {
                    return ForAllQuantifier(listOf(
                        build(node.childNodes.item(1).firstChild),
                        build(node.childNodes.item(2).firstChild)),
                        node.childNodes.item(0).textContent
                    )
                }
            }
            throw IllegalArgumentException("Неизвестный тип узла ${node.attributes.getNamedItem("type").nodeValue}.")
        }
    }
}