package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import util.DataType

/**
 * Boolean литерал
 * @param value Значение
 */
class BooleanLiteral(value: Boolean) : Literal(value.toString()) {

    override val resultDataType: DataType = DataType.Boolean

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genVal(value.toBoolean()))

    /**
     * Скомпилировать boolean литерал как head
     * @return Голова правила, прерывающая правило, если значение false
     */
    fun compileAsHead() =
        if (value.toBoolean()) JenaUtil.genEqualPrim("1", "1")
        else JenaUtil.genEqualPrim("0", "1")

    override fun clone(): Operator = BooleanLiteral(value.toBoolean())
}