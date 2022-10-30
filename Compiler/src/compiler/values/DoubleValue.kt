package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class DoubleValue(value: Double) : Value(value.toString()) {

    override fun resultDataType(): DataType = DataType.Double

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genDoubleVal(value), emptyList(), "")
}