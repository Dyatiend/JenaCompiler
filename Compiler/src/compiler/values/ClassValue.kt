package compiler.values

import compiler.Value
import dictionaries.ClassesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

class ClassValue(value: String) : Value(value) {

    init {
        require(ClassesDictionary.exist(value)) { "Указанного класса не существует" }
    }

    override fun resultDataType(): DataType = DataType.Class

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genLink(POAS_PREF, value), emptyList(), "")
}