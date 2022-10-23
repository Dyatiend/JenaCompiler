package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class StringValue(value: String) : Value(value) {

    override fun resultDataType(): DataType = DataType.String

    override fun compile(): List<CompilationResult> =
        listOf(CompilationResult(JenaUtil.genStingVal(value), "", ""))
}