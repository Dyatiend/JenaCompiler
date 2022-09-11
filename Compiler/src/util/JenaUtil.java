package util;

/**
 * Содержит различные утилитарные методы и переменные, используемые при генерации правил
 */
public class JenaUtil {

    // +++++++++++++++++++++++++++++++++ Префиксы ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // TODO?: пользовательские префиксы?

    /**
     * POAS префикс
     */
    public static final String POAS_PREF = "poas";

    /**
     * XSD префикс
     */
    public static final String XSD_PREF = "xsd";

    /**
     * RDF префикс
     */
    public static final String RDF_PREF = "rdf";

    /**
     * URL POAS префикса
     */
    public static final String POAS_PREF_URL = "http://www.vstu.ru/poas/code#";

    /**
     * URL XSD префикса
     */
    public static final String XSD_PREF_URL = "http://www.w3.org/2001/XMLSchema#";

    /**
     * URL RDF префикса
     */
    public static final String RDF_PREF_URL = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    // ++++++++++++++++++++++++++++ Постоянные элементы ++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Предикат переменной
     */
    public static final String VAR_PRED = POAS_PREF + ":" + "~var~";

    /**
     * Маркировка паузы
     */
    public static final String PAUSE_MARK = "<pause>";

    // +++++++++++++++++++++++++++++++++ Шаблоны +++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Шаблон триплета правила
     */
    public static final String TRIPLE_PATTERN = "(<subj> <pred> <obj>)";

    /**
     * Основной шаблон правила с возвращаемым значением
     */
    public static final String MAIN_RULE_PATTERN =
            "[<ruleHead>makeSkolem(<skolemArgs>)->(<skolemName> <resPredName> <resVarName>)]";

    /**
     * Шаблон для boolean правила
     */
    public static final String BOOLEAN_RULE_PATTERN =
            "[<ruleHead>makeSkolem(<skolemArgs>)->(<skolemName> <resPredName> 1)]";

    // ++++++++++++++++++++++++++++ Методы для генерации +++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Сгенерировать строковую константу
     * @param value Строковая константа
     * @return Запись строковой константы для правила
     */
    public static String genStingVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":string";
    }

    /**
     * Сгенерировать булеву константу
     * @param value Булева константа
     * @return Запись булевой константы для правила
     */
    public static String genBooleanVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":boolean";
    }

    /**
     * Сгенерировать целочисленную константу
     * @param value Целочисленная константа
     * @return Запись целочисленной константы для правила
     */
    public static String genIntegerVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":integer";
    }

    /**
     * Сгенерировать дробную константу
     * @param value Дробная константа
     * @return Запись дробной константы для правила
     */
    public static String genDoubleVal(String value) {
        return "\"" + value + "\"^^" + XSD_PREF + ":double";
    }

    /**
     * Сгенерировать ссылку в правиле
     * @param pref Префикс
     * @param obj Имя
     * @return Ссылка в правиле
     */
    public static String genLink(String pref, String obj) { return pref + ":" + obj; }

    /**
     * Сгенерировать переменную
     * @param name Имя переменной
     * @return Имя переменной для правила
     */
    public static String genVar(String name) { return "?" + name; }

    /**
     * Сгенерировать примитив, проверяющий эквивалентность
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий эквивалентность
     */
    public static String genEqualPrim(String first, String second) {
        return "equal(" + first + "," + second + ")";
    }

    /**
     * Сгенерировать примитив, проверяющий неэквивалентность
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий неэквивалентность
     */
    public static String genNotEqualPrim(String first, String second) {
        return "notEqual(" + first + "," + second + ")";
    }

    /**
     * Сгенерировать примитив, проверяющий, что первый операнд меньше второго
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий, что первый операнд меньше второго
     */
    public static String genLessThanPrim(String first, String second) {
        return "lessThan(" + first + "," + second + ")";
    }

    /**
     * Сгенерировать примитив, проверяющий, что первый операнд больше второго
     * @param first Первый операнд
     * @param second Второй операнд
     * @return Примитив, проверяющий, что первый операнд больше второго
     */
    public static String genGreaterThanPrim(String first, String second) {
        return "greaterThan(" + first + "," + second + ")";
    }

    /**
     * Сгенерировать примитив, проверяющий отсутствие у объекта указанного предиката
     * @param subj Субъект
     * @param pred Предикат
     * @return Примитив, проверяющий отсутствие у объекта указанной предиката
     */
    public static String genNoValuePrim(String subj, String pred) {
        return "noValue(" + subj + "," + pred + ")";
    }

    /**
     * Сгенерировать примитив, создающий сколем с указанными аргументами
     * @param skolemArgs Аргументы
     * @return Примитив, создающий сколем с указанными аргументами
     */
    public static String genMakeSkolemPrim(String skolemArgs) {
        return "makeSkolem(" + skolemArgs+ ")";
    }

    /**
     * Сгенерировать примитив, записывающий значение одно переменной в другую
     * @param from Имя переменной, из которой берется значение
     * @param to Имя переменной, в которую записывается значение
     * @return Примитив, записывающий значение одно переменной в другую
     */
    public static String genBindPrim(String from, String to) {
        return "bind(" + from + "," + to + ")";
    }

    /**
     * Сгенерировать триплет правила
     * @param subj Субъект
     * @param pred Предикат
     * @param obj Объект
     * @return Триплет правила
     */
    public static String genTriple(String subj, String pred, String obj) {
        String res = TRIPLE_PATTERN;

        res = res.replace("<subj>", subj);
        res = res.replace("<pred>", pred);
        res = res.replace("<obj>", obj);

        return res;
    }

    /**
     * Сгенерировать правило с возвращаемым значением
     * @param ruleHead Голова правила
     * @param skolemArgs Аргументы сколема
     * @param skolemName Имя сколема
     * @param resPredName Предикат, указывающий на результат
     * @param resVarName Переменная, содержащая результат
     * @return Правило
     */
    public static String genRule(String ruleHead, String skolemArgs, String skolemName, String resPredName, String resVarName) {
        String rule = MAIN_RULE_PATTERN;

        rule = rule.replace("<ruleHead>", ruleHead);
        rule = rule.replace("<skolemArgs>", skolemArgs);
        rule = rule.replace("<skolemName>", skolemName);
        rule = rule.replace("resPredName", resPredName);
        rule = rule.replace("<resVarName>", resVarName);

        return rule;
    }

    /**
     * Сгенерировать булево правило
     * @param ruleHead Голова правила
     * @param skolemArgs Аргументы сколема
     * @param skolemName Имя сколема
     * @param resPredName Предикат, являющийся флагом результата
     * @return Правило
     */
    public static String genBooleanRule(String ruleHead, String skolemArgs, String skolemName, String resPredName) {
        String rule = BOOLEAN_RULE_PATTERN;

        rule = rule.replace("<ruleHead>", ruleHead);
        rule = rule.replace("<skolemArgs>", skolemArgs);
        rule = rule.replace("<skolemName>", skolemName);
        rule = rule.replace("<resPredName>", resPredName);

        return rule;
    }
}
