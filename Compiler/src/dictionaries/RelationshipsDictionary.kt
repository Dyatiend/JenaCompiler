package dictionaries

import util.JenaUtil.POAS_PREF
import util.JenaUtil.RDF_PREF
import util.JenaUtil.genLink

/**
 * Словарь свойств
 */
object RelationshipsDictionary {

    // ++++++ Шаблоны вспомогательных правил для свойств линейного порядка +++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private const val ASCENDING_NUMERATION_RULES_PATTERN = """
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

    private const val DESCENDING_NUMERATION_RULES_PATTERN = """
        [
        (?var1 <linerPredicate> ?var2)
        noValue(?var2, <linerPredicate>, ?var3)
        ->
        (?var1 <numberPredicate> "1"^^xsd:integer)
        ]
        [
        (?var1 <linerPredicate> ?var2)
        noValue(?var1, <numberPredicate>)
        (?var2 <numberPredicate> ?var3)
        addOne(?var3, ?var4)
        ->
        (?var1 <numberPredicate> ?var4)
        ]
    """

    private const val LEFT_OF_VAR_COUNT = 2
    private const val LEFT_OF_PATTERN = """
        (<arg1> <numberPredicate> <var1>)
        (<arg2> <numberPredicate> <var2>)
        lessThan(<var1>, <var2>)
    """

    private const val RIGHT_OF_VAR_COUNT = 2
    private const val RIGHT_OF_PATTERN = """
        (<arg1> <numberPredicate> <var1>)
        (<arg2> <numberPredicate> <var2>)
        greaterThan(<var1>, <var2>)
    """

    private const val IS_BETWEEN_VAR_COUNT = 3
    private const val IS_BETWEEN_PATTERN = """
        (<arg1> <numberPredicate> <var1>)
        (<arg2> <numberPredicate> <var2>)
        (<arg3> <numberPredicate> <var3>)
        greaterThan(<var1>, <var2>)
        lessThan(<var1>, <var3>)
    """

    private const val IS_CLOSER_TO_THAN_VAR_COUNT = 7
    private const val IS_CLOSER_TO_THAN_PATTERN = """
        (<arg1> <numberPredicate> <var1>)
        (<arg2> <numberPredicate> <var2>)
        (<arg3> <numberPredicate> <var3>)
        difference(<var2>, <var1>, <var4>)
        difference(<var2>, <var3>, <var5>)
        absoluteValue(<var4>, <var6>)
        absoluteValue(<var5>, <var7>)
        lessThan(<var6>, <var7>)
    """

    private const val IS_FURTHER_FROM_THAN_VAR_COUNT = 7
    private const val IS_FURTHER_FROM_THAN_PATTERN = """
        (<arg1> <numberPredicate> <var1>)
        (<arg2> <numberPredicate> <var2>)
        (<arg3> <numberPredicate> <var3>)
        difference(<var2>, <var1>, <var4>)
        difference(<var2>, <var3>, <var5>)
        absoluteValue(<var4>, <var6>)
        absoluteValue(<var5>, <var7>)
        greaterThan(<var6>, <var7>)
    """

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * Использованы ли отношения линейной шкалы
     */
    private var isLinerScaleUsed: Boolean = false

    /**
     * список отношений линейной шкалы
     */
    private val linerScaleRelationships = listOf("leftOf", "rightOf", "isBetween", "isCloserToThan", "isFurtherFromThan")

    /**
     * Вспомогательные правила для работы правил линейной шкалы
     */
    private var auxiliaryLinerScaleRules = ""

    /**
     * Отношения
     * key - имя отношения
     * val - список классов аргументов
     */
    private val relationships: MutableMap<String, List<String>> = HashMap()

    /**
     * Имена отношений линейной шкалы
     * key - First - общее имя отношения линейной шкалы, Second - имя предиката линейной шкалы
     * val - конкретное имя отношения
     */
    private val linerRelationships: MutableMap<Pair<String, String>, String> = HashMap()

    /**
     * Шаблоны правил для отношений
     * row - Имя отношения
     * col - Количество вспомогательных переменных в шаблоне
     * val - First - шаблон правила, Second - вспомогательные правила
     */
    private val patterns: MutableMap<String, Pair<Int, Pair<String, String>>> = HashMap()

    /**
     * Названия предикатов, задающих нумерацию
     * key - предикат линейной шкалы
     * val - предикат нумерации
     */
    private val numberPredicates: MutableMap<String, String> = HashMap()
    
    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    fun init(path: String) {
        // Очищаем старые значения
        relationships.clear()
        linerRelationships.clear()
        patterns.clear()
        numberPredicates.clear()
        auxiliaryLinerScaleRules = ""
        isLinerScaleUsed = false

        // TODO: чтение из файла
        // TODO?: проекция на линейную шкалу?
        // FIXME?: не считать классы линейной шкалой?

        // Подключаем "библиотеку"
        isLinerScaleUsed = true

        // Добавляем свойства
        relationships["has"] = listOf("element", "token")
        relationships["belongsTo"] = listOf("token", "element")
        relationships["directlyLeftOf"] = listOf("token", "token")
        relationships["directlyRightOf"] = listOf("token", "token")
        relationships["isOperandOf"] = listOf("element", "element")
        relationships["isOperatorTo"] = listOf("element", "element")

        relationships["token_leftOf"] = listOf("token", "token")
        relationships["token_rightOf"] = listOf("token", "token")
        relationships["token_isBetween"] = listOf("token", "token", "token")
        relationships["token_isCloserToThan"] = listOf("token", "token", "token")
        relationships["token_isFurtherFromThan"] = listOf("token", "token", "token")

        relationships["class_leftOf"] = listOf("class", "class")
        relationships["class_rightOf"] = listOf("class", "class")
        relationships["class_isBetween"] = listOf("class", "class", "class")
        relationships["class_isCloserToThan"] = listOf("class", "class", "class")
        relationships["class_isFurtherFromThan"] = listOf("class", "class", "class")

        relationships["state_leftOf"] = emptyList()
        relationships["state_rightOf"] = emptyList()
        relationships["state_isBetween"] = emptyList()
        relationships["state_isCloserToThan"] = emptyList()
        relationships["state_isFurtherFromThan"] = emptyList()

        linerRelationships[Pair("leftOf", "directlyLeftOf")] = "token_leftOf"
        linerRelationships[Pair("leftOf", "subClassOf")] = "class_leftOf"
        linerRelationships[Pair("leftOf", "state_directlyLeftOf")] = "state_leftOf"

        linerRelationships[Pair("rightOf", "directlyLeftOf")] = "token_rightOf"
        linerRelationships[Pair("rightOf", "subClassOf")] = "class_rightOf"
        linerRelationships[Pair("rightOf", "state_directlyLeftOf")] = "state_rightOf"

        linerRelationships[Pair("isBetween", "directlyLeftOf")] = "token_isBetween"
        linerRelationships[Pair("isBetween", "subClassOf")] = "class_isBetween"
        linerRelationships[Pair("isBetween", "state_directlyLeftOf")] = "state_isBetween"

        linerRelationships[Pair("isCloserToThan", "directlyLeftOf")] = "token_isCloserToThan"
        linerRelationships[Pair("isCloserToThan", "subClassOf")] = "class_isCloserToThan"
        linerRelationships[Pair("isCloserToThan", "state_directlyLeftOf")] = "state_isCloserToThan"

        linerRelationships[Pair("isFurtherFromThan", "directlyLeftOf")] = "token_isFurtherFromThan"
        linerRelationships[Pair("isFurtherFromThan", "subClassOf")] = "class_isFurtherFromThan"
        linerRelationships[Pair("isFurtherFromThan", "state_directlyLeftOf")] = "state_isFurtherFromThan"

        // Добавляем вспомогательные правила
        var tokenNumerationRules = ASCENDING_NUMERATION_RULES_PATTERN
        tokenNumerationRules = tokenNumerationRules.replace("<linerPredicate>", genLink(POAS_PREF, "directlyLeftOf"))
        tokenNumerationRules = tokenNumerationRules.replace("<numberPredicate>", genLink(POAS_PREF, "__tokenNumber__"))

        var classNumerationRules = DESCENDING_NUMERATION_RULES_PATTERN
        classNumerationRules = classNumerationRules.replace("<linerPredicate>", genLink(RDF_PREF, "subClassOf"))
        classNumerationRules = classNumerationRules.replace("<numberPredicate>", genLink(POAS_PREF, "__classNumber__"))

        var stateNumerationRules = ASCENDING_NUMERATION_RULES_PATTERN
        stateNumerationRules = stateNumerationRules.replace("<linerPredicate>", genLink(RDF_PREF, "state_directlyLeftOf"))
        stateNumerationRules = stateNumerationRules.replace("<numberPredicate>", genLink(POAS_PREF, "__stateNumber__"))

        auxiliaryLinerScaleRules += tokenNumerationRules + classNumerationRules + stateNumerationRules

        // Сохраняем предикаты нумерации
        numberPredicates["directlyLeftOf"] = "__tokenNumber__"
        numberPredicates["subClassOf"] = "__classNumber__"
        numberPredicates["state_directlyLeftOf"] = "__stateNumber__"

        // Добавляем шаблоны
        patterns["has"] = Pair(0, Pair("(<arg1> ${genLink(POAS_PREF, "has")} <arg2>)\n", ""))
        patterns["belongsTo"] = Pair(0, Pair("(<arg1> ${genLink(POAS_PREF, "belongsTo")} <arg2>)\n", ""))
        patterns["directlyLeftOf"] = Pair(0, Pair("(<arg1> ${genLink(POAS_PREF, "directlyLeftOf")} <arg2>)\n", ""))
        patterns["directlyRightOf"] = Pair(0, Pair("(<arg1> ${genLink(POAS_PREF, "directlyRightOf")} <arg2>)\n", ""))
        patterns["isOperandOf"] = Pair(0, Pair("(<arg1> ${genLink(POAS_PREF, "isOperandOf")} <arg2>)\n", ""))
        patterns["isOperatorTo"] = Pair(0, Pair("(<arg1> ${genLink(POAS_PREF, "isOperatorTo")} <arg2>)\n", ""))

        patterns["leftOf"] = Pair(LEFT_OF_VAR_COUNT, Pair(LEFT_OF_PATTERN, ""))
        patterns["rightOf"] = Pair(RIGHT_OF_VAR_COUNT, Pair(RIGHT_OF_PATTERN, ""))
        patterns["isBetween"] = Pair(IS_BETWEEN_VAR_COUNT, Pair(IS_BETWEEN_PATTERN, ""))
        patterns["isCloserToThan"] = Pair(IS_CLOSER_TO_THAN_VAR_COUNT, Pair(IS_CLOSER_TO_THAN_PATTERN, ""))
        patterns["isFurtherFromThan"] = Pair(IS_FURTHER_FROM_THAN_VAR_COUNT, Pair(IS_FURTHER_FROM_THAN_PATTERN, ""))
    }
    
    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /**
     * Использованы ли отношения линейной шкалы
     * @return true - если использованы, иначе - false
     */
    fun isLinerScaleUsed(): Boolean = isLinerScaleUsed

    /**
     * Получить вспомогательные правила для работы правил линейной шкалы
     * @return Вспомогательные правила для работы правил линейной шкалы
     */
    fun auxiliaryLinerScaleRules(): String = auxiliaryLinerScaleRules

    /**
     * Ялвяется ли отношение частью "библиотеки" линейной шкалы
     */
    fun isLinerScaleRelationship(relationshipName: String): Boolean = linerScaleRelationships.contains(relationshipName)

    /**
     * Существует ли отношение
     * @param relationshipName Имя отношения
     * @return true - если существует, иначе - false
     */
    fun exist(relationshipName: String): Boolean = relationships.containsKey(relationshipName)

    /**
     * Получить список классов аргументов
     * @param relationshipName Имя отношения
     * @param linerPredicate Предикат линейной шкалы
     * @return Список классов аргументов
     */
    fun args(relationshipName: String, linerPredicate: String? = null): List<String>? {
        return if (isLinerScaleRelationship(relationshipName) && linerPredicate != null) {
            relationships[linerRelationships[Pair(relationshipName, linerPredicate)]]
        } else {
            relationships[relationshipName]
        }
    }

    /**
     * Получить количество переменных в шаблоне правила
     * @param relationshipName Имя отношения
     * @param linerPredicate Предикат линейной шкалы
     * @return Количество переменных в шаблоне
     */
    fun varCount(relationshipName: String, linerPredicate: String? = null): Int? {
        return if (isLinerScaleRelationship(relationshipName) && linerPredicate != null) {
            patterns[linerRelationships[Pair(relationshipName, linerPredicate)]]?.first
        } else {
            if (!exist(relationshipName)) null else patterns[relationshipName]?.first
        }
    }

    /**
     * Получить шаблон правила
     * @param relationshipName Имя отношения
     * @param linerPredicate Предикат линейной шкалы
     * @return Шаблон правила
     */
    fun pattern(relationshipName: String, linerPredicate: String? = null): Pair<String, String>? {
        return if (isLinerScaleRelationship(relationshipName) && linerPredicate != null && numberPredicates.containsKey(linerPredicate)) {
            val pattern = pattern(relationshipName)!!

            var ruleHead = pattern.first
            ruleHead = ruleHead.replace("<numberPredicate>", genLink(POAS_PREF, numberPredicates[linerPredicate]!!))

            Pair(ruleHead, pattern.second)
        } else {
            patterns[relationshipName]?.second
        }
    }
}