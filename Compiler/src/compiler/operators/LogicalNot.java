package compiler.operators;

import compiler.Operator;
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

        // Получаем аргументы
        Operator arg0 = arg(0);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();

        // Генерируем имена
        String skolemName = NamingManager.genVarName();
        String resFlagName = NamingManager.genFlagName();

        // Подставляем в сколем все объекты операторов
        StringBuilder skolemArgs = new StringBuilder().append(skolemName);
        for(String val : arg0.usedObjects()) {
            skolemArgs.append(",");
            skolemArgs.append(val);
        }

        // Генерируем правило
        String rule = JenaUtil.genBooleanRule(compiledArg0.rulePart(), skolemArgs.toString(), skolemName, resFlagName);

        // Добавляем правило в набор завершенных правил
        completedRules = compiledArg0.completedRules() + rule;

        // Добавляем паузу
        completedRules += PAUSE_MARK;

        // Добавляем в правило проверку отсутствия флага
        rulePart = "makeSkolem(" + skolemArgs+ ")" +
                "noValue(" + skolemName + "," + resFlagName + ")";

        return new CompilationResult(value, rulePart, completedRules);
    }
}
