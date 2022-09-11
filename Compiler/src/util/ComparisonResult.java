package util;

/**
 * Описывает результат сравнения
 */
public enum ComparisonResult {

    /**
     * Больше
     */
    GREATER,

    /**
     * Меньше
     */
    LESS,

    /**
     * Эквивалентно
     */
    EQUAL,

    /**
     * Не определено
     */
    UNDETERMINED;

    /**
     * Преобразование в строку
     * @param value Значение
     * @return Строка
     */
    public static String toString(ComparisonResult value) {
        switch (value) {
            case GREATER -> {
                return "GREATER";
            }
            case LESS -> {
                return "LESS";
            }
            case EQUAL -> {
                return "EQUAL";
            }
            default -> {
                return "UNDETERMINED";
            }
        }
    }
}
