package compiler.values

import compiler.Operator
import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class IntegerValue(value: Int) : Value(value.toString()) {

    override fun resultDataType(): DataType = DataType.Integer

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genIntegerVal(value), listOf(""), "")

    override fun clone(): Operator {
        return IntegerValue(value.toInt())
    }
}