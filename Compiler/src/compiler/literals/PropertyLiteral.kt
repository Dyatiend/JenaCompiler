package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import compiler.util.JenaUtil.POAS_PREF
import dictionaries.PropertiesDictionary
import util.DataType

/**
 * Property литерал
 * @param value Имя свойства
 */
class PropertyLiteral(value: String) : Literal(value) {

    init {
        // Проверяем существование свойства
        require(PropertiesDictionary.exist(value)) { "Свойство $value не объявлено в словаре." }
    }

    override val resultDataType: DataType = DataType.Property

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = PropertyLiteral(value)
}