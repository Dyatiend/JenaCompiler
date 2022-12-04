package compiler.values

import compiler.Operator
import compiler.Value
import dictionaries.PropertiesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

class PropertyValue(value: String) : Value(value) {

    init {
        require(PropertiesDictionary.exist(value)) { "Указанного свойства не существует" }
    }

    override fun resultDataType(): DataType = DataType.Property

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genLink(POAS_PREF, value), listOf(""), "")

    override fun clone(): Operator {
        return PropertyValue(value)
    }
}