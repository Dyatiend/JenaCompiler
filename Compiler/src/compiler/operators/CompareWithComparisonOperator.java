package compiler.operators;

import compiler.Operator;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import java.util.ArrayList;
import java.util.List;

public class CompareWithComparisonOperator extends BaseOperator {

    public enum ComparisonOperator {
        LESS,
        GREATER,
        EQUAL
    }

    private final ComparisonOperator operator;

    /**
     * Конструктор
     * @param args Аргументы
     */
    public CompareWithComparisonOperator(List<Operator> args, ComparisonOperator operator) {
        super(args);
        if(arg(0).resultDataType() == DataType.STRING && operator != ComparisonOperator.EQUAL)
            throw new IllegalArgumentException("Указанный оператор не совместим с этими типам данных");

        this.operator = operator;
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();

        result.add(List.of(DataType.INTEGER, DataType.DOUBLE));
        result.add(List.of(DataType.DOUBLE, DataType.INTEGER));
        result.add(List.of(DataType.INTEGER, DataType.INTEGER));
        result.add(List.of(DataType.DOUBLE, DataType.DOUBLE));
        result.add(List.of(DataType.STRING, DataType.STRING));

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
        String ruleHead = "";
        String completedRules = "";

        // Получаем аргументы
        Operator arg0 = arg(0);
        Operator arg1 = arg(1);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();
        CompilationResult compiledArg1 = arg1.compile();

        ruleHead = compiledArg0.ruleHead() + compiledArg1.ruleHead();
        completedRules = compiledArg0.completedRules() + compiledArg1.completedRules();

        switch (operator) {
            case LESS -> {
                // Правило для проверки меньше
                ruleHead += JenaUtil.genLessThanPrim(compiledArg0.value(), compiledArg1.value());
            }
            case GREATER -> {
                // Правило для проверки больше
                ruleHead += JenaUtil.genGreaterThanPrim(compiledArg0.value(), compiledArg1.value());
            }
            case EQUAL -> {
                // Правило для проверки эквивалентности
                ruleHead += JenaUtil.genEqualPrim(compiledArg0.value(), compiledArg1.value());
            }
        }

        usedObjects = new ArrayList<>(arg0.objectsUsedInRule());
        usedObjects.addAll(new ArrayList<>(arg1.objectsUsedInRule()));

        return new CompilationResult(value, ruleHead, completedRules);
    }
}
