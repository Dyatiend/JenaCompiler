package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import dictionaries.EnumsDictionary
import util.DataType

/**
 * Enum литерал
 * @param value Значение
 * @param owner Имя enum, к которому относится данный элемент
 */
class EnumLiteral(value: String, private val owner: String) : Literal(value) {

    init {
        // Проверяем существование enum и наличие у него такого значения
        require(EnumsDictionary.exist(owner)) { "Enum $owner не объявлен в словаре." }
        require(EnumsDictionary.containsValue(owner, value)) {
            "Enum $owner не содержит значения $value."
        }
    }

    override val resultDataType: DataType = DataType.Enum

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(JenaUtil.POAS_PREF, value))

    override fun clone(): Operator = EnumLiteral(value, owner)
}