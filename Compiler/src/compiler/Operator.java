package compiler;

import dictionaries.RelationshipsDictionary;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;
import util.CompilationResult;

import java.util.ArrayList;
import java.util.List;

import static util.JenaUtil.*;

/**
 * Оператор
 */
public interface Operator {
    /**
     * Список аргументов
     * @return Список аргументов
     */
    default List<Operator> args() { return new ArrayList<>(); }

    /**
     * Список использованных объектов
     * @return Список использованных объектов
     */
    default List<String> usedObjects() { return new ArrayList<>(); }

    /**
     * Получить аргумент
     * @param index Индекс аргумента
     * @return Аргумент
     */
    default Operator arg(int index) { return args().get(index); }

    /**
     * Список типов данных аргументов
     * @return Список типов данных аргументов
     */
    // FIXME: Придумать более удобный способ задания нескольких комбинаций типов входных данных?
    List<List<DataType>> argsDataTypes();

    /**
     * Является ли количество аргументов бесконечным
     * @return true - если является, иначе - false
     */
    default Boolean isArgsCountUnlimited() { return false; }

    /**
     * Тип данных оператора
     * @return Тип данных оператора
     */
    DataType resultDataType();

    /**
     * Скомпилировать выражение
     * @return Правила для вычисления выражения и имя флага для чтения результата (если есть)
     */
    // TODO: валидация переменных, вводимых операторами
    default CompilationResult compileExpression() {
        // Компилируем оператор
        CompilationResult result = compile();

        // Добавляем вспомогательное правило нумерации, если нужно
        String rules = RelationshipsDictionary.isLinerScaleUsed() ?
                RelationshipsDictionary.NUMERATION_RULES + result.completedRules() :
                result.completedRules();

        // Генерируем имена
        String skolemName = NamingManager.genVarName();
        String resFlagName = resultDataType() != null ? NamingManager.genFlagName() : "";
        String resVarName = result.value();

        // Если есть незаконченное правило
        if(!result.rulePart().isEmpty() && resultDataType() != null) {
            // Генерируем правило
            String rule = "";
            if (resultDataType() == DataType.BOOLEAN) rule = JenaUtil.genBooleanRule(result.rulePart(), skolemName, skolemName, resFlagName);
            else rule = JenaUtil.genRule(result.rulePart(), skolemName, skolemName, resFlagName, resVarName);

            // Добавляем правило к остальным
            rules += rule;
        }

        return new CompilationResult(resFlagName, "", rules);
    }

    /**
     * Скомпилировать оператор
     * @return Правила для вычисления выражения, часть правила для проверки и имя флага для чтения результата (если есть)
     */
    // FIXME: Оптимизация правил? (удаление одинаковых строк)
    CompilationResult compile();
}
