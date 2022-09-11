package compiler.operators;

import compiler.Operator;
import compiler.Value;
import dictionaries.PropertiesDictionary;
import dictionaries.RelationshipsDictionary;
import util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Оператор проверки значения свойства объекта
 */
public class CheckPropertyValue extends BaseOperator {

    /**
     * Имя предиката, используемое при компиляции
     */
    private static final String CLASS_PRED_NAME = "type";

    /**
     * Имя предиката, используемое при компиляции
     */
    private static final String SUBCLASS_PRED_NAME = "subClassOf";

    /**
     * Шаблон правила выбора экстремального класса
     */
    private static final String EXTREME_CLASS_PATTER = "[<ruleHead>->drop(0)]";

    /**
     * Конструктор
     * @param args Аргументы
     */
    public CheckPropertyValue(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();

        result.add(List.of(DataType.OBJECT, DataType.PROPERTY, DataType.LITERAL));

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
        Operator arg2 = arg(2);

        // Компилируем аргументы
        CompilationResult compiledArg0 = arg0.compile();
        CompilationResult compiledArg1 = arg1.compile();
        CompilationResult compiledArg2 = arg2.compile();

        // FIXME?: сейчас свойство можно получить только из value, если это изменится, тогда придется фиксить
        // Проверяем, переопределяется ли свойство
        if(PropertiesDictionary.isPropertyBeingOverridden(((Value) arg1).value())) {
            // FIXME?: реализация через extreme?

            // Вспомогательные переменные
            String empty1 = NamingManager.genVarName();
            String empty2 = NamingManager.genVarName();

            // ---------------- Генерируем правило, помечающее классы объекта --------------

            // Флаг, указывающий на классы объекта с заданным свойством
            String classWithPropFlagName = NamingManager.genPredName();
            // Переменная с классом
            String classVarName = NamingManager.genVarName();
            // Skolem name
            String skolemName = NamingManager.genVarName();

            // Собираем правило, помечающее классы
            String part = compiledArg0.ruleHead() + compiledArg1.ruleHead() + compiledArg2.ruleHead();
            part += JenaUtil.genTriple(
                    compiledArg0.value(),
                    JenaUtil.genLink(JenaUtil.RDF_PREF, CLASS_PRED_NAME),
                    classVarName);
            part += JenaUtil.genTriple(
                    classVarName,
                    compiledArg1.value(),
                    empty1);

            String rule = JenaUtil.genRule(part, skolemName, skolemName, classWithPropFlagName, classVarName);

            // ---------------- Генерируем правило, помечающее потенциальный экстремум --------------

            // Флаг цикла
            String cycleFlagName = NamingManager.genPredName();
            // Переменная цикла
            String cycleVar = NamingManager.genVarName();

            // Собираем правило, организующее цикл
            String cyclePart = JenaUtil.genNoValuePrim(empty1, cycleFlagName) +
                    JenaUtil.genTriple(empty2, classWithPropFlagName, cycleVar);
            String cycleRule = JenaUtil.genRule(cyclePart, skolemName, skolemName, cycleFlagName, cycleVar);

            // ---------------- Генерируем правило, проверяющее экстремум --------------

            // Переменные аргументов
            String ruleArg1 = NamingManager.genVarName();
            String ruleArg2 = NamingManager.genVarName();
            String ruleArg3 = NamingManager.genVarName();

            // Собираем правило, выбирающее ближайший класс

            // Инициализируем аргумент 1
            String filterPart = JenaUtil.genTriple(empty1, cycleFlagName, ruleArg1);
            // Инициализируем аргумент 2
            filterPart += JenaUtil.genNoValuePrim(ruleArg2, JenaUtil.genLink(JenaUtil.RDF_PREF, SUBCLASS_PRED_NAME));
            // Инициализируем аргумент 3
            filterPart += JenaUtil.genTriple(empty2, classWithPropFlagName, ruleArg3);

            // FIXME: ХАРДКОД вычисления отношения
            // Вычисляем экстремальное отношение
            String extremeRelName = "class_isFurtherFromThan";
            Pair<String, String> relPattern = RelationshipsDictionary.pattern(extremeRelName);
            String patter = relPattern.first();

            patter = patter.replace("<arg1>", ruleArg1);
            patter = patter.replace("<arg2>", ruleArg2);
            patter = patter.replace("<arg3>", ruleArg3);

            int varCount = RelationshipsDictionary.varCount(extremeRelName);
            for (int i = 1; i <= varCount; ++i) {
                patter = patter.replace("<var" + i + ">", NamingManager.genVarName());
            }

            filterPart += patter;

            String filterRule = EXTREME_CLASS_PATTER;
            filterRule = filterRule.replace("<ruleHead>", filterPart);

            // Добавляем в основное правило
            ruleHead = JenaUtil.genTriple(empty1, cycleFlagName, classVarName) +
                    JenaUtil.genTriple(classVarName, compiledArg1.value(), compiledArg2.value());

            // Собираем правила
            completedRules = compiledArg0.completedRules() +
                    compiledArg1.completedRules() +
                    compiledArg2.completedRules() +
                    rule + cycleRule + relPattern.second() + filterRule + JenaUtil.PAUSE_MARK;
        }
        else {
            // TODO?: может ли быть ситуация, когда свойство находится у дочернего объекта (например, у токена)?
            // Собираем правило
            ruleHead = compiledArg0.ruleHead() + compiledArg1.ruleHead() + compiledArg2.ruleHead(); // Собираем части аргмуентов
            // Добавляем проверку свойства
            ruleHead += JenaUtil.genTriple(
                    compiledArg0.value(),
                    compiledArg1.value(),
                    compiledArg2.value());

            // Передаем завершенные правила дальше
            completedRules = compiledArg0.completedRules() + compiledArg1.completedRules() + compiledArg2.completedRules();
        }

        usedObjects = List.of(compiledArg0.value());

        return new CompilationResult(value, ruleHead, completedRules);
    }
}
