package models

/**
 * Модель отношения в предметной области
 * @param name Имя отношения
 * @param parent Имя родительского отношения
 * @param args Имена классов аргументов
 * @param scaleType Тип порядковой шкалы
 * @param relationType Тип связи между классами
 * @param reverse Имя обратного отношения
 * @param transitiveClosure Имя транзитивного замыкания
 * @param reverseTransitiveClosure Имя обратного транзитивного замыкания
 * @param between Имя отношения типа "между"
 * @param closerToThan Имя отношения типа "ближе"
 * @param furtherFromThan Имя отношения типа "дальше"
 * @param flags Флаги свойств отношения
 */
data class RelationshipModel(
    val name: String,
    val parent: String?,
    val args: List<String>,
    val scaleType: ScaleType?,
    val relationType: RelationType?,
    val reverse: String?,
    val transitiveClosure: String?,
    val reverseTransitiveClosure: String?,
    val between: String?,
    val closerToThan: String?,
    val furtherFromThan: String?,
    val flags: Int
) {

    /**
     * Является ли отношение симметричным
     */
    fun isSymmetric() = flags and SYMMETRIC

    /**
     * Является ли отношение анти-симметричным
     */
    fun isAntisymmetric() = flags and ANTISYMMETRIC

    /**
     * Является ли отношение рефлексивным
     */
    fun isReflexive() = flags and REFLEXIVE

    /**
     * Является ли отношение анти-рефлексивным (строгим)
     */
    fun isStrict() = flags and STRICT

    /**
     * Является ли отношение транзитивным
     */
    fun isTransitive() = flags and TRANSITIVE

    /**
     * Является ли отношение анти-транзитивным
     */
    fun isIntransitive() = flags and INTRANSITIVE

    companion object {

        /**
         * Флаг симметричности отношения
         */
        const val SYMMETRIC = 2

        /**
         * Флаг анти-симметричности отношения
         */
        const val ANTISYMMETRIC = 4

        /**
         * Флаг рефлексивности отношения
         */
        const val REFLEXIVE = 8

        /**
         * Флаг анти-рефлексивности (строгости) отношения
         */
        const val STRICT = 16

        /**
         * Флаг транзитивности отношения
         */
        const val TRANSITIVE = 32

        /**
         * Флаг анти-транзитивности отношения
         */
        const val INTRANSITIVE = 64

        /**
         * Типы связей между классами
         */
        sealed class RelationType {

            /**
             * Один к одному
             */
            object OneToOne : RelationType()

            /**
             * Один ко многим
             */
            object OneToMany : RelationType()

            companion object {

                fun valueOf(value: String) = when (value) {
                    "ONE_TO_ONE" -> OneToOne
                    "ONE_TO_MANY" -> OneToMany
                    else -> throw IllegalArgumentException("No object models.RelationshipModel.RelationType.$value")
                }
            }
        }

        /**
         * Тип порядковой шкалы
         */
        sealed class ScaleType {

            /**
             * Линейный порядок
             */
            object Liner : ScaleType()

            /**
             * Частичный порядок
             */
            object Partial : ScaleType()

            companion object {

                fun valueOf(value: String) = when (value) {
                    "LINER" -> Liner
                    "PARTIAL" -> Partial
                    else -> throw IllegalArgumentException("No object models.RelationshipModel.ScaleType.$value")
                }
            }
        }
    }
}