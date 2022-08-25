package util;

/**
 * Описывает промежуточный результат компиляции выражения на Jena
 * @param value Указатель на результат. Это может быть ссылка на элемент графа, имя переменной в правиле
 *              или имя ссылки, которая указывает на результат всего выражения
 * @param rulePart Часть правила Jena, которая инициализирует переменную, указанную в value.
 *                 Используется только в процессе компиляции для передачи частей правил.
 *                 В результате компиляции выражения эта переменная содержит пустую строку
 * @param completedRules Полностью скомпилированные правила Jena
 */
public record CompilationResult(String value, String rulePart, String completedRules) { }
