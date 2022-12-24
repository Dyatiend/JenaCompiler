package compiler.literals

import compiler.Literal
import compiler.Operator
import util.CompilationResult
import util.DataType
import util.JenaUtil

/**
 * Enum литерал
 * @param value Значение
 * @param owner Имя enum, к которому относится данный элемент
 */
class EnumLiteral(value: String, private val owner: String) : Literal(value) {

    override val resultDataType: DataType = DataType.Enum

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(JenaUtil.POAS_PREF, value))

    override fun clone(): Operator = EnumLiteral(value, owner)
}