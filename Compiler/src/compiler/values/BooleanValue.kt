package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil

class BooleanValue(value: Boolean) : Value(value.toString()) {

    override var value = value.toString()
        set(value) {
            require(value == "true" || value == "false") { "Некорректное значение" }
            field = value
        }

    override fun resultDataType(): DataType = DataType.Boolean

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genBooleanVal(value), emptyList(), "")
}