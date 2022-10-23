package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class IntegerValue(value: Int) : Value(value.toString()) {

    override fun resultDataType(): DataType = DataType.Integer

    override fun compile(): List<CompilationResult> =
        listOf(CompilationResult(JenaUtil.genIntegerVal(value), "", ""))
}