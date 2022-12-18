package compiler.literals

import compiler.Literal
import compiler.Operator
import util.CompilationResult
import util.DataType
import util.JenaUtil

/**
 * String литерал
 * @param value Значение
 */
class StringLiteral(value: String) : Literal(value) {

    override val resultDataType: DataType = DataType.String

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genStingVal(value))

    override fun clone(): Operator = StringLiteral(value)
}