package compiler

import compiler.util.CompilationResult
import compiler.util.JenaUtil
import util.DataType

/**
 * Переменная, вводимая некоторыми операторами
 * @param name Имя переменной
 */
class Variable(
    private val name: String
) : Operator {

    override val args: List<Operator> = ArrayList()

    override val argsDataTypes: List<List<DataType>> = ArrayList()

    override val resultDataType: DataType = DataType.Object

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genVar(name))

    override fun clone(): Operator = Variable(name)

    override fun clone(newArgs: List<Operator>): Operator {
        require(newArgs.isEmpty()) { "Для переменной аргументы не требуются." }
        return clone()
    }
}