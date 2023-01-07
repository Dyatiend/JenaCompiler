package models

import dictionaries.ClassesDictionary
import dictionaries.RelationshipsDictionary

/**
 * Модель отношения в предметной области
 * @param name Имя отношения
 * @param parent Имя родительского отношения
 * @param argsClasses Имена классов аргументов
 * @param scaleType Тип порядковой шкалы
 * @param relationType Тип связи между классами
 * @param reverseRelName Имя обратного отношения
 * @param transitiveClosureRelName Имя отношения транзитивного замыкания
 * @param reverseTransitiveClosureRelName Имя отношения обратного транзитивного замыкания
 * @param isBetweenRelName Имя отношения типа "между"
 * @param isCloserToThanRelName Имя отношения типа "ближе"
 * @param isFurtherFromThanRelName Имя отношения типа "дальше"
 * @param flags Флаги свойств отношения
 * @param varsCount Количество переменных в правиле для вычисления
 * @param head Голова правила для проверки отношения
 * @param rules Завершенные правила для проверки отношения
 */
data class RelationshipModel(
    val name: String,
    val parent: String?,
    val argsClasses: List<String>,
    val scaleType: ScaleType?,
    val relationType: RelationType?,
    val reverseRelName: String?,
    val transitiveClosureRelName: String?,
    val reverseTransitiveClosureRelName: String?,
    val isBetweenRelName: String?,
    val isCloserToThanRelName: String?,
    val isFurtherFromThanRelName: String?,
    val flags: Int,
    val varsCount: Int,
    val head: String,
    val rules: String
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя отношения."
        }
        require(!RelationshipsDictionary.exist(name)) {
            "Отношение $name уже объявлено в словаре."
        }
        require(parent == null || RelationshipsDictionary.exist(parent)) {
            "Отношение $parent не объявлено в словаре."
        }
        require(argsClasses.size >= 2) {
            "Отношение $name имеет меньше двух аргументов."
        }
        argsClasses.forEach {
            require(ClassesDictionary.exist(it)) {
                "Класс $it не объявлен в словаре."
            }
        }
        require(
            scaleType == null
                    || reverseRelName != null
                    && transitiveClosureRelName != null
                    && reverseTransitiveClosureRelName != null
                    && isBetweenRelName != null
                    && isCloserToThanRelName != null
                    && isFurtherFromThanRelName != null
                    && RelationshipsDictionary.exist(reverseRelName)
                    && RelationshipsDictionary.exist(transitiveClosureRelName)
                    && RelationshipsDictionary.exist(reverseTransitiveClosureRelName)
                    && RelationshipsDictionary.exist(isBetweenRelName)
                    && RelationshipsDictionary.exist(isCloserToThanRelName)
                    && RelationshipsDictionary.exist(isFurtherFromThanRelName)
        ) {
            "Для отношения $name не указано одно из отношений порядковой шкалы или оно не объявлено в словаре."
        }
        require(scaleType == null || argsClasses.size == 2 && argsClasses[0] == argsClasses[1]) {
            "Отношение порядковой шкалы может быть только бинарным и только между объектами одного класса."
        }
        when (scaleType) {
            ScaleType.Liner -> require(flags == 6) {
                "Некорректный набор флагов для отношения линейного порядка."
            }

            ScaleType.Partial -> require(flags == 22) {
                "Некорректный набор флагов для отношения частичного порядка."
            }

            else -> require(flags < 64) {
                "Некорректный набор флагов."
            }
        }
    }

    /**
     * Является ли отношение симметричным
     */
    val isSymmetric
        get() = flags and SYMMETRIC != 0

    /**
     * Является ли отношение анти-симметричным
     */
    val isAntisymmetric
        get() = flags and ANTISYMMETRIC != 0

    /**
     * Является ли отношение рефлексивным
     */
    val isReflexive
        get() = flags and REFLEXIVE != 0

    /**
     * Является ли отношение анти-рефлексивным (строгим)
     */
    val isStrict
        get() = flags and STRICT != 0

    /**
     * Является ли отношение транзитивным
     */
    val isTransitive
        get() = flags and TRANSITIVE != 0

    /**
     * Является ли отношение анти-транзитивным
     */
    val isIntransitive
        get() = flags and INTRANSITIVE != 0

    companion object {

        /**
         * Флаг симметричности отношения
         */
        const val SYMMETRIC = 1

        /**
         * Флаг анти-симметричности отношения
         */
        const val ANTISYMMETRIC = 2

        /**
         * Флаг рефлексивности отношения
         */
        const val REFLEXIVE = 4

        /**
         * Флаг анти-рефлексивности (строгости) отношения
         */
        const val STRICT = 8

        /**
         * Флаг транзитивности отношения
         */
        const val TRANSITIVE = 16

        /**
         * Флаг анти-транзитивности отношения
         */
        const val INTRANSITIVE = 32

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
                    else -> null
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
                    else -> null
                }
            }
        }
    }
}