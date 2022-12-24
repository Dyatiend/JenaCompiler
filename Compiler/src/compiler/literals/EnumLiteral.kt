package compiler.literals

import compiler.Literal
import compiler.Operator
import dictionaries.PropertiesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil

/**
 * Enum литерал
 * @param value Значение
 * @param owner Имя enum, к которому относится данный элемент
 */
class EnumLiteral(value: String, private val owner: String) : Literal(value) {

    init {
        // Проверяем существование enum и наличие у него такого значения
        require(PropertiesDictionary.isEnumExist(owner)) { "Enum $owner не объявлен в словаре." }
        require(PropertiesDictionary.enumValues(owner).contains(value)) {
            "Enum $owner не содержит значения $value."
        }
    }

    override val resultDataType: DataType = DataType.Enum

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(JenaUtil.POAS_PREF, value))

    override fun clone(): Operator = EnumLiteral(value, owner)
}