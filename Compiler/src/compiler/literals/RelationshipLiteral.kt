package compiler.literals

import compiler.Literal
import compiler.Operator
import dictionaries.RelationshipsDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

/**
 * Relationship литерал
 * @param value Имя отношения
 */
class RelationshipLiteral(value: String) : Literal(value) {

    init {
        // Проверяем существование отношения
        require(RelationshipsDictionary.exist(value)) { "Указанного отношения не существует." }
    }

    override val resultDataType: DataType = DataType.Relationship

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = RelationshipLiteral(value)
}