package dictionaries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import compiler.Operator;
import compiler.Variable;
import compiler.operators.CheckPropertyValue;
import compiler.operators.LogicalNot;
import compiler.values.PropertyValue;
import compiler.values.StringValue;
import util.DataType;
import util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Словарь классов
 */
public class ClassesDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список классов
     *
     * key - класс
     * val - предок
     */
    private static final Map<String, String> classes = new HashMap<>();

    /**
     * Вычисляемые классы
     *
     * Класс вычисляется путем вычисления boolean выражения над объектом
     *
     * key - класс
     * val - First - имя переменной, куда подставить объект, Second - выражение для вычисления
     */
    private static final Map<String, Pair<String, Operator>> calculations = new HashMap<>();

    /**
     * Переходы между классами
     *
     * row - источник
     * col - назначение
     * val - отношение, по которому идет переход
     */
    private static final Table<String, String, String> transitions = HashBasedTable.create();

    /**
     * Переменные дерева мысли
     *
     * key - имя переменной
     * val - класс переменной
     */
    private static final Map<String, String> decisionTreeVars = new HashMap<>();

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    static {
        // TODO: чтение из файла

        // Добавляем классы
        classes.put("token", null);
        classes.put("element", null);
        classes.put("operand", null);
        classes.put("operator", null);

        classes.put("plus", "element");
        classes.put("minus", "element");
        classes.put("multiplication", "element");
        classes.put("division", "element");
        classes.put("squareParenthesis", "element");
        classes.put("parenthesis", "element");

        // Добавляем выражения для вычисления классов
        Operator var = new Variable("obj", DataType.OBJECT);
        Operator prop = new PropertyValue("state");
        Operator stringVal = new StringValue("unevaluated");
        Operator checkProp = new CheckPropertyValue(List.of(var, prop, stringVal));
        Operator not = new LogicalNot(List.of(checkProp));
        calculations.put("operand", new Pair<>("obj", not));

        var = new Variable("obj", DataType.OBJECT);
        prop = new PropertyValue("state");
        stringVal = new StringValue("unevaluated");
        checkProp = new CheckPropertyValue(List.of(var, prop, stringVal));
        calculations.put("operator", new Pair<>("obj", checkProp));

        // Добавляем переходы
        transitions.put("element", "token", "hasToken");
        transitions.put("token", "element", "isTokenOf");

        // Добавляем переменные
        decisionTreeVars.put("X", "element");
        decisionTreeVars.put("X1", "token");
        decisionTreeVars.put("X2", "token");
        decisionTreeVars.put("Y", "element");
        decisionTreeVars.put("Y1", "token");
        decisionTreeVars.put("Y2", "token");
        decisionTreeVars.put("Z", "element");
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Существует ли класс
     * @param className Имя класса
     * @return true - если существует, иначе - false
     */
    public static boolean exist(String className) {
        return classes.containsKey(className);
    }

    public static boolean isParentOf(String parent, String child) {
        if(!exist(parent) || !exist(child)) return false;

        if(classes.get(child).equals(parent)) return true;
        return isParentOf(parent, classes.get(child));
    }

    /**
     * Вычисляемый ли класс
     * @param className Имя класса
     * @return true - если вычисляемый, иначе - false
     */
    public static boolean isComputable(String className) { return calculations.containsKey(className); }

    /**
     * Как вычислить класс
     * @param className Имя класса
     * @return Выражение для вычисления и имя переменной, в которую надо подставить объект
     */
    public static Pair<String, Operator> howToCalculate(String className) {
        if (!isComputable(className)) return null;
        return calculations.get(className);
    }

    /**
     * Есть ли переход между классами
     * @param from Источник
     * @param to Назначение
     * @return true - если есть переход, иначе - false
     */
    public static boolean hasTransition(String from, String to) { return transitions.contains(from, to); }

    /**
     * Переход между классами
     * @param from Источник
     * @param to Назначение
     * @return Имя отношения, по которому идет переход
     */
    public static String transition(String from, String to) {
        if (!hasTransition(from, to)) return null;
        return transitions.get(from, to);
    }

    /**
     * Получить класс переменной дерева мысли
     * @param varName Имя переменной
     * @return Класс переменной
     */
    public static String decisionTreeVarClass(String varName) {
        return decisionTreeVars.get(varName);
    }
}
