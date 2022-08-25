package compiler;

import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Переменная, вводимая некоторыми операторами
 */
public class Variable implements Operator {

    /**
     * Имя переменной
     */
    private final String name;

    /**
     * Тип данных переменной
     */
    private final DataType dataType;

    /**
     * Конструктор
     * @param name Имя переменной
     * @param dataType Тип данных переменной
     */
    public Variable(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        return new ArrayList<>();
    }

    @Override
    public DataType resultDataType() {
        return dataType;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genRuleVar(name), "", "");
    }
}
