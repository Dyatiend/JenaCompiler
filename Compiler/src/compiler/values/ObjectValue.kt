package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

class ObjectValue(value: String) : Value(value) {

    override fun resultDataType(): DataType = DataType.Object

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genLink(POAS_PREF, value), emptyList(), "")
}