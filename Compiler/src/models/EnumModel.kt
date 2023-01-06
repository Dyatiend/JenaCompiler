package models

/**
 * Модель перечисления в предметной области
 * @param name Имя перечисления
 * @param values Возможные значения
 * @param linerPredicate Имя предиката, задающего линейный порядок
 */
data class EnumModel(
    val name: String,
    val values: List<String>,
    val linerPredicate: String?
)