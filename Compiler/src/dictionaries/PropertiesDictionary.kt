package dictionaries;

import util.DataType;
import util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Словарь свойств
 */
public class PropertiesDictionary {

    /**
     * Свойства
     *
     * row - имя свойства
     * val - First - список классов с данным свойством, null - если относится к объекту, Second - тип данных
     */
    private static final Map<String, Pair<List<String>, DataType>> properties = new HashMap<>();

    public static void init(String path) {
        // Очищаем старые значения
        properties.clear();

        // TODO: чтение из файла

        // Добавляем свойства
        properties.put("tokenCount", new Pair<>(List.of("element", "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.INTEGER));
        properties.put("arity", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.STRING));
        properties.put("place", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.STRING));
        properties.put("precedence", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.INTEGER));
        properties.put("associativity", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.STRING));
        properties.put("needsLeftOperand", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.BOOLEAN));
        properties.put("needsRightOperand", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.BOOLEAN));
        properties.put("needsInnerOperand", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.BOOLEAN));
        properties.put("strictOperandOrder", new Pair<>(List.of("plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"), DataType.BOOLEAN));

        properties.put("state", new Pair<>(null, DataType.STRING));
        properties.put("evaluatesTo", new Pair<>(null, DataType.STRING));
    }

    /**
     * Существует ли свойство
     * @param propertyName Имя свойства
     * @return true - если существует, иначе - false
     */
    public static boolean exist(String propertyName) {
        return properties.containsKey(propertyName);
    }

    /**
     * Является ли статическим
     * @param propertyName Имя свойства
     * @return true - если относится к классу, false - если к объекту
     */
    public static boolean isStatic(String propertyName) {
        if(!exist(propertyName)) return false;
        return properties.get(propertyName).first() != null;
    }

    /**
     * Тип данных
     * @param propertyName Имя свойства
     * @return Тип данных
     */
    public static DataType dataType(String propertyName) {
        if(!exist(propertyName)) return null;
        return properties.get(propertyName).second();
    }

    /**
     * Переопределяется ли свойство
     * @param propertyName Имя свойства
     * @return true - если переопределяется, иначе - false
     */
    public static boolean isPropertyBeingOverridden(String propertyName) {
        if(!exist(propertyName)) return false;
        if(properties.get(propertyName).first() == null) return false;

        List<String> classes = properties.get(propertyName).first();
        for (int i = 0; i < classes.size(); ++i) {
            for (int j = 0; j < classes.size(); ++j) {
                if(i == j) continue;

                if(ClassesDictionary.isParentOf(classes.get(i), classes.get(j))) {
                    return true;
                }
            }
        }

        return false;
    }
}
