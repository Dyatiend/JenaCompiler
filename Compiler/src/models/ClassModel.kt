package models

import compiler.Operator
import dictionaries.ClassesDictionary
import util.DataType

/**
 * Модель класса в предметной области
 * @param name Имя класса
 * @param parent Имя класса родителя
 * @param calcExprXML Выражение для вычисления в формате XML
 */
data class ClassModel(
    val name: String,
    val parent: String?,
    val calcExprXML: String?
) {

    /**
     * Проверяет корректность модели
     * @throws IllegalArgumentException
     */
    fun validate() {
        require(name.isNotBlank()) {
            "Некорректное имя класса."
        }
        require(!ClassesDictionary.exist(name)) {
            "Класс $name уже объявлен в словаре."
        }
        require(parent == null || ClassesDictionary.exist(parent)) {
            "Класс $parent не объявлен в словаре."
        }
        calcExprXML?.let {
            val expr = Operator.fromXMLString(it)
            require(expr?.resultDataType == DataType.Boolean) {
                "Выражение для вычисления класса $name должно иметь тип Boolean, но имеет тип ${expr?.resultDataType}."
            }
        }
    }

    /**
     * Выражение для вычисления
     */
    val calcExpr
        get() = if (calcExprXML != null) Operator.fromXMLString(calcExprXML) else null

    companion object {

        /**
         * Имя переменной в выражении для вычисления класса
         */
        const val CALC_EXPR_VAR_NAME = "obj"
    }
}