package compiler

import compiler.operators.*
import compiler.values.*
import dictionaries.RelationshipsDictionary
import org.w3c.dom.Node
import org.xml.sax.SAXException
import util.*
import java.io.IOException
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Оператор
 */
interface Operator {

    /**
     * Список аргументов
     * @return Список аргументов
     */
    fun args(): List<Operator> = ArrayList()

    /**
     * Установить аргументы
     * @param args Аргументы
     */
    fun setArgs(args: List<Operator>) {}

    /**
     * Получить аргумент
     * @param index Индекс аргумента
     * @return Аргумент
     */
    fun arg(index: Int): Operator = args()[index]

    /**
     * Список типов данных аргументов
     * @return Список типов данных аргументов
     */
    fun argsDataTypes(): List<List<DataType>>

    /**
     * Является ли количество аргументов бесконечным
     * @return true - если является, иначе - false
     */
    val isArgsCountUnlimited: Boolean
        get() = false

    /**
     * Тип данных оператора
     * @return Тип данных оператора
     */
    fun resultDataType(): DataType?

    /**
     * Скомпилировать выражение
     * @return Правила для вычисления выражения и имя предиката для чтения результата (если есть)
     */
    fun compileExpression(): CompilationResult {
        // TODO: валидация переменных, вводимых операторами
        // TODO: таблица переменных, вводимых операторами и их валидация
        // TODO?: оптимизация пауз?
        // TODO?: оптимизация правил? (удаление одинаковых строк)

        // Добавляем пустой триплет в граф, чтобы он никогда небыл пустым
        var rules = JenaUtil.genBooleanRule("", NamingManager.genVarName(), NamingManager.genPredicateName())

        // Добавляем вспомогательные правила, если нужно
        rules += if (RelationshipsDictionary.isLinerScaleUsed())
            RelationshipsDictionary.auxiliaryLinerScaleRules()
        else
            ""

        // Добавляем паузу
        rules += JenaUtil.PAUSE_MARK

        // Генерируем имена
        val skolemName = NamingManager.genVarName()
        val resPredicateName = NamingManager.genPredicateName()

        // Упрощаем выражение
        val expr = doSemantic()

        // Если корневой оператор - булево значение
        if (expr is BooleanValue) {
            // Добавляем выражение, равное значению
            val head = if (expr.value.toBoolean()) {
                JenaUtil.genEqualPrim("1", "1")
            } else {
                JenaUtil.genEqualPrim("0", "1")
            }

            // Генерируем правило и добавляем правило к остальным
            rules += JenaUtil.genBooleanRule(head, skolemName, resPredicateName)
        } else {
            // Компилируем оператор
            val result = expr.compile()

            // Добавляем скомпилированныее правила в результат
            rules += result.completedRules

            // Для всех незаконченных правил
            result.ruleHeads.forEach { head ->
                // Если есть незаконченное правило
                if (head.isNotEmpty() && resultDataType() != null) {
                    // Генерируем правило и добавляем правило к остальным
                    rules += if (resultDataType() == DataType.Boolean) {
                        JenaUtil.genBooleanRule(head, skolemName, resPredicateName)
                    } else {
                        JenaUtil.genRule(head, skolemName, resPredicateName, result.value)
                    }
                }
            }
        }

        return CompilationResult(resPredicateName, listOf(""), rules)
    }

    /**
     * Скомпилировать оператор
     * @return Список правил для вычисления выражения, части правил для проверки и имя предикатов для чтения результата (если есть)
     */
    fun compile(): CompilationResult

    /**
     * Создает копию объекта
     * @return Копия
     */
    fun clone(): Operator

    /**
     * Семантический анализ дерева
     */
    fun doSemantic(): Operator {
//        fillVarsTable() TODO
        return simplify(false)
    }

    /**
     * Упрощает выражение, удаляя из него отрицания
     * @return Упрощенное выражение
     */
    private fun simplify(isNegative: Boolean): Operator {
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
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this.isNegative = true
                    this
                }
                is ExistenceQuantifier -> {
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this.isNegative = true
                    this
                }
                is CompareWithComparisonOperator -> {
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this.isNegative = true
                    this
                }
                is CheckRelationship -> {
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this.isNegative = true
                    this
                }
                is CheckPropertyValue -> {
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this.isNegative = true
                    this
                }
                is CheckClass -> {
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this.isNegative = true
                    this
                }
                is BooleanValue -> {
                    value = (!value.toBoolean()).toString()
                    this
                }

                else -> {
                    throw IllegalStateException("Отрицание не над bool оператором")
                }
            }
        } else {
            return when (this) {
                is LogicalNot -> {
                    arg(0).simplify(true)
                }

                else -> {
                    val newArgs = ArrayList<Operator>()

                    args().forEach { arg ->
                        newArgs.add(arg.simplify(false))
                    }

                    setArgs(newArgs)
                    this
                }
            }
        }
    }

    /**
     * Заполняет таблицу переменных
     */
    private fun fillVarsTable() {
        TODO()
    }

    companion object {

        /**
         * Таблица переменных
         * key - Имя
         * val - Список известных классов
         */
        val varsTable: MutableMap<String, MutableList<String>> = HashMap()

        /**
         * Создает дерево из XML файла
         * @param path Путь к файлу
         * @return Дерево выражения
         */
        fun fromXML(path: String?): Operator? {
            try {
                // Создается билдер дерева
                val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                // Создается дерево DOM документа из файла
                val document = documentBuilder.parse(path)

                // Получаем корневой элемент
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
                            throw IllegalAccessException("Выражение должно иметь один корневой узел")
                        }
                    }
                }
                if (root == null) {
                    throw IllegalAccessException("Не найден корневой узел выражения")
                }

                // Строим дерево
                return build(root)
            } catch (ex: ParserConfigurationException) {
                ex.printStackTrace(System.out)
            } catch (ex: IOException) {
                ex.printStackTrace(System.out)
            } catch (ex: SAXException) {
                ex.printStackTrace(System.out)
            }
            return null
        }

        /**
         * Создает оператор из узла XML
         * @param node XML узел
         * @return Оператор
         */
        private fun build(node: Node): Operator {
            require(node.nodeName == "block") { "Неподходящий тип узла" }

            when (node.attributes.getNamedItem("type").nodeValue) {
                "object" -> {
                    val name = node.firstChild.textContent
                    return ObjectValue(name)
                }

                "variable" -> {
                    val name = node.firstChild.textContent
                    return Variable(name)
                }

                "class" -> {
                    val name = node.firstChild.textContent
                    return ClassValue(name)
                }

                "property" -> {
                    val name = node.firstChild.textContent
                    return PropertyValue(name)
                }

                "relationship" -> {
                    val name = node.firstChild.textContent
                    return RelationshipValue(name)
                }

                "boolean" -> {
                    val value = node.firstChild.textContent
                    return BooleanValue(value == "TRUE")
                }

                "integer" -> {
                    val value = node.firstChild.textContent
                    return IntegerValue(value.toInt())
                }

                "double" -> {
                    val value = node.firstChild.textContent
                    return DoubleValue(value.toDouble())
                }

                "string" -> {
                    val value = node.firstChild.textContent
                    return StringValue(value)
                }

                "comparison_result" -> {
                    val value = node.firstChild.textContent
                    return ComparisonResultValue(ComparisonResult.valueOf(value.uppercase(Locale.getDefault())))
                }

                "ref_to_decision_tree_var" -> {
                    val name = node.firstChild.textContent
                    return DecisionTreeVarValue(name)
                }

                "get_class" -> {
                    return GetClass(listOf(build(node.firstChild.firstChild)))
                }

                "get_property_value" -> {
                    return GetPropertyValue(listOf(
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
            throw IllegalArgumentException("Неизвестный тип узла")
        }
    }
}