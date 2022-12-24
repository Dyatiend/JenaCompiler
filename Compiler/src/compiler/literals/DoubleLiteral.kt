package compiler.literals

import compiler.Literal
import compiler.Operator
import util.CompilationResult
import util.DataType
import util.JenaUtil

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