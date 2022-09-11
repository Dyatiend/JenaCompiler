package compiler;

import dictionaries.RelationshipsDictionary;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;
import util.CompilationResult;

import java.util.ArrayList;
import java.util.List;

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
     * Список объектов, использованных в правиле
     * @return Список объектов, использованных в правиле
     */
    default List<String> objectsUsedInRule() { return new ArrayList<>(); }

    /**
     * Получить аргумент
     * @param index Индекс аргумента
     * @return Аргумент
     */
    default Operator arg(int index) { return args().get(index); }

    // FIXME?: придумать более удобный способ задания нескольких комбинаций типов входных данных?
    /**
     * Список типов данных аргументов
     * @return Список типов данных аргументов
     */
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

    // TODO: валидация переменных, вводимых операторами
    // TODO: таблица переменных, вводимых операторами и их валидация
    // TODO?: оптимизация пауз?
    // TODO?: оптимизация правил? (удаление одинаковых строк)
    /**
     * Скомпилировать выражение
     * @return Правила для вычисления выражения и имя предиката для чтения результата (если есть)
     */
    default CompilationResult compileExpression() {
        // Компилируем оператор
        CompilationResult result = compile();

        // Добавляем вспомогательные правила, если нужно
        String rules = RelationshipsDictionary.isLinerScaleUsed() ?
                RelationshipsDictionary.auxiliaryLinerScaleRules() + result.completedRules() :
                result.completedRules();

        // Генерируем имена
        String skolemName = NamingManager.genVarName();
        String resPredName = resultDataType() != null ? NamingManager.genPredName() : "";
        String resVarName = result.value();

        // Если есть незаконченное правило
        if(!result.ruleHead().isEmpty() && resultDataType() != null) {
            // Генерируем правило
            String rule;
            if (resultDataType() == DataType.BOOLEAN)
                rule = JenaUtil.genBooleanRule(result.ruleHead(), skolemName, skolemName, resPredName);
            else
                rule = JenaUtil.genRule(result.ruleHead(), skolemName, skolemName, resPredName, resVarName);

            // Добавляем правило к остальным
            rules += rule;
        }

        return new CompilationResult(resPredName, "", rules);
    }

    /**
     * Скомпилировать оператор
     * @return Правила для вычисления выражения, часть правила для проверки и имя предиката для чтения результата (если есть)
     */
    CompilationResult compile();
}
