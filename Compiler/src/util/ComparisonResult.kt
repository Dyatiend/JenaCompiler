package util

/**
 * Описывает результат сравнения
 */
sealed class ComparisonResult {

    /**
     * Больше
     */
    object Greater : ComparisonResult()

    /**
     * Меньше
     */
    object Less : ComparisonResult()

    /**
     * Эквивалентно
     */
    object Equal : ComparisonResult()

    /**
     * Не определено
     */
    object Undetermined : ComparisonResult()

    override fun toString(): String = when (this) {
        Equal -> "EQUAL"
        Greater -> "GREATER"
        Less -> "LESS"
        Undetermined -> "UNDETERMINED"
    }
    
    companion object {

        fun values(): Array<ComparisonResult> = arrayOf(Greater, Less, Equal, Undetermined)

        fun valueOf(value: String): ComparisonResult = when (value) {
                "GREATER" -> Greater
                "LESS" -> Less
                "EQUAL" -> Equal
                "UNDETERMINED" -> Undetermined
                else -> throw IllegalArgumentException("No object util.ComparisonResult.$value")
        }
    }
}