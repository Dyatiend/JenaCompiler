package compiler.values

import compiler.Value
import dictionaries.RelationshipsDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

class RelationshipValue(value: String) : Value(value) {

    init {
        require(RelationshipsDictionary.exist(value)) { "Указанного отношения не существует" }
    }

    override fun resultDataType(): DataType = DataType.Relationship

    override fun compile(): CompilationResult =
        CompilationResult(JenaUtil.genLink(POAS_PREF, value), emptyList(), "")
}