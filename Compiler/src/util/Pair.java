package util;

/**
 * Пара из двух объектов
 * @param <F> Тип первого объекта
 * @param <S> Тип второго объекта
 */
public record Pair<F, S>(F first, S second) { }
