package compiler.operators;

import compiler.Operator;
import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import java.util.ArrayList;
import java.util.List;

import static util.JenaUtil.POAS_PREF;
import static util.JenaUtil.VAR_LINK;

/**
 * Присваивание
 */
public class Assign extends BaseOperator {

    /**
     * Шаблон правила присваивания значения свойства
     */
    private static final String PROPERTY_ASSIGN_PATTERN = "[<rulePart>->(<subjName> <propName> <value>)]";

    /**
     * Шаблон правила присваивания значения переменной дерева мысли
     */
    private static final String DECISION_TREE_VAR_ASSIGN_PATTERN = "[<rulePart>->drop(0)(<newObj> <varLink> <varName>)]";

    /**
     * Конструктор
     * @param args Аргументы
     */
    public Assign(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();
        result.add(List.of(DataType.OBJECT, DataType.PROPERTY, DataType.LITERAL));
        result.add(List.of(DataType.DECISION_TREE_VAR, DataType.OBJECT));
        return result;
    }

    @Override
    public DataType resultDataType() {
        return null;
    }

    @Override
    public CompilationResult compile() {
        // Объявляем переменные
        String value = "";
        String rulePart = "";
        String completedRules = "";

        if(args().size() == 3) {
            // Получаем аргументы
            Operator arg0 = arg(0);
            Operator arg1 = arg(1);
            Operator arg2 = arg(2);

            // Компилируем аргументы
            CompilationResult compiledArg0 = arg0.compile();
            CompilationResult compiledArg1 = arg1.compile();
            CompilationResult compiledArg2 = arg2.compile();

            // Генерируем правило
            String part = compiledArg0.rulePart() + compiledArg1.rulePart() + compiledArg2.rulePart();
            String rule = PROPERTY_ASSIGN_PATTERN;
            rule = rule.replace("<rulePart>", part);
            rule = rule.replace("<subjName>", compiledArg0.value());
            rule = rule.replace("<propName>", compiledArg1.value());
            rule = rule.replace("<value>", compiledArg2.value());

            // Добавляем правило в набор правил
            completedRules = compiledArg0.completedRules() +
                    compiledArg1.completedRules() +
                    compiledArg2.completedRules() +
                    rule;
        }
        else {
            // Получаем аргументы
            Operator arg0 = arg(0);
            Operator arg1 = arg(1);

            // Компилируем аргументы
            CompilationResult compiledArg0 = arg0.compile();
            CompilationResult compiledArg1 = arg1.compile();

            // Генерируем правило
            String part = compiledArg0.rulePart() + compiledArg1.rulePart();
            String rule = DECISION_TREE_VAR_ASSIGN_PATTERN;
            rule = rule.replace("<rulePart>", part);
            rule = rule.replace("<newObj>", compiledArg1.value());
            rule = rule.replace("<varLink>", JenaUtil.genRuleLink(POAS_PREF, VAR_LINK));
            // FIXME?: сейчас переменную можно получить только из value, но может это измениться, тогда придется фиксить
            rule = rule.replace("<varName>", ((Value) arg0).value()); // Получаем имя переменной

            // Добавляем правило в набор правил
            completedRules = compiledArg0.completedRules() +
                    compiledArg1.completedRules() +
                    rule;
        }

        return new CompilationResult(value, rulePart, completedRules);
    }
}
