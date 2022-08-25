package compiler.operators;

import compiler.Operator;
import compiler.values.ClassValue;
import dictionaries.ClassesDictionary;
import util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Получить класс объекта
 * FIXME: А этот оператор нужен вообще?
 */
public class GetClass extends BaseOperator {

    /**
     * Имя предиката, используемое при компиляции
     */
    private static final String CLASS_PRED_NAME = "type";

    /**
     * Имя предиката, используемое при компиляции
     */
    private static final String SUBCLASS_PRED_NAME = "subClassOf";

    /**
     * Конструктор
     * @param args Аргументы
     */
    public GetClass(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();
        result.add(List.of(DataType.OBJECT));
        return result;
    }

    @Override
    public DataType resultDataType() {
        return DataType.CLASS;
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

        value = NamingManager.genVarName();

        rulePart = compiledArg0.rulePart();

        rulePart += JenaUtil.genTriple(
                compiledArg0.value(),
                JenaUtil.genRuleLink(JenaUtil.RDF_PREF, CLASS_PRED_NAME),
                value) +
                "noValue(" + value + "," + JenaUtil.genRuleLink(JenaUtil.RDF_PREF, SUBCLASS_PRED_NAME) + ")";

        completedRules = compiledArg0.completedRules();

        usedObjects = List.of(compiledArg0.value());

        return new CompilationResult(value, rulePart, completedRules);
    }
}
