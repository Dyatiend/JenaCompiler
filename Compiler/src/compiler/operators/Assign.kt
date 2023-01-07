package compiler.operators

import compiler.Literal
import compiler.Operator
import compiler.literals.DoubleLiteral
import compiler.literals.EnumLiteral
import compiler.literals.IntegerLiteral
import compiler.literals.PropertyLiteral
import compiler.util.CompilationResult
import dictionaries.EnumsDictionary
import dictionaries.PropertiesDictionary
import util.DataType
import util.JenaUtil
import util.JenaUtil.DECISION_TREE_VAR_PREDICATE
import util.JenaUtil.POAS_PREF
import util.NamingManager

/**
 * Присваивание
 */
class Assign(args: List<Operator>) : BaseOperator(args) {

    init {
        if (args.size == 3) {
            val arg2 = arg(2)

            val propertyName = (arg(1) as PropertyLiteral).value
            val newValueDataType = arg(2).resultDataType!!

            // Проверяем, что свойство не статическое
            require(PropertiesDictionary.isStatic(propertyName) == true) {
                "Свойство $propertyName не должно быть статическим"
            }
            require(PropertiesDictionary.dataType(propertyName) == newValueDataType) {
                "Тип данных значения не совпадает с типом данных свойства"
            }
            // Проверяем, что присваиваемое значение статическое
            require(arg2 is Literal || arg2 is GetPropertyValue) {
                "Нельзя присвоить динамическое значение"
            }
            if (arg2 is Literal) {
                when (arg2) {
                    is IntegerLiteral -> {
                        PropertiesDictionary.isValueInRange(propertyName, arg2.value.toInt())
                    }

                    is DoubleLiteral -> {
                        PropertiesDictionary.isValueInRange(propertyName, arg2.value.toDouble())
                    }

                    is EnumLiteral -> {
                        EnumsDictionary.containsValue(PropertiesDictionary.enumName(propertyName)!!, arg2.value)
                    }
                }
            }
        }
    }

    override val argsDataTypes = listOf(
        listOf(DataType.Object, DataType.Property, DataType.Integer),
        listOf(DataType.Object, DataType.Property, DataType.Double),
        listOf(DataType.Object, DataType.Property, DataType.Boolean),
        listOf(DataType.Object, DataType.Property, DataType.String),
        listOf(DataType.Object, DataType.Property, DataType.Enum),
        listOf(DataType.DecisionTreeVar, DataType.Object)
    )

    override val resultDataType = null

    override fun compile(): CompilationResult {
        // Объявляем переменные
        var completedRules = ""

        if (args.size == 3) {
            // Получаем аргументы
            val arg0 = arg(0)
            val arg1 = arg(1)
            val arg2 = arg(2)

            // Компилируем аргументы
            val compiledArg0 = arg0.compile()
            val compiledArg1 = arg1.compile()
            val compiledArg2 = arg2.compile()

            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules +
                    compiledArg2.rules

            // Для всех результатов компиляции
            compiledArg0.heads.forEach { head0 ->
                compiledArg1.heads.forEach { head1 ->
                    compiledArg2.heads.forEach { head2 ->
                        // Собираем правила для аргументов
                        val head = head0 + head1 + head2

                        // Заполняем шаблон
                        var rule = PROPERTY_ASSIGN_PATTERN
                        rule = rule.replace("<tmp0>", NamingManager.genVarName())
                        rule = rule.replace("<tmp1>", NamingManager.genVarName())

                        rule = rule.replace("<ruleHead>", head)
                        rule = rule.replace("<subjName>", compiledArg0.value)
                        rule = rule.replace("<propName>", compiledArg1.value)
                        rule = rule.replace("<value>", compiledArg2.value)

                        // Добавляем в результат
                        completedRules += rule
                    }
                }
            }
        } else {
            // Получаем аргументы
            val arg0 = arg(0)
            val arg1 = arg(1)

            // Получаем имя переменной
            val varName = (arg0 as Literal).value

            // Компилируем аргументы
            val compiledArg0 = arg0.compile()
            val compiledArg1 = arg1.compile()

            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules

            // Для всех результатов компиляции
            compiledArg0.heads.forEach { head0 ->
                compiledArg1.heads.forEach { head1 ->
                    // Собираем правила для аргументов
                    val head = head0 + head1

                    // Заполняем шаблон
                    var rule = DECISION_TREE_VAR_ASSIGN_PATTERN
                    rule = rule.replace("<ruleHead>", head)
                    rule = rule.replace("<newObj>", compiledArg1.value)
                    rule = rule.replace("<varPredicate>", JenaUtil.genLink(POAS_PREF, DECISION_TREE_VAR_PREDICATE))
                    rule = rule.replace("<varName>", varName)

                    // Добавляем в результат
                    completedRules += rule
                }
            }
        }

        return CompilationResult("", listOf(""), completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        return Assign(newArgs)
    }

    override fun clone(newArgs: List<Operator>): Operator {
        return Assign(newArgs)
    }

    companion object {

        /**
         * Шаблон правила присваивания значения свойства
         */
        private const val PROPERTY_ASSIGN_PATTERN =
            "[\n(<tmp0> <propName> <tmp1>)\n<ruleHead>\nequal(<subjName>, <tmp0>)\n->\ndrop(0)\n(<subjName> <propName> <value>)\n]\n"

        /**
         * Шаблон правила присваивания значения переменной дерева мысли
         */
        private const val DECISION_TREE_VAR_ASSIGN_PATTERN = "[\n<ruleHead>\n->\ndrop(0)\n(<newObj> <varPredicate> <varName>)\n]\n"
    }
}