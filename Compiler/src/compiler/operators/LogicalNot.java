package compiler.operators;

import compiler.Operator;
import compiler.values.BooleanValue;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

import static util.JenaUtil.PAUSE_MARK;

/**
 * Логическое отрицание
 */
public class LogicalNot extends BaseOperator {

    /**
     * Конструктор
     * @param args Аргументы
     */
    public LogicalNot(List<Operator> args) {
        super(args);
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
        // TODO: упрощение по законам де моргана?

        // Объявляем переменные
        String value = "";
        String rulePart = "";
        String completedRules = "";

        String operRule = "";

        // Получаем аргументы
        Operator arg0 = arg(0);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();

        // Если оператор - булево значение
        if (arg0 instanceof BooleanValue) {
            // Добавляем выражение, равное значению
            if(Boolean.parseBoolean(((BooleanValue) arg0).value())) {
                operRule += JenaUtil.genEqualPrim("1", "1");
            }
            else {
                operRule += JenaUtil.genEqualPrim("0", "1");
            }
        }

        // Генерируем имена
        String skolemName = NamingManager.genVarName();
        String resFlagName = NamingManager.genPredName();

        // Подставляем в сколем все объекты операторов
        StringBuilder skolemArgs = new StringBuilder().append(skolemName);
        for(String val : arg0.objectsUsedInRule()) {
            skolemArgs.append(",");
            skolemArgs.append(val);
        }

        // Генерируем правило
        String rule = JenaUtil.genBooleanRule(operRule + compiledArg0.ruleHead(), skolemArgs.toString(), skolemName, resFlagName);

        // Добавляем правило в набор завершенных правил
        completedRules = compiledArg0.completedRules() + rule;

        // Добавляем паузу
        completedRules += PAUSE_MARK;

        // FIXME?: придумать способ инициализации получше
        // Инициализируем переменные в новом правиле
        StringBuilder initPart = new StringBuilder();
        for(String var : arg0.objectsUsedInRule()) {
            initPart.append(JenaUtil.genTriple(var, NamingManager.genVarName(), NamingManager.genVarName()));
        }

        // Добавляем в правило проверку отсутствия флага
        rulePart = initPart + JenaUtil.genMakeSkolemPrim(skolemArgs.toString()) +
                JenaUtil.genNoValuePrim(skolemName, resFlagName);

        usedObjects = new ArrayList<>(arg0.objectsUsedInRule());

        return new CompilationResult(value, rulePart, completedRules);
    }
}
