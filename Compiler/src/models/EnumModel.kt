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
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя перечисления."
        }
        require(values.isNotEmpty()) {
            "Перечисление $name не содержит значений."
        }
    }

    /**
     * Содержит ли перечисление указанное значение
     * @param value Значение
     */
    fun containsValue(value: String) = values.contains(value)
}