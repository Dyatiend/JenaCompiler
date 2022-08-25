package util;

/**
 * Описывает результат сравнения
 */
public enum ComparisonResult {
    GREATER,
    LESS,
    EQUAL,
    UNDETERMINED;

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
