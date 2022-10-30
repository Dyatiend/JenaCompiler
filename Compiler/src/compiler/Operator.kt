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
        var rules = JenaUtil.genBooleanRule("", NamingManager.genVarName(), NamingManager.genPredName())

        // Добавляем вспомогательные правила, если нужно
        rules += if (RelationshipsDictionary.isLinerScaleUsed())
            RelationshipsDictionary.auxiliaryLinerScaleRules()
        else
            ""

        // Добавляем паузу
        rules += JenaUtil.PAUSE_MARK

        // Компилируем оператор
        val result = compile()

        // Добавляем скомпилированныее правила в результат
        rules += result.completedRules

        // Генерируем имена
        val skolemName = NamingManager.genVarName()
        val resPredName = NamingManager.genPredName()

        // Для всех незаконченных правил
        result.ruleHeads.forEach { head ->
            // Если есть незаконченное правило
            if (head.isNotEmpty() && resultDataType() != null) {
                // Генерируем правило и добавляем правило к остальным
                rules += if (resultDataType() == DataType.Boolean) {
                    JenaUtil.genBooleanRule(head, skolemName, resPredName)
                } else {
                    JenaUtil.genRule(head, skolemName, resPredName, result.value)
                }
            }
        }

        return CompilationResult(resPredName, emptyList(), rules)
    }

    /**
     * Скомпилировать оператор
     * @return Список правил для вычисления выражения, части правил для проверки и имя предикатов для чтения результата (если есть)
     */
    fun compile(): CompilationResult

    companion object {

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
        private fun build(node: Node): Operator? {
            var operator: Operator
            if (node.nodeName == "block") {
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
                        val `val` = node.firstChild.textContent
                        return BooleanValue(`val` == "TRUE")
                    }

                    "integer" -> {
                        val `val` = node.firstChild.textContent
                        return IntegerValue(Integer.valueOf(`val`))
                    }

                    "double" -> {
                        val `val` = node.firstChild.textContent
                        return DoubleValue(java.lang.Double.valueOf(`val`))
                    }

                    "string" -> {
                        val `val` = node.firstChild.textContent
                        return StringValue(`val`)
                    }

                    "comparison_result" -> {
                        val `val` = node.firstChild.textContent
                        return ComparisonResultValue(ComparisonResult.valueOf(`val`.uppercase(Locale.getDefault())))
                    }

                    "ref_to_decision_tree_var" -> {
                        val name = node.firstChild.textContent
                        return DecisionTreeVarValue(name)
                    }

                    "get_class" -> {
                        return GetClass(java.util.List.of(build(node.firstChild.firstChild)))
                    }

                    "get_property_value" -> {
                        return GetPropertyValue(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "get_relationship_object" -> {
                        return GetByRelationship(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "get_condition_object" -> {
                        return GetByCondition(java.util.List.of(
                                build(node.lastChild.firstChild)),
                                node.firstChild.textContent
                        )
                    }

                    "get_extr_object_condition_and_relation" -> {
                        // TODO
                    }

                    "assign_value_to_property" -> {
                        return Assign(java.util.List.of(
                                build(node.childNodes.item(0).firstChild),
                                build(node.childNodes.item(1).firstChild),
                                build(node.childNodes.item(2).firstChild)
                        ))
                    }

                    "assign_value_to_variable_decision_tree" -> {
                        return Assign(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "check_object_class" -> {
                        return CheckClass(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "check_value_of_property" -> {
                        return CheckPropertyValue(java.util.List.of(
                                build(node.childNodes.item(0).firstChild),
                                build(node.childNodes.item(1).firstChild),
                                build(node.childNodes.item(2).firstChild)
                        ))
                    }

                    "check_relationship" -> {
                        val args = ArrayList<Operator?>()
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
                        return LogicalAnd(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "or" -> {
                        return LogicalOr(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "not" -> {
                        return LogicalNot(java.util.List.of(
                                build(node.firstChild.firstChild)
                        ))
                    }

                    "comparison" -> {
                        return CompareWithComparisonOperator(java.util.List.of(
                                build(node.childNodes.item(1).firstChild),
                                build(node.childNodes.item(2).firstChild)),
                                CompareWithComparisonOperator.ComparisonOperator.valueOf(node.childNodes.item(0).textContent)
                        )
                    }

                    "three_digit_comparison" -> {
                        return Compare(java.util.List.of(
                                build(node.firstChild.firstChild),
                                build(node.lastChild.firstChild)
                        ))
                    }

                    "quantifier_of_existence" -> {
                        return ExistenceQuantifier(java.util.List.of(
                                build(node.lastChild.firstChild)),
                                node.firstChild.textContent
                        )
                    }

                    "quantifier_of_generality" -> {
                        return ForAllQuantifier(java.util.List.of(
                                build(node.childNodes.item(1).firstChild),
                                build(node.childNodes.item(2).firstChild)),
                                node.childNodes.item(0).textContent
                        )
                    }
                }
            }
            return null
        }
    }
}