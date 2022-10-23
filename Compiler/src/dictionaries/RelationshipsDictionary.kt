package dictionaries;

import util.JenaUtil;
import util.Pair;

import java.util.*;

import static util.JenaUtil.POAS_PREF;

// TODO?: добавить транзитивность и экстремальность?
// TODO?: statement list?
// FIXME: множественные переходы между классами (Как узнать какой токен нужен для отношения?)
/**
 * Словарь свойств
 */
public class RelationshipsDictionary {

    // ++++++ Шаблоны вспомогательных правил для свойств линейного порядка +++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public static final String NUMERATION_RULES_PATTERN = """
            [
                (?var1 <linerPred> ?var2)
                noValue(?var3, <linerPred>, ?var1)
                ->
                (?var1 <numberPred> "1"^^xsd:integer)
            ]
            
            [
                (?var1 <linerPred> ?var2)
                noValue(?var2, <numberPred>)
                (?var1 <numberPred> ?var3)
                addOne(?var3, ?var4)
                ->
                (?var2 <numberPred> ?var4)
            ]
            """;

    public static final int LEFT_OF_VAR_COUNT = 2;
    public static final String LEFT_OF_PATTERN = """
            (<arg1> <numberPred> <var1>)
            (<arg2> <numberPred> <var2>)
            lessThan(<var1>, <var2>)
            """;

    public static final int RIGHT_OF_VAR_COUNT = 2;
    public static final String RIGHT_OF_PATTERN = """
            (<arg1> <numberPred> <var1>)
            (<arg2> <numberPred> <var2>)
            greaterThan(<var1>, <var2>)
            """;

    public static final int IS_BETWEEN_VAR_COUNT = 3;
    public static final String IS_BETWEEN_PATTERN = """
            (<arg1> <numberPred> <var1>)
            (<arg2> <numberPred> <var2>)
            (<arg3> <numberPred> <var3>)
            greaterThan(<var1>, <var2>)
            lessThan(<var1>, <var3>)
            """;

    public static final int IS_CLOSER_TO_THAN_VAR_COUNT = 7;
    public static final String IS_CLOSER_TO_THAN_PATTERN = """
            (<arg1> <numberPred> <var1>)
            (<arg2> <numberPred> <var2>)
            (<arg3> <numberPred> <var3>)
            difference(<var2>, <var1>, <var4>)
            difference(<var2>, <var3>, <var5>)
            absoluteValue(<var4>, <var6>)
            absoluteValue(<var5>, <var7>)
            lessThan(<var6>, <var7>)
            """;

    public static final int IS_FURTHER_FROM_THAN_VAR_COUNT = 7;
    public static final String IS_FURTHER_FROM_THAN_PATTERN = """
            (<arg1> <numberPred> <var1>)
            (<arg2> <numberPred> <var2>)
            (<arg3> <numberPred> <var3>)
            difference(<var2>, <var1>, <var4>)
            difference(<var2>, <var3>, <var5>)
            absoluteValue(<var4>, <var6>)
            absoluteValue(<var5>, <var7>)
            greaterThan(<var6>, <var7>)
            """;

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Использованы ли отношения линейной шкалы
     */
    private static boolean isLinerScaleUsed = false;

    /**
     * Вспомогательные правила для работы правил линейной шкалы
     */
    private static String auxiliaryLinerScaleRules = "";

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

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public static void init(String path) {
        // Очищаем старые значения
        relationships.clear();
        patterns.clear();
        auxiliaryLinerScaleRules = "";
        isLinerScaleUsed = false;

        // TODO: чтение из файла
        // FIXME?: не считать классы линейной шкалой?

        // Подключаем "библиотеку"
        isLinerScaleUsed = true;

        // Добавляем свойства
        relationships.put("hasToken", List.of("element", "token"));
        relationships.put("isTokenOf", List.of("token", "element"));
        relationships.put("directlyLeftOf", List.of("token", "token"));
        relationships.put("directlyRightOf", List.of("token", "token"));
        relationships.put("isOperandOf", List.of("element", "element"));
        relationships.put("isOperatorTo", List.of("element", "element"));

        relationships.put("token_leftOf", List.of("token", "token"));
        relationships.put("token_rightOf", List.of("token", "token"));
        relationships.put("token_isBetween", List.of("token", "token", "token"));
        relationships.put("token_isCloserToThan", List.of("token", "token", "token"));
        relationships.put("token_isFurtherFromThan", List.of("token", "token", "token"));

        relationships.put("class_leftOf", List.of("class", "class"));
        relationships.put("class_rightOf", List.of("class", "class"));
        relationships.put("class_isBetween", List.of("class", "class", "class"));
        relationships.put("class_isCloserToThan", List.of("class", "class", "class"));
        relationships.put("class_isFurtherFromThan", List.of("class", "class", "class"));

        // Добавляем вспомогательные правила
        String token_numerationRules = NUMERATION_RULES_PATTERN;

        token_numerationRules = token_numerationRules.replace("<linerPred>", JenaUtil.genLink(POAS_PREF, "directlyLeftOf"));
        token_numerationRules = token_numerationRules.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~tokenNumber~"));

        String class_numerationRules = NUMERATION_RULES_PATTERN;

        class_numerationRules = class_numerationRules.replace("<linerPred>", JenaUtil.genLink(POAS_PREF, "subClassOf"));
        class_numerationRules = class_numerationRules.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~classNumber~"));

        auxiliaryLinerScaleRules += token_numerationRules + class_numerationRules;

        // Добавляем шаблоны
        patterns.put("hasToken", new Pair<>(0, new Pair<>("(<arg1> poas:hasToken <arg2>)", "")));
        patterns.put("isTokenOf", new Pair<>(0, new Pair<>("(<arg1> poas:isTokenOf <arg2>)", "")));
        patterns.put("directlyLeftOf", new Pair<>(0, new Pair<>("(<arg1> poas:directlyLeftOf <arg2>)", "")));
        patterns.put("directlyRightOf", new Pair<>(0, new Pair<>("(<arg1> poas:directlyRightOf <arg2>)", "")));
        patterns.put("isOperandOf", new Pair<>(0, new Pair<>("(<arg1> poas:isOperandOf <arg2>)", "")));
        patterns.put("isOperatorTo", new Pair<>(0, new Pair<>("(<arg1> poas:isOperatorTo <arg2>)", "")));

        String token_leftOf = LEFT_OF_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~tokenNumber~"));
        patterns.put("token_leftOf", new Pair<>(LEFT_OF_VAR_COUNT, new Pair<>(token_leftOf, "")));
        String token_rightOf = RIGHT_OF_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~tokenNumber~"));
        patterns.put("token_rightOf", new Pair<>(RIGHT_OF_VAR_COUNT, new Pair<>(token_rightOf, "")));
        String token_isBetween = IS_BETWEEN_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~tokenNumber~"));
        patterns.put("token_isBetween", new Pair<>(IS_BETWEEN_VAR_COUNT, new Pair<>(token_isBetween, "")));
        String token_isCloserToThan = IS_CLOSER_TO_THAN_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~tokenNumber~"));
        patterns.put("token_isCloserToThan", new Pair<>(IS_CLOSER_TO_THAN_VAR_COUNT, new Pair<>(token_isCloserToThan, "")));
        String token_isFurtherFromThan = IS_FURTHER_FROM_THAN_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~tokenNumber~"));
        patterns.put("token_isFurtherFromThan", new Pair<>(IS_FURTHER_FROM_THAN_VAR_COUNT, new Pair<>(token_isFurtherFromThan, "")));

        String class_leftOf = LEFT_OF_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~classNumber~"));
        patterns.put("class_leftOf", new Pair<>(LEFT_OF_VAR_COUNT, new Pair<>(class_leftOf, "")));
        String class_rightOf = RIGHT_OF_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~classNumber~"));
        patterns.put("class_rightOf", new Pair<>(RIGHT_OF_VAR_COUNT, new Pair<>(class_rightOf, "")));
        String class_isBetween = IS_BETWEEN_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~classNumber~"));
        patterns.put("class_isBetween", new Pair<>(IS_BETWEEN_VAR_COUNT, new Pair<>(class_isBetween, "")));
        String class_isCloserToThan = IS_CLOSER_TO_THAN_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~classNumber~"));
        patterns.put("class_isCloserToThan", new Pair<>(IS_CLOSER_TO_THAN_VAR_COUNT, new Pair<>(class_isCloserToThan, "")));
        String class_isFurtherFromThan = IS_FURTHER_FROM_THAN_PATTERN.replace("<numberPred>", JenaUtil.genLink(POAS_PREF, "~classNumber~"));
        patterns.put("class_isFurtherFromThan", new Pair<>(IS_FURTHER_FROM_THAN_VAR_COUNT, new Pair<>(class_isFurtherFromThan, "")));
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Использованы ли отношения линейной шкалы
     * @return true - если использованы, иначе - false
     */
    public static boolean isLinerScaleUsed() { return isLinerScaleUsed; }

    /**
     * Получить вспомогательные правила для работы правил линейной шкалы
     * @return Вспомогательные правила для работы правил линейной шкалы
     */
    public static String auxiliaryLinerScaleRules() { return auxiliaryLinerScaleRules; }

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
}
