package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class DoubleValue(value: Double) : Value(value.toString()) {

    override fun resultDataType(): DataType = DataType.Double

    override fun compile(): List<CompilationResult> =
        listOf(CompilationResult(JenaUtil.genDoubleVal(value), "", ""))
}