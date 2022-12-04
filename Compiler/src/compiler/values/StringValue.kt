package compiler.values

import compiler.Operator
import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class StringValue(value: String) : Value(value) {

    override fun resultDataType(): DataType = DataType.String

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genStingVal(value), listOf(""), "")

    override fun clone(): Operator {
        return StringValue(value)
    }
}