package compiler.values

import compiler.Operator
import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class EnumValue(value: String, private val isLinerEnum: Boolean): Value(value) {

    override fun resultDataType(): DataType = if (isLinerEnum) DataType.LinerEnum else DataType.Enum

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genLink(JenaUtil.POAS_PREF, value), listOf(""), "")

    override fun clone(): Operator {
        return EnumValue(value, isLinerEnum)
    }
}