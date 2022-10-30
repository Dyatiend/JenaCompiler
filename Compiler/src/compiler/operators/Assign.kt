package compiler.operators

import compiler.Operator
import compiler.Value
import compiler.values.PropertyValue
import dictionaries.PropertiesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF
import util.JenaUtil.VAR_PREDICATE
import util.NamingManager

/**
 * Присваивание
 */
class Assign(args: List<Operator>) : BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(
            listOf(DataType.Object, DataType.Property, DataType.Literal),
            listOf(DataType.DecisionTreeVar, DataType.Object)
        )
    }

    override fun resultDataType(): DataType? {
        return null
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        var completedRules = ""

        if (args().size == 3) {
            // Получаем аргументы
            val arg0 = arg(0)
            val arg1 = arg(1)
            val arg2 = arg(2)

            // Проверям, что свойство не статическое
            require(PropertiesDictionary.isStatic((arg1 as PropertyValue).value)) {
                "Свойство не должно быть статическим"
            }

            // Проверяем, что присваиваемое значение статическое
            require(arg2 is Value || arg2 is GetPropertyValue) {
                "Нельзя присвоить динамическое значение"
            }

            // Компилируем аргументы
            val compiledArg0 = arg0.compile()
            val compiledArg1 = arg1.compile()
            val compiledArg2 = arg2.compile()

            // Передаем завершенные правила дальше
            completedRules += compiledArg0.completedRules +
                    compiledArg1.completedRules +
                    compiledArg2.completedRules

            // Для всех результатов компиляции
            compiledArg0.ruleHeads.forEach { head0 ->
                compiledArg1.ruleHeads.forEach { head1 ->
                    compiledArg2.ruleHeads.forEach { head2 ->
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
            val varName = (arg0 as Value).value

            // Компилируем аргументы
            val compiledArg0 = arg0.compile()
            val compiledArg1 = arg1.compile()

            // Передаем завершенные правила дальше
            completedRules += compiledArg0.completedRules +
                    compiledArg1.completedRules

            // Для всех результатов компиляции
            compiledArg0.ruleHeads.forEach { head0 ->
                compiledArg1.ruleHeads.forEach { head1 ->
                    // Собираем правила для аргументов
                    val head = head0 + head1

                    // Заполняем шаблон
                    var rule = DECISION_TREE_VAR_ASSIGN_PATTERN
                    rule = rule.replace("<ruleHead>", head)
                    rule = rule.replace("<newObj>", compiledArg1.value)
                    rule = rule.replace("<varPredicate>", JenaUtil.genLink(POAS_PREF, VAR_PREDICATE))
                    rule = rule.replace("<varName>", varName)

                    // Добавляем в результат
                    completedRules += rule
                }
            }
        }

        return CompilationResult("", emptyList(), completedRules)
    }

    companion object {

        /**
         * Шаблон правила присваивания значения свойства
         */
        private const val PROPERTY_ASSIGN_PATTERN = "[\n(<tmp0> <propName> <tmp1>)\n<ruleHead>\nequal(<subjName>, <tmp0>)\n->\ndrop(0)\n(<subjName> <propName> <value>)\n]\n"

        /**
         * Шаблон правила присваивания значения переменной дерева мысли
         */
        private const val DECISION_TREE_VAR_ASSIGN_PATTERN = "[\n<ruleHead>\n->\ndrop(0)\n(<newObj> <varPredicate> <varName>)\n]\n"
    }
}