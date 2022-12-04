package compiler.operators

import compiler.Operator
import util.CompilationResult
import util.DataType

/**
 * Логическое отрицание
 */
class LogicalNot(args: List<Operator>) : BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Boolean))
    }

    override fun resultDataType(): DataType {
        return DataType.Boolean
    }

    override fun compile(): CompilationResult {
        throw RuntimeException("Оператор LogicalNot должен быть удален при упрощении выражения")
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args().forEach { arg ->
            newArgs.add(arg.clone())
        }

        return LogicalNot(newArgs)
    }
}