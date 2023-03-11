package compiler

import compiler.literals.*
import compiler.operators.*
import compiler.util.ComparisonResult
import compiler.util.CompilationResult
import dictionaries.util.DictionariesUtil.generateAuxiliaryRules
import org.apache.commons.io.IOUtils
import org.w3c.dom.Node
import org.xml.sax.SAXException
import util.DataType
import util.JenaUtil
import util.NamingManager
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
    val isArgsCountUnlimited: Boolean
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
        // TODO: таблица переменных, вводимых операторами
        // TODO: валидация переменных, вводимых операторами
        // TODO?: оптимизация пауз?
        // TODO?: оптимизация правил? (удаление одинаковых строк)

        // Добавляем вспомогательные правила
        var rules = JenaUtil.AUXILIARY_RULES

        // Добавляем вспомогательные правила, сгенерированные по словарям
        rules += generateAuxiliaryRules()

        // Добавляем паузу
        rules += JenaUtil.PAUSE_MARK

        // Генерируем имена
        val skolemName = NamingManager.genVarName()
        val resPredicateName = if (resultDataType != null) NamingManager.genPredicateName() else ""

        // Упрощаем выражение
        val expr = semantic()

        // Если корневой оператор - булево значение
        if (expr is BooleanLiteral) {
            // Добавляем выражение, равное значению
            val body = expr.compileAsBody()

            // Генерируем правило и добавляем правило к остальным
            rules += JenaUtil.genBooleanRule(body, skolemName, resPredicateName)
        } else {
            // Компилируем оператор
            val result = expr.compile()

            // Добавляем скомпилированные правила в результат
            rules += result.rules

            // Для всех незаконченных правил
            result.bodies.forEach { body ->
                // Если есть незаконченное правило
                if (body.isNotEmpty() && resultDataType != null) {
                    // Генерируем правило и добавляем правило к остальным
                    rules += if (resultDataType == DataType.Boolean) {
                        JenaUtil.genBooleanRule(body, skolemName, resPredicateName)
                    } else {
                        JenaUtil.genRule(body, skolemName, resPredicateName, result.value)
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
                val xml = document.documentElement

                // Строим дерево
                return build(xml)
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
                val xml = document.documentElement

                // Строим дерево
                return build(xml)
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
            val children = mutableListOf<Operator>()
            for (i in 0 until node.childNodes.length) {
                val child = node.childNodes.item(i)
                if (child.nodeType == Node.ELEMENT_NODE) {
                    children.add(build(child))
                }
            }

            when (node.nodeName) {
                "Variable" -> {
                    return Variable(node.attributes.getNamedItem("name").nodeValue)
                }

                "DecisionTreeVar" -> {
                    return DecisionTreeVarLiteral(node.attributes.getNamedItem("name").nodeValue)
                }

                "Class" -> {
                    return ClassLiteral(node.attributes.getNamedItem("name").nodeValue)
                }

                "Object" -> {
                    return ObjectLiteral(node.attributes.getNamedItem("name").nodeValue)
                }

                "Property" -> {
                    return PropertyLiteral(node.attributes.getNamedItem("name").nodeValue)
                }

                "Relationship" -> {
                    return RelationshipLiteral(node.attributes.getNamedItem("name").nodeValue)
                }

                "ComparisonResult" -> {
                    return ComparisonResultLiteral(ComparisonResult.valueOf(node.attributes.getNamedItem("value").nodeValue)!!)
                }

                "String" -> {
                    return StringLiteral(node.attributes.getNamedItem("value").nodeValue)
                }

                "Boolean" -> {
                    return BooleanLiteral(node.attributes.getNamedItem("value").nodeValue.toBoolean())
                }

                "Integer" -> {
                    return IntegerLiteral(node.attributes.getNamedItem("value").nodeValue.toInt())
                }

                "Double" -> {
                    return DoubleLiteral(node.attributes.getNamedItem("value").nodeValue.toDouble())
                }

                "Enum" -> {
                    return EnumLiteral(
                        node.attributes.getNamedItem("value").nodeValue,
                        node.attributes.getNamedItem("owner").nodeValue
                    )
                }

                "AssignToDecisionTreeVar" -> {
                    return Assign(children)
                }

                "AssignToProperty" -> {
                    return Assign(children)
                }

                "CheckClass" -> {
                    return CheckClass(children)
                }

                "CheckPropertyValue" -> {
                    return CheckPropertyValue(children)
                }

                "CheckRelationship" -> {
                    return CheckRelationship(children)
                }

                "Compare" -> {
                    return if (node.attributes.getNamedItem("operator") == null) {
                        Compare(children)
                    } else {
                        val operator =
                            CompareWithComparisonOperator.ComparisonOperator.valueOf(node.attributes.getNamedItem("operator").nodeValue)!!
                        CompareWithComparisonOperator(children, operator)
                    }
                }

                "ExistenceQuantifier" -> {
                    return ExistenceQuantifier(children, node.attributes.getNamedItem("varName").nodeValue)
                }

                "ForAllQuantifier" -> {
                    return ForAllQuantifier(children, node.attributes.getNamedItem("varName").nodeValue)
                }

                "GetByCondition" -> {
                    return GetByCondition(children, node.attributes.getNamedItem("varName").nodeValue)
                }

                "GetByRelationship" -> {
                    return GetByRelationship(
                        children,
                        if (node.attributes.getNamedItem("varName") == null) {
                            null
                        } else {
                            node.attributes.getNamedItem("varName").nodeValue
                        }
                    )
                }

                "GetClass" -> {
                    return GetClass(children)
                }

                "GetExtreme" -> {
                    return GetExtreme(
                        children,
                        node.attributes.getNamedItem("varName").nodeValue,
                        node.attributes.getNamedItem("extremeVarName").nodeValue
                    )
                }

                "GetPropertyValue" -> {
                    return GetPropertyValue(children)
                }

                "LogicalAnd" -> {
                    return LogicalAnd(children)
                }

                "LogicalOr" -> {
                    return LogicalOr(children)
                }

                "LogicalNot" -> {
                    return LogicalNot(children)
                }
            }
            throw IllegalArgumentException("Неизвестный тип узла ${node.nodeName}.")
        }
    }
}