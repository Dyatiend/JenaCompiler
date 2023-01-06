package util

/**
 * Тип данных
 */
sealed class DataType {

    /**
     * Переменная дерева мысли
     */
    object DecisionTreeVar : DataType()

    /**
     * Класс
     */
    object Class : DataType()

    /**
     * Объект
     */
    object Object : DataType()

    /**
     * Свойство
     */
    object Property : DataType()

    /**
     * Отношение
     */
    object Relationship : DataType()

    /**
     * Строка
     */
    object String : DataType()

    /**
     * Булево значение
     */
    object Boolean : DataType()

    /**
     * Целое число
     */
    object Integer : DataType()

    /**
     * Дробное число
     */
    object Double : DataType()

    /**
     * Результат сравнения
     */
    object ComparisonResult : DataType()

    /**
     * Enum
     */
    object Enum : DataType()

    /**
     * Может ли этот тип быть преобразован в другой
     * @param to Тип, в который преобразовываем
     * @return Может ли этот тип быть преобразован в другой
     */
    fun canCast(to: DataType) = this == DecisionTreeVar && to == Object

    override fun toString() = when (this) {
        DecisionTreeVar -> "DECISION_TREE_VAR"
        Class -> "CLASS"
        Object -> "OBJECT"
        Property -> "PROPERTY"
        Relationship -> "RELATIONSHIP"
        String -> "STRING"
        Boolean -> "BOOLEAN"
        Integer -> "INTEGER"
        Double -> "DOUBLE"
        ComparisonResult -> "COMPARISON_RESULT"
        Enum -> "ENUM"
    }

    companion object {

        fun valueOf(value: kotlin.String) = when (value) {
            "DECISION_TREE_VAR" -> DecisionTreeVar
            "CLASS" -> Class
            "OBJECT" -> Object
            "PROPERTY" -> Property
            "RELATIONSHIP" -> Relationship
            "STRING" -> String
            "BOOLEAN" -> Boolean
            "INTEGER" -> Integer
            "DOUBLE" -> Double
            "COMPARISON_RESULT" -> ComparisonResult
            "ENUM" -> Enum
            else -> null
        }
    }
}