package dictionaries;

import util.Pair;

import java.util.*;

/**
 * Словарь свойств
 *
 * TODO?: Добавить транзитивность и экстремальность?
 * TODO: statement list?
 * TODO: множественные переходы между классами, как быть? (Как узнать какой токен нужен для связи например)
 */
public class RelationshipsDictionary {

    /**
     * Отношения
     *
     * key - имя отношения
     * val - список классов аргументов
     */
    private static final Map<String, List<String>> relationships = new HashMap<>();

    /**
     * Шаблоны правил для отношений
     *
     * row - Имя отношения
     * col - Количество вспомогательных переменных в шаблоне
     * val - First - шаблон правила, Second - вспомогательные правила
     */
    private static final Map<String, Pair<Integer, Pair<String, String>>> patterns = new HashMap<>();

    /**
     * Существует ли отношение
     * @param relationshipName Имя отношения
     * @return true - если существует, иначе - false
     */
    public static boolean exist(String relationshipName) {
        return relationships.containsKey(relationshipName);
    }

    /**
     * Получить список классов аргументов
     * @param relationshipName Имя отношения
     * @return Список классов аргументов
     */
    public static List<String> args(String relationshipName) {
        return relationships.get(relationshipName);
    }

    /**
     * Получить количество переменных в шаблоне правила
     * @param relationshipName Имя отношения
     * @return Количество переменных в шаблоне
     */
    public static Integer varCount(String relationshipName) {
        if (!exist(relationshipName)) return null;
        return patterns.get(relationshipName).first();
    }

    /**
     * Получить шаблон правила
     * @param relationshipName Имя отношения
     * @return Шаблон правила
     */
    public static Pair<String, String> pattern(String relationshipName) {
        if (!exist(relationshipName)) return null;
        return patterns.get(relationshipName).second();
    }

    // -------------- Вспомогательный правила для свойств линейного порядка ---------------

    private static final boolean isLinerScaleUsed;

    /**
     * Использованы ли отношения линейной шкалы
     * @return true - если использованы, иначе - false
     */
    public static boolean isLinerScaleUsed() { return isLinerScaleUsed; }

    // TODO: подставлять свойства в шаблон
    // TODO: подставлять префикс в шаблон?
    // TODO: поддержка кастомных префиксов?
    // TODO: разное имя связи с номером для каждой линейной шкалы?????
    // TODO: проблема множества шкал с пересекающимися объектами???
    public static final String NUMERATION_RULES = """
            [
            (?var1 poas:directlyLeftOf ?var2)
            noValue(?var3, poas:directlyLeftOf, ?var1)
            ->
            (?var1 poas:&number& "1"^^xsd:integer)
            ]
            [
            (?var1 poas:directlyLeftOf ?var2)
            noValue(?var2, poas:&number&)
            (?var1 poas:&number& ?var3)
            addOne(?var3, ?var4)
            ->
            (?var2 poas:&number& ?var4)
            ]
            
            [
            (?var1 rdf:subClassOf ?var2)
            noValue(?var2, rdf:subClassOf)
            ->
            (?var2 poas:&number& "1"^^xsd:integer)
            ]
            [
            (?var1 rdf:subClassOf ?var2)
            noValue(?var1, poas:&number&)
            (?var2 poas:&number& ?var3)
            addOne(?var3, ?var4)
            ->
            (?var1 poas:&number& ?var4)
            ]
            """;

    public static final int LEFT_OF_VAR_COUNT = 2;
    public static final String LEFT_OF = """
            (<arg1> poas:&number& <var1>)
            (<arg2> poas:&number& <var2>)
            lessThan(<var1>, <var2>)
            """;

    public static final int RIGHT_OF_VAR_COUNT = 2;
    public static final String RIGHT_OF = """
            (<arg1> poas:&number& <var1>)
            (<arg2> poas:&number& <var2>)
            greaterThan(<var1>, <var2>)
            """;

    public static final int IS_BETWEEN_VAR_COUNT = 3;
    public static final String IS_BETWEEN = """
            (<arg1> poas:&number& <var1>)
            (<arg2> poas:&number& <var2>)
            (<arg3> poas:&number& <var3>)
            greaterThan(<var1>, <var2>)
            lessThan(<var1>, <var3>)
            """;

    public static final int IS_CLOSER_TO_THAN_VAR_COUNT = 7;
    public static final String IS_CLOSER_TO_THAN = """
            (<arg1> poas:&number& <var1>)
            (<arg2> poas:&number& <var2>)
            (<arg3> poas:&number& <var3>)
            difference(<var2>, <var1>, <var4>)
            difference(<var2>, <var3>, <var5>)
            absoluteValue(<var4>, <var6>)
            absoluteValue(<var5>, <var7>)
            lessThan(<var6>, <var7>)
            """;

    public static final int IS_FURTHER_FROM_THAN_VAR_COUNT = 7;
    public static final String IS_FURTHER_FROM_THAN = """
            (<arg1> poas:&number& <var1>)
            (<arg2> poas:&number& <var2>)
            (<arg3> poas:&number& <var3>)
            difference(<var2>, <var1>, <var4>)
            difference(<var2>, <var3>, <var5>)
            absoluteValue(<var4>, <var6>)
            absoluteValue(<var5>, <var7>)
            greaterThan(<var6>, <var7>)
            """;

    static {
        // TODO: чтение из файла
        isLinerScaleUsed = true;

        relationships.put("hasToken", List.of("element", "token"));
        relationships.put("isTokenOf", List.of("token", "element"));
        relationships.put("directlyLeftOf", List.of("token", "token"));
        relationships.put("directlyRightOf", List.of("token", "token"));

        // TODO: Как определять классы, если они зависят от отношения, которое образует шкалу
        relationships.put("leftOf", List.of("token", "token"));
        relationships.put("rightOf", List.of("token", "token"));
        relationships.put("isBetween", List.of("token", "token", "token"));
        relationships.put("isCloserToThan", List.of("token", "token", "token"));
        relationships.put("isFurtherFromThan", List.of("token", "token", "token"));
        relationships.put("isOperandOf", List.of("element", "element"));
        relationships.put("isOperatorTo", List.of("element", "element"));

        patterns.put("hasToken", new Pair<>(0, new Pair<>("(<arg1> poas:hasToken <arg2>)", "")));
        patterns.put("isTokenOf", new Pair<>(0, new Pair<>("(<arg1> poas:isTokenOf <arg2>)", "")));
        patterns.put("directlyLeftOf", new Pair<>(0, new Pair<>("(<arg1> poas:directlyLeftOf <arg2>)", "")));
        patterns.put("directlyRightOf", new Pair<>(0, new Pair<>("(<arg1> poas:directlyRightOf <arg2>)", "")));
        patterns.put("leftOf", new Pair<>(LEFT_OF_VAR_COUNT, new Pair<>(LEFT_OF, "")));
        patterns.put("rightOf", new Pair<>(RIGHT_OF_VAR_COUNT, new Pair<>(RIGHT_OF, "")));
        patterns.put("isBetween", new Pair<>(IS_BETWEEN_VAR_COUNT, new Pair<>(IS_BETWEEN, "")));
        patterns.put("isCloserToThan", new Pair<>(IS_CLOSER_TO_THAN_VAR_COUNT, new Pair<>(IS_CLOSER_TO_THAN, "")));
        patterns.put("isFurtherFromThan", new Pair<>(IS_FURTHER_FROM_THAN_VAR_COUNT, new Pair<>(IS_FURTHER_FROM_THAN, "")));
        patterns.put("isOperandOf", new Pair<>(0, new Pair<>("(<arg1> poas:isOperandOf <arg2>)", "")));
        patterns.put("isOperatorTo", new Pair<>(0, new Pair<>("(<arg1> poas:isOperatorTo <arg2>)", "")));
    }
}
