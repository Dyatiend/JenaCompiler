package models

import dictionaries.ClassesDictionary
import dictionaries.RelationshipsDictionary

/**
 * Модель отношения в предметной области
 * @param name Имя отношения
 * @param parent Модель родительского отношения
 * @param argsClasses Модели классов аргументов
 * @param scaleType Тип порядковой шкалы
 * @param relationType Тип связи между классами
 * @param reverse Обратное отношение
 * @param transitiveClosure Отношение транзитивного замыкания
 * @param reverseTransitiveClosure Отношение обратного транзитивного замыкания
 * @param between Отношение типа "между"
 * @param closerToThan Отношение типа "ближе"
 * @param furtherFromThan Отношение типа "дальше"
 * @param flags Флаги свойств отношения
 * @see ClassModel
 */
data class RelationshipModel(
    val name: String,
    val parent: RelationshipModel?,
    val argsClasses: List<ClassModel>,
    val scaleType: ScaleType?,
    val relationType: RelationType?,
    val reverse: RelationshipModel?,
    val transitiveClosure: RelationshipModel?,
    val reverseTransitiveClosure: RelationshipModel?,
    val between: RelationshipModel?,
    val closerToThan: RelationshipModel?,
    val furtherFromThan: RelationshipModel?,
    val flags: Int
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя отношения."
        }
        require(!RelationshipsDictionary.exist(name, argsClasses)) {
            "Отношение $name уже объявлено в словаре."
        }
        require(parent == null || RelationshipsDictionary.exist(parent)) {
            "Отношение ${parent?.name} не объявлено в словаре."
        }
        require(argsClasses.size >= 2) {
            "Отношение $name имеет меньше двух аргументов."
        }
        argsClasses.forEach {
            require(ClassesDictionary.exist(it.name)) {
                "Класс $it не объявлен в словаре."
            }
        }
        require(
            scaleType == null || reverse != null
                    && transitiveClosure != null && reverseTransitiveClosure != null
                    && between != null && closerToThan != null && furtherFromThan != null
        ) {
            "Не указано одно из отношений порядковой шкалы для отношения $name."
        }
        require(scaleType == null || argsClasses.size == 2 && argsClasses[0] == argsClasses[1]) {
            "Отношение порядковой шкалы может быть только бинарным и только между объектами одного класса."
        }
        require(flags < 128) {
            "Некорректный набор флагов."
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