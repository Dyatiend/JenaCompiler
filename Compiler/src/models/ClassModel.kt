package models

import compiler.Operator

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

    companion object {

        /**
         * Имя переменной в выражении для вычисления класса
         */
        const val CALC_EXPR_VAR_NAME = "obj"
    }
}