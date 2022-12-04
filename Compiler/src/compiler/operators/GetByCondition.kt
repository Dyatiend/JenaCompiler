package compiler.operators

import compiler.Operator
import compiler.values.BooleanValue
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.genCountValuesPrim
import util.JenaUtil.genEqualPrim
import util.JenaUtil.genIntegerVal
import util.JenaUtil.genRule
import util.JenaUtil.genTriple
import util.JenaUtil.genVar
import util.NamingManager.genPredicateName
import util.NamingManager.genVarName

class GetByCondition(
    args: List<Operator>,
    val varName: String
) : BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Boolean))
    }

    override fun resultDataType(): DataType {
        return DataType.Boolean
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val value = genVar(varName)
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()

        // Передаем завершенные правила дальше
        completedRules += compiledArg0.completedRules

        // Флаг, указывающий на объекты множества
        val flag = genPredicateName()
        // Skolem name
        val skolemName = genVarName()

        // Вспомогательные переменные
        val empty0 = genVarName()
        val empty1 = genVarName()

        // Для всех результатов компиляции
        compiledArg0.ruleHeads.forEach { head0 ->
            // Инициализируем переменную FIXME?: сюда не попадут объекты без связей
            var head = genTriple(value, genVarName(), genVarName())

            head += head0

            // Если оператор булево значение
            if (arg0 is BooleanValue) {
                // Добавляем выражение, равное значению
                head += if (arg0.value.toBoolean()) {
                    genEqualPrim("1", "1")
                } else {
                    genEqualPrim("0", "1")
                }
            }

            // Собираем правило, помачающее найденные объекты
            val rule = genRule(head, skolemName, flag, value)

            // Добавляем в основное правило
            val mainHead = genTriple(empty0, flag, value) +
                    genCountValuesPrim(empty0, flag, empty1) +
                    genEqualPrim(empty1, genIntegerVal("1"))

            // Добавляем в рзультат
            heads.add(mainHead)
            completedRules += rule
        }

        // Добавляем паузу
        completedRules += JenaUtil.PAUSE_MARK

        return CompilationResult(value, heads, completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args().forEach { arg ->
            newArgs.add(arg.clone())
        }

        return GetByCondition(newArgs, varName)
    }
}