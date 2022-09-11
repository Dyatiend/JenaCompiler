package compiler.operators;

import compiler.Operator;
import compiler.values.ClassValue;
import dictionaries.ClassesDictionary;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Оператор проверки класса объекта
 */
public class CheckClass extends BaseOperator {

    /**
     * Имя предиката, используемое при компиляции
     */
    private static final String CLASS_PRED_NAME = "type";

    /**
     * Конструктор
     * @param args Аргументы
     */
    public CheckClass(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();

        result.add(List.of(DataType.OBJECT, DataType.CLASS));

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

        // Если класс можно вычислить (вычисляемый класс можно получить только указав его имя т.е. через ClassValue)
        if(arg1 instanceof ClassValue &&
                ClassesDictionary.isComputable(((ClassValue) arg1).value())) {
            // Получаем выражение для вычисления
            Pair<String, Operator> calculation = ClassesDictionary.howToCalculate(((ClassValue) arg1).value());
            String varName = calculation.first();
            Operator expression = calculation.second();

            CompilationResult compiledCalculation = expression.compile(); // Выражение для вычисления

            // Собираем правило
            ruleHead = compiledArg0.ruleHead() + compiledArg1.ruleHead(); // Собираем части первого и второго аргументов
            ruleHead += JenaUtil.genBindPrim(compiledArg0.value(), varName); // Инициализируем переменную
            ruleHead += compiledCalculation.ruleHead(); // Добавляем результат компиляции вычисления

            // Передаем завершенные правила дальше
            completedRules = compiledArg0.completedRules() +
                    compiledArg1.completedRules() +
                    compiledCalculation.completedRules();
        }
        else {
            // Собираем правило
            ruleHead = compiledArg0.ruleHead() + compiledArg1.ruleHead(); // Собираем части первого и второго аргументов

            // Добавляем проверку класса
            ruleHead += JenaUtil.genTriple(
                    compiledArg0.value(),
                    JenaUtil.genLink(JenaUtil.RDF_PREF, CLASS_PRED_NAME),
                    compiledArg1.value());

            // Передаем завершенные правила дальше
            completedRules = compiledArg0.completedRules() + compiledArg1.completedRules();
        }

        usedObjects = List.of(compiledArg0.value());

        return new CompilationResult(value, ruleHead, completedRules);
    }
}
