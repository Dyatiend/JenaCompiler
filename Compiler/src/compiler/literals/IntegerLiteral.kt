package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import util.DataType
import util.JenaUtil

/**
 * Integer литерал
 * @param value Значение
 */
class IntegerLiteral(value: Int) : Literal(value.toString()) {

    override val resultDataType: DataType = DataType.Integer

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genVal(value.toInt()))

    override fun clone(): Operator = IntegerLiteral(value.toInt())
}