package compiler.operators;

import compiler.Operator;
import util.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый оператор
 */
public abstract class BaseOperator implements Operator {
    /**
     * Аргументы
     */
    private final List<Operator> args;

    /**
     * Объекты, над которыми вычисляется оператор
     * TODO: передавать все и дочерние объекты?
     */
    protected List<String> usedObjects = new ArrayList<>();

    @Override
    public List<String> usedObjects() {
        return usedObjects;
    }

    /**
     * Конструктор
     * @param args Аргументы
     */
    public BaseOperator(List<Operator> args) {
        // Проверяем аргументы перед сохранением
        checkArgs(args);
        this.args = args;
    }

    /**
     * Проверка аргументов
     * @param args Аргументы
     */
    // TODO: Проверять переменные, вводимые операторами
    private void checkArgs(List<Operator> args) {
        // Получаем список типов данных
        List<DataType> actualArgsDataTypes = new ArrayList<>();
        for (Operator operator : args) {
            if(operator.resultDataType() == null) throw new IllegalArgumentException("Аргумент без возвращаемого значения");
            actualArgsDataTypes.add(operator.resultDataType());
        }

        // TODO?: упростить?
        boolean success = false;
        // Для каждого набора типа данных
        for (List<DataType> expectedArgsDataTypes : argsDataTypes()) {
            // Если количество аргументов неограниченно
            if(isArgsCountUnlimited()) {
                // Проверяем, что кол-во полученных аргументов больше или равно ожидаемым
                if(expectedArgsDataTypes.size() <= actualArgsDataTypes.size()) {
                    boolean equals = true;
                    // Сравниваем аргументы
                    for (int i = 0, j = 0; i < actualArgsDataTypes.size(); ++i, ++j) {
                        // Если дошли до последнего аргумента - сравниваем все полученные с последним ожидаемым
                        if (j > expectedArgsDataTypes.size() - 1) {
                            j = expectedArgsDataTypes.size() - 1;
                        }
                        // Аргументы совпадают - если типы данных равны или могут быть преобразованы
                        equals &= actualArgsDataTypes.get(i).equals(expectedArgsDataTypes.get(j)) ||
                                actualArgsDataTypes.get(i).canCast(expectedArgsDataTypes.get(j));
                    }
                    success |= equals;
                }
            }
            else {
                // Проверяем, что кол-во полученных аргументов равно ожидаемым
                if(expectedArgsDataTypes.size() == actualArgsDataTypes.size()) {
                    boolean equals = true;
                    // Сравниваем аргументы
                    for (int i = 0; i < actualArgsDataTypes.size(); ++i) {
                        // Аргументы совпадают - если типы данных равны или могут быть преобразованы
                        equals &= actualArgsDataTypes.get(i).equals(expectedArgsDataTypes.get(i)) ||
                                actualArgsDataTypes.get(i).canCast(expectedArgsDataTypes.get(i));
                    }
                    success |= equals;
                }
            }
        }

        // Если аргументы не совпали ни с одним набором - выбрасываем исключение
        if(!success) {
            throw new IllegalArgumentException("Набор аргументов не соответствует ни одной из вариаций оператора");
        }
    }

    @Override
    public List<Operator> args() {
        return args;
    }
}
