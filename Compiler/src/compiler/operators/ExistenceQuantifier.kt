package compiler.operators

import compiler.Operator
import compiler.values.BooleanValue
import util.CompilationResult
import util.DataType
import util.JenaUtil.genEqualPrim
import util.JenaUtil.genTriple
import util.JenaUtil.genVar
import util.NamingManager.genVarName

/**
 * Квантор существования
 * TODO: отрицание
 */
class ExistenceQuantifier(
    args: List<Operator>,
    private val varName: String
) : BaseOperator(args) {

    /**
     * Является ли оператор негативным (т.е. нужно ли отрицание при компиляции)
     */
    internal var isNegative = false

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Boolean))
    }

    override fun resultDataType(): DataType {
        return DataType.Boolean
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()

        // Передаем завершенные правила дальше
        completedRules += compiledArg0.completedRules

        // Если оператор булево значение
        if (arg0 is BooleanValue) {
            // Добавляем выражение, равное значению
            val head = if (arg0.value.toBoolean()) {
                genEqualPrim("1", "1")
            } else {
                genEqualPrim("0", "1")
            }

            // Добавляем в массив
            heads.add(head)
        } else {
            // Для всех результатов компиляции
            compiledArg0.ruleHeads.forEach { head0 ->
                // Добавляем инициализацию переменной
                val head = genTriple(genVar(varName), genVarName(), genVarName()) + head0

                // Добавляем в массив
                heads.add(head)
            }
        }

        return CompilationResult("", heads, completedRules)
    }
}