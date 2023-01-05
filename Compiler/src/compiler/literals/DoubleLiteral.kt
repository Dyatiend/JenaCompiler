package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import util.DataType

/**
 * Double литерал
 * @param value Значение
 */
class DoubleLiteral(value: Double) : Literal(value.toString()) {

    override val resultDataType: DataType = DataType.Double

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genVal(value.toDouble()))

    override fun clone(): Operator = DoubleLiteral(value.toDouble())
}