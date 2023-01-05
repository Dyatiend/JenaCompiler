package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import util.DataType

/**
 * String литерал
 * @param value Значение
 */
class StringLiteral(value: String) : Literal(value) {

    override val resultDataType: DataType = DataType.String

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genVal(value))

    override fun clone(): Operator = StringLiteral(value)
}