package compiler.operators

import compiler.Operator
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.genCountValuesPrim
import util.JenaUtil.genEqualPrim
import util.JenaUtil.genIntegerVal
import util.JenaUtil.genNoValuePrim
import util.JenaUtil.genRule
import util.JenaUtil.genTriple
import util.JenaUtil.genVar
import util.NamingManager.genPredicateName
import util.NamingManager.genVarName

/**
 * Выбор экстремального объекта
 */
class GetExtreme(
    args: List<Operator>,
    val varName: String,
    val extremeVarName: String
): BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Boolean, DataType.Boolean))
    }

    override fun resultDataType(): DataType {
        return DataType.Object
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val value = genVarName()
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()

        // Передаем завершенные правила дальше
        completedRules += compiledArg0.completedRules +
                compiledArg1.completedRules

        // Вспомогательные переменные
        val empty0 = genVarName()
        val empty1 = genVarName()

        // Флаг, указывающий на объекты множества
        val flag = genPredicateName()
        // Skolem name
        val skolemName = genVarName()

        // Флаг цикла
        val cycleFlag = genPredicateName()
        // Переменная цикла
        val cycleVar = genVarName()

        // Для всех результатов компиляции
        compiledArg0.ruleHeads.forEach { head0 ->
            compiledArg1.ruleHeads.forEach { head1 ->

                // ---------------- Генерируем правило, помечающее объекты множества --------------

                // Добавляем в рзультат
                completedRules += genRule(head0, skolemName, flag, genVar(varName))

                // ---------------- Генерируем правило, помечающее потенциальный экстремум --------------

                // Собираем правило, организующее цикл
                val cycleHead = genNoValuePrim(empty0, cycleFlag) +
                        genTriple(empty1, flag, cycleVar)

                // Добавляем в рзультат
                completedRules += genRule(cycleHead, skolemName, cycleFlag, cycleVar)

                // ---------------- Генерируем правило, проверяющее экстремум --------------

                // Собираем фильтрующее правило
                val filterHead = genTriple(empty0, cycleFlag, genVar(extremeVarName)) + head1

                var filterRule = EXTREME_PATTERN
                filterRule = filterRule.replace("<ruleHead>", filterHead)

                // Добавляем в основное правило получение объекта и проверку их количества
                val mainHead = genTriple(empty0, cycleFlag, value) +
                        genCountValuesPrim(empty0, cycleFlag, empty1) +
                        genEqualPrim(empty1, genIntegerVal("1"))

                // Добавляем в рзультат
                heads.add(mainHead)
                completedRules += filterRule
            }
        }

        // Добавляем паузу
        completedRules += JenaUtil.PAUSE_MARK

        return CompilationResult(value, heads, completedRules)
    }

    companion object {

        /**
         * Шаблон правила выбора экстремального
         */
        private const val EXTREME_PATTERN = "[<ruleHead>->drop(0)]"
    }
}