package dictionaries

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import dictionaries.RelationshipsDictionary.LinerScalePatterns.IS_BETWEEN_PATTERN
import dictionaries.RelationshipsDictionary.LinerScalePatterns.IS_BETWEEN_VAR_COUNT
import dictionaries.RelationshipsDictionary.LinerScalePatterns.IS_CLOSER_TO_THAN_PATTERN
import dictionaries.RelationshipsDictionary.LinerScalePatterns.IS_CLOSER_TO_THAN_VAR_COUNT
import dictionaries.RelationshipsDictionary.LinerScalePatterns.IS_FURTHER_FROM_THAN_PATTERN
import dictionaries.RelationshipsDictionary.LinerScalePatterns.IS_FURTHER_FROM_THAN_VAR_COUNT
import dictionaries.RelationshipsDictionary.LinerScalePatterns.REVERSE_PATTERN
import dictionaries.RelationshipsDictionary.LinerScalePatterns.REVERSE_TRANSITIVE_CLOSURE_PATTERN
import dictionaries.RelationshipsDictionary.LinerScalePatterns.REVERSE_TRANSITIVE_CLOSURE_VAR_COUNT
import dictionaries.RelationshipsDictionary.LinerScalePatterns.REVERSE_VAR_COUNT
import dictionaries.RelationshipsDictionary.LinerScalePatterns.TRANSITIVE_CLOSURE_PATTERN
import dictionaries.RelationshipsDictionary.LinerScalePatterns.TRANSITIVE_CLOSURE_VAR_COUNT
import dictionaries.util.DictionariesUtil.COLUMNS_SEPARATOR
import dictionaries.util.DictionariesUtil.LIST_ITEMS_SEPARATOR
import models.RelationshipModel
import models.RelationshipModel.Companion.RelationType
import models.RelationshipModel.Companion.ScaleType
import util.JenaUtil.POAS_PREF
import util.JenaUtil.genLink
import util.NamingManager
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Словарь свойств
 */
object RelationshipsDictionary {

    // ++++++ Шаблоны вспомогательных правил для отношений порядковых шкал +++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    object LinerScalePatterns {

        const val NUMERATION_RULES_PATTERN = """
            [
            (?var1 <linerPredicate> ?var2)
            noValue(?var3, <linerPredicate>, ?var1)
            ->
            (?var1 <numberPredicate> "1"^^xsd:integer)
            ]
        
            [
            (?var1 <linerPredicate> ?var2)
            noValue(?var2, <numberPredicate>)
            (?var1 <numberPredicate> ?var3)
            addOne(?var3, ?var4)
            ->
            (?var2 <numberPredicate> ?var4)
            ]
        """

        const val REVERSE_VAR_COUNT = 0
        const val REVERSE_PATTERN = """
            (<arg2> <predicate> <arg1>)
        """

        const val TRANSITIVE_CLOSURE_VAR_COUNT = 2
        const val TRANSITIVE_CLOSURE_PATTERN = """
            (<arg1> <numberPredicate> <var1>)
            (<arg2> <numberPredicate> <var2>)
            lessThan(<var1>, <var2>)
        """

        const val REVERSE_TRANSITIVE_CLOSURE_VAR_COUNT = 2
        const val REVERSE_TRANSITIVE_CLOSURE_PATTERN = """
            (<arg1> <numberPredicate> <var1>)
            (<arg2> <numberPredicate> <var2>)
            greaterThan(<var1>, <var2>)
        """

        const val IS_BETWEEN_VAR_COUNT = 3
        const val IS_BETWEEN_PATTERN = """
            (<arg1> <numberPredicate> <var1>)
            (<arg2> <numberPredicate> <var2>)
            (<arg3> <numberPredicate> <var3>)
            greaterThan(<var1>, <var2>)
            lessThan(<var1>, <var3>)
        """

        const val IS_CLOSER_TO_THAN_VAR_COUNT = 7
        const val IS_CLOSER_TO_THAN_PATTERN = """
            (<arg1> <numberPredicate> <var1>)
            (<arg2> <numberPredicate> <var2>)
            (<arg3> <numberPredicate> <var3>)
            difference(<var2>, <var1>, <var4>)
            difference(<var2>, <var3>, <var5>)
            absoluteValue(<var4>, <var6>)
            absoluteValue(<var5>, <var7>)
            lessThan(<var6>, <var7>)
        """

        const val IS_FURTHER_FROM_THAN_VAR_COUNT = 7
        const val IS_FURTHER_FROM_THAN_PATTERN = """
            (<arg1> <numberPredicate> <var1>)
            (<arg2> <numberPredicate> <var2>)
            (<arg3> <numberPredicate> <var3>)
            difference(<var2>, <var1>, <var4>)
            difference(<var2>, <var3>, <var5>)
            absoluteValue(<var4>, <var6>)
            absoluteValue(<var5>, <var7>)
            greaterThan(<var6>, <var7>)
        """
    }

    object PartialScalePatterns {
        // TODO
    }

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список отношений
     */
    private val relationships: MutableList<RelationshipModel> = mutableListOf()

    /**
     * Названия предикатов, задающих нумерацию для шкал
     *
     * key - имя отношения,
     * val - имя предиката нумерации
     */
    private val scalePredicates: MutableMap<String, String> = HashMap()

    /**
     * ID предиката
     */
    private var scalePredicateId = 0
        get() = ++field

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Инициализирует словарь данными
     * @param path Путь с фалу с данными для словаря
     */
    internal fun init(path: String) {
        // Очищаем старые значения
        relationships.clear()
        scalePredicates.clear()

        // Создаем объекты
        val parser = CSVParserBuilder().withSeparator(COLUMNS_SEPARATOR).build()
        val bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)
        val csvReader = CSVReaderBuilder(bufferedReader).withCSVParser(parser).build()

        // Считываем файл
        csvReader.use { reader ->
            val rows = reader.readAll()

            rows.forEach { row ->
                val name = row[0]
                val parent = row[1].ifBlank { null }
                val args = row[2].split(LIST_ITEMS_SEPARATOR).filter {
                    it.isNotBlank()
                }.ifEmpty { null }
                val scaleType = ScaleType.valueOf(row[3])
                val isRelation = row[4].toBoolean()
                val relationType = RelationType.valueOf(row[5])
                val scaleRelations = row[6].split(LIST_ITEMS_SEPARATOR).filter {
                    it.isNotBlank()
                }.ifEmpty { null }
                var flags = row[7].toInt()

                require(!isRelation || relationType != null) {
                    "Не указан тип связи между классами."
                }
                require(scaleType == null || scaleRelations != null && scaleRelations.size == 6) {
                    "Некорректное количество отношений порядковой шкалы для отношения $name."
                }
                require(args != null) {
                    "Не указаны аргументы для отношения $name."
                }
                when (scaleType) {
                    ScaleType.Liner -> {
                        require(flags == 6 || flags == 0) {
                            "Некорректный набор флагов для отношения линейного порядка."
                        }

                        scalePredicates[name] = name + scalePredicateId + NamingManager.PROTECTIVE_CHARS
                        flags = 6
                    }

                    ScaleType.Partial -> {
                        require(flags == 22 || flags == 0) {
                            "Некорректный набор флагов для отношения частичного порядка."
                        }

                        scalePredicates[name] = name + scalePredicateId + NamingManager.PROTECTIVE_CHARS
                        flags = 22
                    }

                    else -> require(flags < 64) {
                        "Некорректный набор флагов."
                    }
                }

                when (scaleType) {
                    ScaleType.Liner -> {
                        require(scaleRelations != null)

                        val scalePredicate = scalePredicates[name]!!

                        relationships.add(
                            RelationshipModel(
                                name = scaleRelations[0],
                                parent = null,
                                argsClasses = args,
                                scaleType = null,
                                relationType = null,
                                reverseRelName = null,
                                transitiveClosureRelName = null,
                                reverseTransitiveClosureRelName = null,
                                isBetweenRelName = null,
                                isCloserToThanRelName = null,
                                isFurtherFromThanRelName = null,
                                flags = flags,
                                varsCount = REVERSE_VAR_COUNT,
                                head = REVERSE_PATTERN.replace("<predicate>", genLink(POAS_PREF, name)),
                                rules = null
                            )
                        )

                        relationships.add(
                            RelationshipModel(
                                name = scaleRelations[1],
                                parent = null,
                                argsClasses = args,
                                scaleType = null,
                                relationType = null,
                                reverseRelName = null,
                                transitiveClosureRelName = null,
                                reverseTransitiveClosureRelName = null,
                                isBetweenRelName = null,
                                isCloserToThanRelName = null,
                                isFurtherFromThanRelName = null,
                                flags = 16,
                                varsCount = TRANSITIVE_CLOSURE_VAR_COUNT,
                                head = TRANSITIVE_CLOSURE_PATTERN.replace(
                                    "<numberPredicate>",
                                    genLink(POAS_PREF, scalePredicate)
                                ),
                                rules = null
                            )
                        )

                        relationships.add(
                            RelationshipModel(
                                name = scaleRelations[2],
                                parent = null,
                                argsClasses = args,
                                scaleType = null,
                                relationType = null,
                                reverseRelName = null,
                                transitiveClosureRelName = null,
                                reverseTransitiveClosureRelName = null,
                                isBetweenRelName = null,
                                isCloserToThanRelName = null,
                                isFurtherFromThanRelName = null,
                                flags = 16,
                                varsCount = REVERSE_TRANSITIVE_CLOSURE_VAR_COUNT,
                                head = REVERSE_TRANSITIVE_CLOSURE_PATTERN.replace(
                                    "<numberPredicate>",
                                    genLink(POAS_PREF, scalePredicate)
                                ),
                                rules = null
                            )
                        )

                        relationships.add(
                            RelationshipModel(
                                name = scaleRelations[3],
                                parent = null,
                                argsClasses = args.plus(args[0]),
                                scaleType = null,
                                relationType = null,
                                reverseRelName = null,
                                transitiveClosureRelName = null,
                                reverseTransitiveClosureRelName = null,
                                isBetweenRelName = null,
                                isCloserToThanRelName = null,
                                isFurtherFromThanRelName = null,
                                flags = 0,
                                varsCount = IS_BETWEEN_VAR_COUNT,
                                head = IS_BETWEEN_PATTERN.replace(
                                    "<numberPredicate>",
                                    genLink(POAS_PREF, scalePredicate)
                                ),
                                rules = null
                            )
                        )

                        relationships.add(
                            RelationshipModel(
                                name = scaleRelations[4],
                                parent = null,
                                argsClasses = args.plus(args[0]),
                                scaleType = null,
                                relationType = null,
                                reverseRelName = null,
                                transitiveClosureRelName = null,
                                reverseTransitiveClosureRelName = null,
                                isBetweenRelName = null,
                                isCloserToThanRelName = null,
                                isFurtherFromThanRelName = null,
                                flags = 0,
                                varsCount = IS_CLOSER_TO_THAN_VAR_COUNT,
                                head = IS_CLOSER_TO_THAN_PATTERN.replace(
                                    "<numberPredicate>",
                                    genLink(POAS_PREF, scalePredicate)
                                ),
                                rules = null
                            )
                        )

                        relationships.add(
                            RelationshipModel(
                                name = scaleRelations[5],
                                parent = null,
                                argsClasses = args.plus(args[0]),
                                scaleType = null,
                                relationType = null,
                                reverseRelName = null,
                                transitiveClosureRelName = null,
                                reverseTransitiveClosureRelName = null,
                                isBetweenRelName = null,
                                isCloserToThanRelName = null,
                                isFurtherFromThanRelName = null,
                                flags = 0,
                                varsCount = IS_FURTHER_FROM_THAN_VAR_COUNT,
                                head = IS_FURTHER_FROM_THAN_PATTERN.replace(
                                    "<numberPredicate>",
                                    genLink(POAS_PREF, scalePredicate)
                                ),
                                rules = null
                            )
                        )
                    }

                    ScaleType.Partial -> {
                        TODO("Отношения частичного порядка")
                    }

                    else -> {}
                }

                val varsCount = if (args.size == 2) {
                    0
                } else {
                    1
                }
                val head = if (args.size == 2) {
                    "(<arg1> ${genLink(POAS_PREF, name)} <arg2>)\n"
                } else {
                    var tmp = "(<arg1> ${genLink(POAS_PREF, name)} <var1>)\n"

                    args.forEachIndexed { index, _ ->
                        if (index != 0) {
                            tmp += "(<var1> ${genLink(POAS_PREF, name)} <arg${index + 1}>)\n"
                        }
                    }

                    tmp
                }

                relationships.add(
                    RelationshipModel(
                        name = name,
                        parent = parent,
                        argsClasses = args,
                        scaleType = scaleType,
                        relationType = relationType,
                        reverseRelName = scaleRelations?.get(0),
                        transitiveClosureRelName = scaleRelations?.get(1),
                        reverseTransitiveClosureRelName = scaleRelations?.get(2),
                        isBetweenRelName = scaleRelations?.get(3),
                        isCloserToThanRelName = scaleRelations?.get(4),
                        isFurtherFromThanRelName = scaleRelations?.get(5),
                        flags = flags,
                        varsCount = varsCount,
                        head = head,
                        rules = null
                    )
                )
            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    internal fun forEach(block: (RelationshipModel) -> Unit) {
        relationships.forEach(block)
    }

    /**
     * Получить предикат порядковой шкалы для отношения
     * @param name Имя отношения
     */
    internal fun getScalePredicate(name: String) = scalePredicates[name]

    /**
     * Получить модель отношения по имени
     * @param name Имя отношения
     */
    internal fun get(name: String) = relationships.firstOrNull { it.name == name }

    /**
     * Проверяет корректность содержимого словаря
     * @throws IllegalArgumentException
     */
    fun validate() {
        relationships.forEach {
            it.validate()
            require(it.scaleType == null || scalePredicates.containsKey(it.name))
        }
    }

    /**
     * Существует ли отношение
     * @param name Имя отношения
     */
    fun exist(name: String) = relationships.any { it.name == name }

    /**
     * Получить список классов аргументов
     * @param name Имя отношения
     */
    fun args(name: String) = get(name)?.argsClasses

    /**
     * Получить количество переменных в шаблоне правила
     * @param name Имя отношения
     */
    fun varsCount(name: String) = get(name)?.varsCount

    /**
     * Получить голову правила для проверки отношения
     * @param name Имя отношения
     */
    fun head(name: String) = get(name)?.head

    /**
     * Получить завершенные правила для проверки отношения
     * @param name Имя отношения
     */
    fun rules(name: String) = get(name)?.rules
}