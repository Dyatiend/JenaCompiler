package compiler.operators;

import compiler.Operator;
import compiler.values.BooleanValue;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Квантор существования
 */
public class ExistenceQuantifier extends BaseOperator {

    private final String varName;

    /**
     * Конструктор
     * @param args Аргументы
     */
    public ExistenceQuantifier(List<Operator> args, String varName) {
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

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();

        // Инициализация переменной
        rulePart = "(" + JenaUtil.genVar(varName) + " " + NamingManager.genVarName() + " " + NamingManager.genVarName() + ")";

        // Если оператор булево значение
        if(arg0 instanceof BooleanValue) {
            // Добавляем выражение, равное значению
            if(Boolean.parseBoolean(((BooleanValue) arg0).value())) {
                rulePart += JenaUtil.genEqualPrim("1", "1");
            }
            else {
                rulePart += JenaUtil.genEqualPrim("0", "1");
            }
        }

        rulePart += compiledArg0.ruleHead();
        completedRules = compiledArg0.completedRules();

        usedObjects = List.of(JenaUtil.genVar(varName));

        return new CompilationResult(value, rulePart, completedRules);
    }
}
