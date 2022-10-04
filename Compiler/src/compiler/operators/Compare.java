package compiler.operators;

import compiler.Operator;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

public class Compare extends BaseOperator {

    /**
     * Конструктор
     * @param args Аргументы
     */
    public Compare(List<Operator> args) {
        super(args);
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
        return DataType.COMPARISON_RESULT;
    }

    @Override
    public CompilationResult compile() {
        // TODO: флаг "не может быть неопределенным" позволит избежать паузы

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

        // Правило для проверки эквивалентности
        String operatorsPart = compiledArg0.ruleHead() + compiledArg1.ruleHead();
        String equal = operatorsPart + JenaUtil.genEqualPrim(compiledArg0.value(), compiledArg1.value());
        String equalRule = JenaUtil.genRule(
                equal,
                skolemArgs.toString(),
                skolemName,
                resFlagName,
                JenaUtil.genStingVal("equal"));

        // Собираем правила
        completedRules = compiledArg0.completedRules() + compiledArg1.completedRules() + equalRule;

        // Правила для проверки больше/меньше (только для чисел)
        if(arg0.resultDataType() != DataType.STRING) {
            // Правило для проверки меньше
            String less = operatorsPart + JenaUtil.genLessThanPrim(compiledArg0.value(), compiledArg1.value());
            String lessRule = JenaUtil.genRule(
                    less,
                    skolemArgs.toString(),
                    skolemName,
                    resFlagName,
                    JenaUtil.genStingVal("less"));

            // Правило для проверки больше
            String greater = operatorsPart + JenaUtil.genGreaterThanPrim(compiledArg0.value(), compiledArg1.value());
            String greaterRule = JenaUtil.genRule(
                    greater,
                    skolemArgs.toString(),
                    skolemName,
                    resFlagName,
                    JenaUtil.genStingVal("greater"));

            completedRules += lessRule + greaterRule;
        }

        completedRules += JenaUtil.PAUSE_MARK;

        String empty = NamingManager.genVarName();

        // Правило для проверки undetermined
        String undetermined = JenaUtil.genNotEqualPrim(empty, resFlagName);
        String undeterminedRule = JenaUtil.genRule(
                undetermined,
                skolemArgs.toString(),
                skolemName,
                resFlagName,
                JenaUtil.genStingVal("undetermined"));

        completedRules += undeterminedRule;

        value = NamingManager.genVarName();

        // FIXME?: придумать способ инициализации получше
        // Инициализируем переменные в новом правиле
        StringBuilder initPart = new StringBuilder();
        for(String var : arg0.objectsUsedInRule()) {
            initPart.append(JenaUtil.genTriple(var, NamingManager.genVarName(), NamingManager.genVarName()));
        }
        for(String var : arg1.objectsUsedInRule()) {
            initPart.append(JenaUtil.genTriple(var, NamingManager.genVarName(), NamingManager.genVarName()));
        }
        if(arg0.objectsUsedInRule().isEmpty() && arg1.objectsUsedInRule().isEmpty()) {
            initPart.append(JenaUtil.genTriple(NamingManager.genVarName(), NamingManager.genVarName(), NamingManager.genVarName()));
        }

        ruleHead = initPart + JenaUtil.genMakeSkolemPrim(skolemArgs.toString()) +
                JenaUtil.genTriple(skolemName, resFlagName, value);

        usedObjects = new ArrayList<>(arg0.objectsUsedInRule());
        usedObjects.addAll(new ArrayList<>(arg1.objectsUsedInRule()));

        return new CompilationResult(value, ruleHead, completedRules);
    }
}
