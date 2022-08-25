package compiler.operators;

import compiler.Operator;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import java.util.ArrayList;
import java.util.List;

public class GetByCondition extends BaseOperator {

    private final String varName;

    /**
     * Конструктор
     * @param args Аргументы
     */
    public GetByCondition(List<Operator> args, String varName) {
        super(args);
        this.varName = varName;
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();
        result.add(List.of(DataType.BOOLEAN));
        return result;
    }

    @Override
    public DataType resultDataType() {
        return DataType.OBJECT;
    }

    @Override
    public CompilationResult compile() {
        // TODO: вернуть один самый первый или вернуть false если несколько? (во втором случае понадобится пауза)

        // Объявляем переменные
        String value = "";
        String rulePart = "";
        String completedRules = "";

        // Получаем аргументы
        Operator arg0 = arg(0);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();

        value = JenaUtil.genRuleVar(varName);
        rulePart = compiledArg0.rulePart();
        completedRules = compiledArg0.completedRules();

        usedObjects = List.of(compiledArg0.value());

        return new CompilationResult(value, rulePart, completedRules);
    }
}
