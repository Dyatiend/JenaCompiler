package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class BooleanValue(value: Boolean) : Value(value.toString()) {

    override fun resultDataType(): DataType = DataType.Boolean

    override fun compile(): List<CompilationResult> =
        listOf(CompilationResult(JenaUtil.genBooleanVal(value), "", ""))
}