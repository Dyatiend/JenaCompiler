package compiler.literals

import compiler.Literal
import compiler.Operator
import dictionaries.ClassesDictionary
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF

/**
 * Class литерал
 * @param value Имя класса
 */
class ClassLiteral(value: String) : Literal(value) {

    init {
        // Проверяем существование класса
        require(ClassesDictionary.exist(value)) { "Указанного класса не существует." }
    }

    override val resultDataType: DataType = DataType.Class

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(POAS_PREF, value))

    override fun clone(): Operator = ClassLiteral(value)
}