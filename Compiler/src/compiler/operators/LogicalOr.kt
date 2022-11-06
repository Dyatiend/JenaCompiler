package compiler.operators

import compiler.Operator
import util.CompilationResult
import util.DataType

/**
 * Логическое ИЛИ
 */
class LogicalOr(args: List<Operator>): BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Boolean, DataType.Boolean))
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
        val arg1 = arg(1)

        // Раскрываем через And
        val expr0 = LogicalAnd(listOf(arg0, arg1)).doSemantic()
        val expr1 = LogicalAnd(listOf(LogicalNot(listOf(arg0)), arg1)).doSemantic()
        val expr2 = LogicalAnd(listOf(arg0, LogicalNot(listOf(arg1)))).doSemantic()

        // Компилируем правила
        val compiledExpr0 = expr0.compile()
        val compiledExpr1 = expr1.compile()
        val compiledExpr2 = expr2.compile()

        // Если в разных вариациях отличаются не только головы
        if (compiledExpr0.completedRules != compiledExpr1.completedRules
            || compiledExpr0.completedRules != compiledExpr2.completedRules) {
            TODO() // Как то собрать их в одно ???
        } else {
            // Передаем завершенные правила дальше
            completedRules += compiledExpr0.completedRules
        }

        // Собираем полученные правила
        heads.addAll(compiledExpr0.ruleHeads)
        heads.addAll(compiledExpr1.ruleHeads)
        heads.addAll(compiledExpr2.ruleHeads)

        return CompilationResult("", heads, completedRules)
    }
}