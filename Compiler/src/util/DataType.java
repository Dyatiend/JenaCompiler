package util;

/**
 * Типы данных
 * FIXME: Заглушка. Типы данных используются только для валидации, поэтому пока достаточно ENUM
 * TODO: тип данных ENUM?
 */
public enum DataType {
    DECISION_TREE_VAR, // FIXME: изменить синтаксис оператора присваивания, чтобы убрать этот тип?

    CLASS,
    OBJECT,
    PROPERTY,
    RELATIONSHIP,

    STRING,
    BOOLEAN,
    INTEGER,
    DOUBLE,

    COMPARISON_RESULT,

    LITERAL;

    // Может ли один тип быть преобразован в другой
    // //FIXME: Убрать, если DECISION_TREE_VAR не будет отдельным типом данных
    public static boolean canCast(DataType from, DataType to) {
        return from == DECISION_TREE_VAR && to == OBJECT ||
                from == STRING && to == LITERAL ||
                from == BOOLEAN && to == LITERAL ||
                from == INTEGER && to == LITERAL ||
                from == DOUBLE && to == LITERAL;
    }

    public boolean canCast(DataType to) {
        return this == DECISION_TREE_VAR && to == OBJECT ||
                this == STRING && to == LITERAL ||
                this == BOOLEAN && to == LITERAL ||
                this == INTEGER && to == LITERAL ||
                this == DOUBLE && to == LITERAL;
    }
}
