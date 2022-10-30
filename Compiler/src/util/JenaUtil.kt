package util

/**
 * Содержит различные утилитарные методы и переменные, используемые при генерации правил
 */
object JenaUtil {

    // +++++++++++++++++++++++++++++++++ Префиксы ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // TODO?: пользовательские префиксы?

    /**
     * POAS префикс
     */
    const val POAS_PREF = "http://www.vstu.ru/poas/code#"

    /**
     * XSD префикс
     */
    const val XSD_PREF = "http://www.w3.org/2001/XMLSchema#"

    /**
     * RDF префикс
     */
    const val RDF_PREF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"

    // ++++++++++++++++++++++++++++ Постоянные элементы ++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Предикат переменной
     */
    const val VAR_PREDICATE = "__var__"

    /**
     * Маркировка паузы
     */
    const val PAUSE_MARK = "<pause>"

    // +++++++++++++++++++++++++++++++++ Шаблоны +++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Шаблон триплета правила
     */
    private const val TRIPLE_PATTERN = "(<subj> <predicate> <obj>)\n"

    /**
     * Основной шаблон правила с возвращаемым значением
     */
    private const val MAIN_RULE_PATTERN = "[\n<ruleHead>\nmakeSkolem(<skolemName>)\n->\n(<skolemName> <resPredicateName> <resVarName>)\n]\n"

    /**
     * Шаблон для boolean правила
     */
    private const val BOOLEAN_RULE_PATTERN = "[\n<ruleHead>\nmakeSkolem(<skolemName>)\n->\n(<skolemName> <resPredicateName> \"true\"^^xsd:boolean)\n]\n"

    // ++++++++++++++++++++++++++++ Методы для генерации +++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Сгенерировать строковую константу
     * @param value Строковая константа
     * @return Запись строковой константы для правила
     */
    fun genStingVal(value: String): String = "\"$value\"^^${XSD_PREF}string"

    /**
     * Сгенерировать булеву константу
     * @param value Булева константа
     * @return Запись булевой константы для правила
     */
    fun genBooleanVal(value: String): String = "\"$value\"^^${XSD_PREF}boolean"

    /**
     * Сгенерировать целочисленную константу
     * @param value Целочисленная константа
     * @return Запись целочисленной константы для правила
     */
    @JvmStatic
    fun genIntegerVal(value: String): String = "\"$value\"^^${XSD_PREF}integer"

    /**
     * Сгенерировать дробную константу
     * @param value Дробная константа
     * @return Запись дробной константы для правила
     */
    fun genDoubleVal(value: String): String = "\"$value\"^^${XSD_PREF}double"

    /**
     * Сгенерировать ссылку в правиле
     * @param pref Префикс
     * @param obj Имя
     * @return Ссылка в правиле
     */
    @JvmStatic
    fun genLink(pref: String, obj: String): String = "$pref$obj"

    /**
     * Сгенерировать переменную
     * @param name Имя переменной
     * @return Имя переменной для правила
     */
    fun genVar(name: String): String = "?$name"

    /**
     * Сгенерировать примитив, проверяющий эквивалентность
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий эквивалентность
     */
    fun genEqualPrim(first: String, second: String): String = "equal($first,$second)\n"

    /**
     * Сгенерировать примитив, проверяющий неэквивалентность
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий неэквивалентность
     */
    fun genNotEqualPrim(first: String, second: String): String = "notEqual($first,$second)\n"

    /**
     * Сгенерировать примитив, проверяющий, что первый операнд меньше второго
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий, что первый операнд меньше второго
     */
    fun genLessThanPrim(first: String, second: String): String = "lessThan($first,$second)\n"

    /**
     * Сгенерировать примитив, проверяющий, что первый операнд больше второго
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий, что первый операнд больше второго
     */
    fun genGreaterThanPrim(first: String, second: String): String = "greaterThan($first,$second)\n"

    /**
     * Сгенерировать примитив, проверяющий, что первый операнд меньше или равен второму
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий, что первый операнд меньше или равен второму
     */
    fun genLessEqualPrim(first: String, second: String): String = "le($first,$second)\n"

    /**
     * Сгенерировать примитив, проверяющий, что первый операнд больше или равен второму
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий, что первый операнд больше или равен второму
     */
    fun genGreaterEqualPrim(first: String, second: String): String = "ge($first,$second)\n"

    /**
     * Сгенерировать примитив, проверяющий отсутствие у объекта указанного предиката
     * @param subj Субъект
     * @param predicate Предикат
     * @return Примитив, проверяющий отсутствие у объекта указанной предиката
     */
    fun genNoValuePrim(subj: String, predicate: String): String = "noValue($subj,$predicate)\n"

    /**
     * Сгенерировать примитив, проверяющий отсутствие у объекта указанного предиката с указанным значением
     * @param subj Субъект
     * @param predicate Предикат
     * @param obj Объект
     * @return Примитив, проверяющий отсутствие у объекта указанной предиката с указанным значением
     */
    fun genNoValuePrim(subj: String, predicate: String, obj: String): String = "noValue($subj,$predicate,$obj)\n"

    /**
     * Сгенерировать примитив, создающий сколем с указанным именем
     * @param skolemName Имя
     * @return Примитив, создающий сколем с указанным именем
     */
    fun genMakeSkolemPrim(skolemName: String): String = "makeSkolem($skolemName)\n"

    /**
     * Сгенерировать примитив, записывающий значение одно переменной в другую
     * @param from Имя переменной, из которой берется значение
     * @param to Имя переменной, в которую записывается значение
     * @return Примитив, записывающий значение одно переменной в другую
     */
    fun genBindPrim(from: String, to: String): String = "bind($from,$to)\n"

    /**
     * Сгенерировать триплет правила
     * @param subj Субъект
     * @param predicate Предикат
     * @param obj Объект
     * @return Триплет правила
     */
    fun genTriple(subj: String, predicate: String, obj: String): String {
        var res = TRIPLE_PATTERN
        res = res.replace("<subj>", subj)
        res = res.replace("<predicate>", predicate)
        res = res.replace("<obj>", obj)
        return res
    }

    /**
     * Сгенерировать правило с возвращаемым значением
     * @param ruleHead Голова правила
     * @param skolemName Имя сколема
     * @param resPredicateName Предикат, указывающий на результат
     * @param resVarName Переменная, содержащая результат
     * @return Правило
     */
    fun genRule(ruleHead: String, skolemName: String, resPredicateName: String, resVarName: String): String {
        var rule = MAIN_RULE_PATTERN
        rule = rule.replace("<ruleHead>", ruleHead)
        rule = rule.replace("<skolemName>", skolemName)
        rule = rule.replace("resPredicateName", resPredicateName)
        rule = rule.replace("<resVarName>", resVarName)
        return rule
    }

    /**
     * Сгенерировать булево правило
     * @param ruleHead Голова правила
     * @param skolemName Имя сколема
     * @param resPredicateName Предикат, являющийся флагом результата
     * @return Правило
     */
    fun genBooleanRule(ruleHead: String, skolemName: String, resPredicateName: String): String {
        var rule = BOOLEAN_RULE_PATTERN
        rule = rule.replace("<ruleHead>", ruleHead)
        rule = rule.replace("<skolemName>", skolemName)
        rule = rule.replace("<resPredicateName>", resPredicateName)
        return rule
    }
}