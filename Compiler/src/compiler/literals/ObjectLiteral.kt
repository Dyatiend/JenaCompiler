package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

/**
 * Object литерал
 * @param value Имя объекта
 */
class ObjectLiteral(value: String) : Literal(value) {

    override val resultDataType: DataType
        get() = DataType.Object

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = ObjectLiteral(value)
}