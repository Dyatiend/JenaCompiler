package compiler.operators

import compiler.Operator
import compiler.Value
import compiler.values.PropertyValue
import dictionaries.PropertiesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF
import util.JenaUtil.VAR_PRED
import util.NamingManager

/**
 * Присваивание
 */
class Assign(args: List<Operator>) : BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        val result: MutableList<List<DataType>> = ArrayList()
        result.add(listOf(DataType.Object, DataType.Property, DataType.Literal))
        result.add(listOf(DataType.DecisionTreeVar, DataType.Object))
        return result
    }

    override fun resultDataType(): DataType? {
        return null
    }

    override fun compile(): List<CompilationResult> {
        val result: MutableList<CompilationResult> = ArrayList()

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

            // Для всех результатов компиляции
            compiledArg0.forEach { argRes0 ->
                compiledArg1.forEach { argRes1 ->
                    compiledArg2.forEach { argRes2 ->
                        // Собираем правила для аргументов
                        val head = argRes0.ruleHead + argRes1.ruleHead + argRes2.ruleHead

                        // Заполняем шаблон
                        var rule = PROPERTY_ASSIGN_PATTERN
                        rule = rule.replace("<tmp0>", NamingManager.genVarName())
                        rule = rule.replace("<tmp1>", NamingManager.genVarName())

                        rule = rule.replace("<ruleHead>", head)
                        rule = rule.replace("<subjName>", argRes0.value)
                        rule = rule.replace("<propName>", argRes1.value)
                        rule = rule.replace("<value>", argRes2.value)

                        // Добавляем правило в набор правил
                        val completedRules = argRes0.completedRules +
                                argRes1.completedRules +
                                argRes2.completedRules +
                                rule

                        // Добавляем в результат
                        result.add(CompilationResult("", "", completedRules))
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

            // Для всех результатов компиляции
            compiledArg0.forEach { argRes0 ->
                compiledArg1.forEach { argRes1 ->
                    // Собираем правила для аргументов
                    val head = argRes0.ruleHead + argRes1.ruleHead

                    // Заполняем шаблон
                    var rule = DECISION_TREE_VAR_ASSIGN_PATTERN
                    rule = rule.replace("<ruleHead>", head)
                    rule = rule.replace("<newObj>", argRes1.value)
                    rule = rule.replace("<varPred>", JenaUtil.genLink(POAS_PREF, VAR_PRED))
                    rule = rule.replace("<varName>", varName)

                    // Добавляем правило в набор правил
                    val completedRules = argRes0.completedRules +
                            argRes1.completedRules +
                            rule

                    // Добавляем в результат
                    result.add(CompilationResult("", "", completedRules))
                }
            }
        }

        return result
    }

    companion object {

        /**
         * Шаблон правила присваивания значения свойства
         */
        private const val PROPERTY_ASSIGN_PATTERN = "[\n(<tmp0> <propName> <tmp1>)\n<ruleHead>\nequal(<subjName>, <tmp0>)\n->\ndrop(0)\n(<subjName> <propName> <value>)\n]\n"

        /**
         * Шаблон правила присваивания значения переменной дерева мысли
         */
        private const val DECISION_TREE_VAR_ASSIGN_PATTERN = "[\n<ruleHead>\n->\ndrop(0)\n(<newObj> <varPred> <varName>)\n]\n"
    }
}