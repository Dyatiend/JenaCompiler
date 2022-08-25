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
        String resFlagName = NamingManager.genFlagName();

        // Подставляем в сколем все объекты операторов
        StringBuilder skolemArgs = new StringBuilder().append(skolemName);
        for(String val : arg0.usedObjects()) {
            skolemArgs.append(",");
            skolemArgs.append(val);
        }
        for(String val : arg1.usedObjects()) {
            skolemArgs.append(",");
            skolemArgs.append(val);
        }

        // Правило для проверки эквивалентности
        String operatorsPart = compiledArg0.rulePart() + compiledArg1.rulePart();
        String equal = operatorsPart + "equal(" + compiledArg0.value() + "," + compiledArg1.value() + ")";
        String equalRule = JenaUtil.genRule(
                equal,
                skolemArgs.toString(),
                skolemName,
                resFlagName,
                "\"equal\"^^xsd:string");

        // Собираем правила
        completedRules = compiledArg0.completedRules() + compiledArg1.completedRules() + equalRule;

        // Правила для проверки больше/меньше (только для чисел)
        if(arg0.resultDataType() != DataType.STRING) {
            // Правило для проверки меньше
            String less = operatorsPart + "lessThan(" + compiledArg0.value() + "," + compiledArg1.value() + ")";
            String lessRule = JenaUtil.genRule(
                    less,
                    skolemArgs.toString(),
                    skolemName,
                    resFlagName,
                    "\"less\"^^xsd:string");

            // Правило для проверки больше
            String greater = operatorsPart + "greaterThan(" + compiledArg0.value() + "," + compiledArg1.value() + ")";
            String greaterRule = JenaUtil.genRule(
                    greater,
                    skolemArgs.toString(),
                    skolemName,
                    resFlagName,
                    "\"greater\"^^xsd:string");

            completedRules += lessRule + greaterRule;
        }

        completedRules += JenaUtil.PAUSE_MARK;

        String empty = NamingManager.genVarName();

        // Правило для проверки undetermined
        String undetermined = "noValue(" + empty + "," + resFlagName + ")";
        String undeterminedRule = JenaUtil.genRule(
                undetermined,
                skolemArgs.toString(),
                skolemName,
                resFlagName,
                "\"undetermined\"^^xsd:string");

        completedRules += undeterminedRule;

        value = NamingManager.genVarName();

        rulePart = "makeSkolem(" + skolemArgs+ ")" +
                JenaUtil.genTriple(skolemName, resFlagName, value);

        return new CompilationResult(value, rulePart, completedRules);
    }
}
