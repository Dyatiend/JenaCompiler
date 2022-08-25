package compiler.operators;

import compiler.Operator;
import compiler.values.DecisionTreeVarValue;
import compiler.values.RelationshipValue;
import dictionaries.ClassesDictionary;
import dictionaries.RelationshipsDictionary;
import util.CompilationResult;
import util.DataType;
import util.NamingManager;
import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        String rulePart = "";
        String completedRules = "";
        StringBuilder rulePartBuilder = new StringBuilder();
        StringBuilder completedRulesBuilder = new StringBuilder();

        // Проверяем кол-во аргументов
        // FIXME?: сейчас отношение можно получить только из value, но может это измениться, тогда придется фиксить
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
            rulePartBuilder.append(compiledArgs.get(compiledArgs.size() - 1).rulePart());
            completedRulesBuilder.append(compiledArgs.get(compiledArgs.size() - 1).completedRules());
        }
        rulePart = rulePartBuilder.toString();
        completedRules = completedRulesBuilder.toString();

        // ------------------- Добавляем переходы между классами --------------------

        // Способы получения объекта:
        // Получить объект, связанный отношением
        // Получить объект по условию - TODO: как вычислить класс из условия
        // Получить экстремальный объект по условию и отношению - TODO: как вычислить класс из условия
        // Переменная дерева мысли
        // Переменная, вводимая квантором - TODO: как определить, если нужна инфа с верхнего уровня?
        // Прямая ссылка - невозможно определить

        for(int i = 1; i < args().size(); ++i) {
            String actualClass = "";
            String expectedClass = "";

            // Определяем классы аргументов
            if(arg(i) instanceof GetByRelationship arg) {
                actualClass = RelationshipsDictionary.args(((RelationshipValue)arg.arg(1)).value()).get(1);
                expectedClass = RelationshipsDictionary.args(relName).get(i - 1);
            }
            else if (arg(i) instanceof DecisionTreeVarValue arg) {
                actualClass = ClassesDictionary.decisionTreeVarClass(arg.value());
                expectedClass = RelationshipsDictionary.args(relName).get(i - 1);
            }

            if(!expectedClass.equals(actualClass)) {
                if(ClassesDictionary.hasTransition(actualClass, expectedClass)) {
                    // TODO: КАК ОПРЕДЕЛЯТЬ КАКОЙ ИЗ ДВУХ ТОКЕНОВ БРАТЬ?
                    String transitionRel = ClassesDictionary.transition(actualClass, expectedClass);
                    String transitionPattern = RelationshipsDictionary.pattern(transitionRel).first();

                    // FIXME: связь перехода всегда простая бинарная?
                    String newArgValue = NamingManager.genVarName();
                    transitionPattern = transitionPattern.replace("<arg1>", argValues.get(i));
                    transitionPattern = transitionPattern.replace("<arg2>", newArgValue);
                    argValues.set(i, newArgValue);
                    rulePart += transitionPattern;
                    completedRules += RelationshipsDictionary.pattern(transitionRel).second();
                }
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

        rulePart += relPattern;
        completedRules += RelationshipsDictionary.pattern(relName).second();

        usedObjects = new ArrayList<>(argValues);

        return new CompilationResult(value, rulePart, completedRules);
    }
}
