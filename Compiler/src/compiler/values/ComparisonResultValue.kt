package compiler.values

import compiler.Value
import util.ComparisonResult
import util.CompilationResult
import util.DataType
import util.JenaUtil

class ComparisonResultValue(value: ComparisonResult) : Value(value.toString()) {

    override fun resultDataType(): DataType = DataType.ComparisonResult

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genLink(JenaUtil.POAS_PREF, value), emptyList(), "")
}