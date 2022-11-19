package compiler.operators;

import compiler.Operator;
import util.CompilationResult;
import util.DataType;

import java.util.ArrayList;
import java.util.List;

public class ForAllQuantifier extends BaseOperator {

    private final String varName;

    /**
     * Конструктор
     * @param args Аргументы
     */
    public ForAllQuantifier(List<Operator> args, String varName) {
        super(args);
        this.varName = varName;
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
        // Компилируем через другие операторы
        Operator not = new LogicalNot(List.of(arg(1)));
        Operator and = new LogicalAnd(List.of(arg(0), not));
        Operator existence = new ExistenceQuantifier(List.of(and), varName);
        Operator res = new LogicalNot(List.of(existence));

        CompilationResult compilationResult = res.compile();

        usedObjects = new ArrayList<>(res.objectsUsedInRule());

        return compilationResult;
    }
}
