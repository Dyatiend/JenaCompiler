package util

/**
 * Типы данных
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
     * Линейный Enum
     */
    object LinerEnum : DataType()

    /**
     * Может ли один тип быть преобразован в другой
     * @param to Тип, в который преобразовываем
     * @return Может ли один тип быть преобразован в другой
     */
    fun canCast(to: DataType): kotlin.Boolean {
        return this == DecisionTreeVar && to == Object
                || this == LinerEnum && to == Enum
    }

    override fun toString(): kotlin.String = when (this) {
        Boolean -> "BOOLEAN"
        Class -> "CLASS"
        ComparisonResult -> "COMPARISON_RESULT"
        DecisionTreeVar -> "DECISION_TREE_VAR"
        Double -> "DOUBLE"
        Enum -> "ENUM"
        Integer -> "INTEGER"
        LinerEnum -> "LINER_ENUM"
        Object -> "OBJECT"
        Property -> "PROPERTY"
        Relationship -> "RELATIONSHIP"
        String -> "STRING"
    }

    companion object {

        /**
         * Может ли один тип быть преобразован в другой
         * @param from Тип, который преобразовываем
         * @param to Тип, в который преобразовываем
         * @return Может ли один тип быть преобразован в другой
         */
        fun canCast(from: DataType, to: DataType): kotlin.Boolean {
            return from == DecisionTreeVar && to == Object
                    || from == LinerEnum && to == Enum
        }

        fun values(): Array<DataType> = arrayOf(
            DecisionTreeVar,
            Class,
            Object,
            Property,
            Relationship,
            String,
            Boolean,
            Integer,
            Double,
            ComparisonResult,
            Enum,
            LinerEnum
        )

        fun valueOf(value: kotlin.String): DataType = when (value) {
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
                "LINER_ENUM" -> LinerEnum
                else -> throw IllegalArgumentException("No object util.DataType.$value")
        }
    }
}