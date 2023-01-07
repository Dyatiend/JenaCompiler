package compiler.operators

import compiler.Operator
import compiler.util.CompilationResult
import util.DataType

/**
 * Логическое отрицание
 */
class LogicalNot(args: List<Operator>) : BaseOperator(args) {

    override val argsDataTypes = listOf(listOf(DataType.Boolean))

    override val resultDataType = DataType.Boolean

    override fun compile(): CompilationResult {
        throw RuntimeException("Оператор LogicalNot должен быть удален при упрощении выражения")
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        return LogicalNot(newArgs)
    }

    override fun clone(newArgs: List<Operator>): Operator {
        return LogicalNot(newArgs)
    }
}