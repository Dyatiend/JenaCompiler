package compiler;

import util.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Значение в выражении
 */
public abstract class Value implements Operator {

    /**
     * Значение
     */
    protected final String value;

    /**
     * Конструктор
     * @param value Значение
     */
    public Value(String value) {
        this.value = value;
    }

    /**
     * Получить значение
     * @return Значение
     */
    public String value() {
        return value;
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        return new ArrayList<>();
    }
}
