package dictionaries;

import util.DataType;
import util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Словарь свойств
 */
public class PropertiesDictionary {

    /**
     * Свойства
     *
     * row - имя свойства
     * col - true - если относится к классу, false - если к объекту
     * val - тип данных
     */
    private static final Map<String, Pair<Boolean, DataType>> properties = new HashMap<>();

    static {
        // TODO: чтение из файла
        properties.put("tokenCount", new Pair<>(true, DataType.INTEGER));
        properties.put("arity", new Pair<>(true, DataType.STRING));
        properties.put("place", new Pair<>(true, DataType.STRING));
        properties.put("precedence", new Pair<>(true, DataType.INTEGER));
        properties.put("associativity", new Pair<>(true, DataType.STRING));
        properties.put("needsLeftOperand", new Pair<>(true, DataType.BOOLEAN));
        properties.put("needsRightOperand", new Pair<>(true, DataType.BOOLEAN));
        properties.put("needsInnerOperand", new Pair<>(true, DataType.BOOLEAN));
        properties.put("strictOperandOrder", new Pair<>(true, DataType.BOOLEAN));

        properties.put("state", new Pair<>(false, DataType.STRING));
        properties.put("evaluatesTo", new Pair<>(false, DataType.STRING));
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
        return properties.get(propertyName).first();
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
}
