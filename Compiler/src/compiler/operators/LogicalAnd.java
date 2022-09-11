package compiler.operators;

import compiler.Operator;
import util.CompilationResult;
import util.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Логическое И
 */
public class LogicalAnd extends BaseOperator {

    /**
     * Конструктор
     * @param args Аргументы
     */
    public LogicalAnd(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();

        result.add(List.of(DataType.BOOLEAN, DataType.BOOLEAN));

        return result;
    }

    @Override
    public DataType resultDataType() {
        return DataType.BOOLEAN;
    }

    @Override
    public CompilationResult compile() {
        // Объявляем переменные
        String value = "";
        String rulePart = "";
        String completedRules = "";

        // Получаем аргументы
        Operator arg0 = arg(0);
        Operator arg1 = arg(1);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();
        CompilationResult compiledArg1 = arg1.compile();

        rulePart = compiledArg0.ruleHead() + compiledArg1.ruleHead();
        completedRules = compiledArg0.completedRules() + compiledArg1.completedRules();

        usedObjects = new ArrayList<>(arg0.objectsUsedInRule());
        usedObjects.addAll(new ArrayList<>(arg1.objectsUsedInRule()));

        return new CompilationResult(value, rulePart, completedRules);
    }
}
