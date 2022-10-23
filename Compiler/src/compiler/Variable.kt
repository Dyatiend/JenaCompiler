package compiler

import util.CompilationResult
import util.DataType
import util.JenaUtil

/**
 * Переменная, вводимая некоторыми операторами
 */
class Variable(
    private val name: String
) : Operator {

    override fun argsDataTypes(): List<List<DataType>> = ArrayList()

    override fun resultDataType(): DataType = DataType.Object

    override fun compile(): List<CompilationResult> =
        listOf(CompilationResult(JenaUtil.genVar(name), "", ""))
}