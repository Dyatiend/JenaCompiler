package models

/**
 * Модель переменной дерева рассуждения
 * @param name Имя переменной
 * @param classModel Модель класса переменной
 * @see ClassModel
 */
data class DecisionTreeVarModel(
    val name: String,
    val classModel: ClassModel
)