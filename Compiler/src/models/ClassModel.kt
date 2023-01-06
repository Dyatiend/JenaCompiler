package models

import compiler.Operator

/**
 * Модель класса в предметной области
 * @param name Имя класса
 * @param parent Имя родительского класса
 * @param calcExpr Выражение для вычисления
 * @see Operator
 */
data class ClassModel(
    val name: String,
    val parent: String?,
    val calcExpr: Operator?
)