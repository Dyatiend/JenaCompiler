package compiler.operators;

import compiler.Operator;
import compiler.values.DecisionTreeVarValue;
import compiler.values.RelationshipValue;
import dictionaries.ClassesDictionary;
import dictionaries.RelationshipsDictionary;
import util.CompilationResult;
import util.DataType;
import util.NamingManager;

import java.util.ArrayList;
import java.util.List;

public class CheckRelationship extends BaseOperator {

    /**
     * Конструктор
     * @param args Аргументы
     */
    public CheckRelationship(List<Operator> args) {
        super(args);
    }

    @Override
    public List<List<DataType>> argsDataTypes() {
        List<List<DataType>> result = new ArrayList<>();

        result.add(List.of(DataType.RELATIONSHIP, DataType.OBJECT));

        return result;
    }

    @Override
    public Boolean isArgsCountUnlimited() {
        return true;
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
        StringBuilder rulePartBuilder = new StringBuilder();
        StringBuilder completedRulesBuilder = new StringBuilder();

        // FIXME?: сейчас отношение можно получить только из value, если это изменится, тогда придется фиксить
        // Проверяем кол-во аргументов
        RelationshipValue relValue = (RelationshipValue) arg(0);
        String relName = relValue.value();
        if(RelationshipsDictionary.args(relName).size() != args().size() - 1) {
            throw new IllegalArgumentException("Некорректное количество аргументов");
        }

        // Компилируем аргументы
        List<CompilationResult> compiledArgs = new ArrayList<>();
        List<String> argValues = new ArrayList<>();
        for(Operator arg : args()) {
            compiledArgs.add(arg.compile());
            argValues.add(compiledArgs.get(compiledArgs.size()-1).value());
            rulePartBuilder.append(compiledArgs.get(compiledArgs.size() - 1).ruleHead());
            completedRulesBuilder.append(compiledArgs.get(compiledArgs.size() - 1).completedRules());
        }
        ruleHead = rulePartBuilder.toString();
        completedRules = completedRulesBuilder.toString();

        // ------------------- Добавляем переходы между классами --------------------

        for(int i = 1; i < args().size(); ++i) {
            String actualClass = "";
            String expectedClass = "";

            // ------------------- Определяем классы аргументов --------------------

            // TODO: добавить способы "Переменная, вводимая оператором", "Объект, полученный по условию"
            //  и "Экстремальный объект" после добавления таблицы
            // Если объект получен через отношение
            if(arg(i) instanceof GetByRelationship arg) {
                actualClass = RelationshipsDictionary.args(((RelationshipValue)arg.arg(1)).value()).get(1);
                expectedClass = RelationshipsDictionary.args(relName).get(i - 1);
            }
            // Если объект получен из переменной дерева
            else if (arg(i) instanceof DecisionTreeVarValue arg) {
                actualClass = ClassesDictionary.decisionTreeVarClass(arg.value());
                expectedClass = RelationshipsDictionary.args(relName).get(i - 1);
            }

            // Если классы не равны
            if(!expectedClass.equals(actualClass)) {
                // Если есть переход между классами
                if(ClassesDictionary.hasTransition(actualClass, expectedClass)) {
                    // TODO: определять необходимый объект в ситуации, когда связь не один к одному
                    String transitionRel = ClassesDictionary.transition(actualClass, expectedClass);
                    String transitionPattern = RelationshipsDictionary.pattern(transitionRel).first();

                    // FIXME?: добавить заполнение вспомогательных переменных, если появятся такие переходы
                    // Добавляем переход
                    String newArgValue = NamingManager.genVarName();

                    transitionPattern = transitionPattern.replace("<arg1>", argValues.get(i));
                    transitionPattern = transitionPattern.replace("<arg2>", newArgValue);
                    argValues.set(i, newArgValue);

                    ruleHead += transitionPattern;
                    completedRules += RelationshipsDictionary.pattern(transitionRel).second();
                }
                // Иначе - исключение
                else {
                    throw new IllegalArgumentException("Некорректный класс объекта " + i);
                }
            }
        }

        // ------------------- Добавляем проверку отношения --------------------

        String relPattern = RelationshipsDictionary.pattern(relName).first();
        for(int i = 1; i <= RelationshipsDictionary.args(relName).size(); ++i) {
            relPattern = relPattern.replace("<arg" + i + ">", argValues.get(i));
        }
        for(int i = 1; i <= RelationshipsDictionary.varCount(relName); ++i) {
            relPattern = relPattern.replace("<var" + i + ">", NamingManager.genVarName());
        }

        ruleHead += relPattern;
        completedRules += RelationshipsDictionary.pattern(relName).second();

        usedObjects = new ArrayList<>(argValues);
        usedObjects.remove(0);

        return new CompilationResult(value, ruleHead, completedRules);
    }
}
