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
 * Логическое ИЛИ
 */
public class LogicalOr extends BaseOperator {

    /**
     * Конструктор
     * @param args Аргументы
     */
    public LogicalOr(List<Operator> args) {
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

        // Генерируем имена
        String skolemName = NamingManager.genVarName();
        String resFlagName = NamingManager.genPredName();

        // Подставляем в сколем все объекты операторов
        StringBuilder skolemArgs = new StringBuilder().append(skolemName);
        for(String val : arg0.objectsUsedInRule()) {
            skolemArgs.append(",");
            skolemArgs.append(val);
        }
        for(String val : arg1.objectsUsedInRule()) {
            skolemArgs.append(",");
            skolemArgs.append(val);
        }

        // Компилируем правила
        String firstRule = JenaUtil.genBooleanRule(compiledArg0.ruleHead(), skolemArgs.toString(), skolemName, resFlagName);
        String secondRule = JenaUtil.genBooleanRule(compiledArg1.ruleHead(), skolemArgs.toString(), skolemName, resFlagName);

        // Добавляем правило в набор завершенных правил
        completedRules = compiledArg0.completedRules() + compiledArg1.completedRules() + firstRule + secondRule;

        // Добавляем паузу
        completedRules += PAUSE_MARK;

        // FIXME?: придумать способ инициализации получше
        // Инициализируем переменные в новом правиле
        StringBuilder initPart = new StringBuilder();
        for(String var : arg0.objectsUsedInRule()) {
            initPart.append(JenaUtil.genTriple(var, NamingManager.genVarName(), NamingManager.genVarName()));
        }
        for(String var : arg1.objectsUsedInRule()) {
            initPart.append(JenaUtil.genTriple(var, NamingManager.genVarName(), NamingManager.genVarName()));
        }

        // Добавляем в правило проверку наличия флага
        String flagValueVar = NamingManager.genVarName();
        rulePart = initPart + JenaUtil.genMakeSkolemPrim(skolemArgs.toString()) +
                JenaUtil.genTriple(skolemName, resFlagName, flagValueVar);

        usedObjects = new ArrayList<>(arg0.objectsUsedInRule());
        usedObjects.addAll(new ArrayList<>(arg1.objectsUsedInRule()));

        return new CompilationResult(value, rulePart, completedRules);
    }
}
