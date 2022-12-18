package compiler.literals

import compiler.Literal
import compiler.Operator
import dictionaries.PropertiesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

/**
 * Property литерал
 * @param value Имя свойства
 */
class PropertyLiteral(value: String) : Literal(value) {

    init {
        // Проверяем существование свойства
        require(PropertiesDictionary.exist(value)) { "Указанного свойства не существует." }
    }

    override val resultDataType: DataType = DataType.Property

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = PropertyLiteral(value)
}