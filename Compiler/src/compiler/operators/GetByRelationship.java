package compiler.operators;

import compiler.Operator;
import compiler.values.RelationshipValue;
import dictionaries.RelationshipsDictionary;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

public class GetByRelationship extends BaseOperator {

    /**
     * Конструктор
     * @param args Аргументы
     */
    public GetByRelationship(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();
        result.add(List.of(DataType.OBJECT, DataType.RELATIONSHIP));
        return result;
    }

    @Override
    public DataType resultDataType() {
        return DataType.OBJECT;
    }

    @Override
    public CompilationResult compile() {
        // TODO: вернуть один самый первый или вернуть false если несколько? (во втором случае понадобится пауза)

        // FIXME?: сейчас отношение можно получить только из value, но может это измениться, тогда придется фиксить
        RelationshipValue relValue = (RelationshipValue) arg(1);
        String relName = relValue.value();
        if(RelationshipsDictionary.args(relName).size() != 2) {
            throw new IllegalArgumentException("Отношение не является бинарным");
        }

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

        value = NamingManager.genVarName();
        rulePart = compiledArg0.rulePart() + compiledArg1.rulePart();
        completedRules = compiledArg0.completedRules() + compiledArg1.completedRules();

        String relPattern = RelationshipsDictionary.pattern(relName).first();

        relPattern = relPattern.replace("<arg1>", compiledArg0.value());
        relPattern = relPattern.replace("<arg2>", value);

        int varCount = RelationshipsDictionary.varCount(relName);
        for (int i = 1; i <= varCount; ++i) {
            relPattern = relPattern.replace("<var" + i + ">", NamingManager.genVarName());
        }

        rulePart += relPattern;
        completedRules += RelationshipsDictionary.pattern(relName).second();

        usedObjects = List.of(compiledArg0.value());

        return new CompilationResult(value, rulePart, completedRules);
    }
}
