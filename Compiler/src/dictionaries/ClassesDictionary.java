package dictionaries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import compiler.Operator;
import compiler.Variable;
import compiler.operators.CheckPropertyValue;
import compiler.operators.CheckRelationship;
import compiler.operators.LogicalNot;
import compiler.values.PropertyValue;
import compiler.values.StringValue;
import util.DataType;
import util.Pair;

import java.util.*;

/**
 * Словарь классов
 */
public class ClassesDictionary {

    /**
     * Список классов
     */
    private static final List<String> classes = new ArrayList<>();

    /**
     * Вычисляемые классы
     *
     * Класс вычисляется путем вычисления bool выражения над объектом
     *
     * key - класс
     * val - First -имя переменной, куда подставить объект, Second - выражение для вычисления
     *
     * FIXME - другой способ?
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

    private static final Map<String, String> decisionTreeVars = new HashMap<>();

    static {
        // TODO: чтение из файла
        classes.add("token");
        classes.add("element");
        classes.add("operand");
        classes.add("operator");

        classes.add("plus");
        classes.add("minus");
        classes.add("multiplication");
        classes.add("division");
        classes.add("squareParenthesis");
        classes.add("parenthesis");

        // TODO добавить выражения
        // TODO: не работает, т.к. некоторые операторы не позволят просто забиндить переменную
        calculations.put("operand", new Pair<>("obj", null));
        calculations.put("operator", new Pair<>("obj", null));

        transitions.put("element", "token", "hasToken");
        transitions.put("token", "element", "isTokenOf");

        // TODO добавить все переменные
        decisionTreeVars.put("X", "element");
        decisionTreeVars.put("X1", "token");
        decisionTreeVars.put("X2", "token");
        decisionTreeVars.put("Y", "element");
        decisionTreeVars.put("Y1", "token");
        decisionTreeVars.put("Y2", "token");
        decisionTreeVars.put("Z", "element");
    }

    /**
     * Существует ли класс
     * @param className Имя класса
     * @return true - если существует, иначе - false
     */
    public static boolean exist(String className) {
        return classes.contains(className);
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
     * @return Выражение для вычисления ИМЯ ПЕРЕМЕННОЙ КУДА ПОСТААВИТЬ ОБЪЕКТ
     */
    public static Operator howToCalculate(String className, Operator obj) {
        if (!isComputable(className)) return null;

        // TODO: более адекватный вид
        if(className.equals("operand")) {
            Operator propVal = new PropertyValue("state");
            Operator strVal = new StringValue("unevaluated");
            Operator op = new CheckPropertyValue(List.of(obj, propVal, strVal));
            Operator not = new LogicalNot(List.of(op));
            return not;
        }

        return null;
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
