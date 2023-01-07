package models

import compiler.Operator
import dictionaries.ClassesDictionary
import util.DataType

/**
 * Модель класса в предметной области
 * @param name Имя класса
 * @param parent Модель класса родителя
 * @param calcExpr Выражение для вычисления
 * @see Operator
 */
data class ClassModel(
    val name: String,
    val parent: ClassModel?,
    val calcExpr: Operator?
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя класса."
        }
        require(!ClassesDictionary.exist(name)) {
            "Класс $name уже объявлен в словаре."
        }
        require(parent == null || ClassesDictionary.exist(parent.name)) {
            "Класс $parent не объявлен в словаре."
        }
        require(calcExpr == null || calcExpr.resultDataType == DataType.Boolean) {
            "Выражение для вычисления класса $name должно иметь тип Boolean, но имеет тип ${calcExpr?.resultDataType}."
        }
    }

    companion object {

        /**
         * Имя переменной в выражении для вычисления класса
         */
        const val CALC_EXPR_VAR_NAME = "obj"
    }
}