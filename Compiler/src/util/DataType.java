package util;

// FIXME: заглушка. Типы данных используются только для валидации, поэтому пока достаточно ENUM
// TODO?: тип данных ENUM?
/**
 * Типы данных
 */
public enum DataType {

    /**
     * Переменная дерева мысли
     */
    DECISION_TREE_VAR,

    /**
     * Класс
     */
    CLASS,

    /**
     * Объект
     */
    OBJECT,

    /**
     * Свойство
     */
    PROPERTY,

    /**
     * Отношение
     */
    RELATIONSHIP,

    /**
     * Строка
     */
    STRING,

    /**
     * Булево значение
     */
    BOOLEAN,

    /**
     * Целое число
     */
    INTEGER,

    /**
     * Дробное число
     */
    DOUBLE,

    /**
     * Результат сравнения
     */
    COMPARISON_RESULT,

    /**
     * Литерал (Строка | Булево значение | Целое число | Дробное число)
     */
    LITERAL;

    /**
     * Может ли один тип быть преобразован в другой
     * @param from Тип, который преобразовываем
     * @param to Тип, в который преобразовываем
     * @return Может ли один тип быть преобразован в другой
     */
    public static boolean canCast(DataType from, DataType to) {
        return from == DECISION_TREE_VAR && to == OBJECT ||
                from == STRING && to == LITERAL ||
                from == BOOLEAN && to == LITERAL ||
                from == INTEGER && to == LITERAL ||
                from == DOUBLE && to == LITERAL;
    }

    /**
     * Может ли этот тип быть преобразован в другой
     * @param to Тип, в который преобразовываем
     * @return Может ли этот тип быть преобразован в другой
     */
    public boolean canCast(DataType to) {
        return this == DECISION_TREE_VAR && to == OBJECT ||
                this == STRING && to == LITERAL ||
                this == BOOLEAN && to == LITERAL ||
                this == INTEGER && to == LITERAL ||
                this == DOUBLE && to == LITERAL;
    }
}
