package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import compiler.util.JenaUtil.POAS_PREF
import dictionaries.RelationshipsDictionary
import util.DataType

/**
 * Relationship литерал
 * @param value Имя отношения
 */
class RelationshipLiteral(value: String) : Literal(value) {

    init {
        // Проверяем существование отношения
        require(RelationshipsDictionary.exist(value)) { "Отношение $value не объявлено в словаре." }
    }

    override val resultDataType: DataType = DataType.Relationship

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = RelationshipLiteral(value)
}