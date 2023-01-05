package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import compiler.util.JenaUtil.POAS_PREF
import util.DataType

/**
 * Object литерал
 * @param value Имя объекта
 */
class ObjectLiteral(value: String) : Literal(value) {

    override val resultDataType: DataType = DataType.Object

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = ObjectLiteral(value)
}