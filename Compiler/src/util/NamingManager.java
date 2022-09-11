package util;

import static util.JenaUtil.POAS_PREF;

/**
 * Генерирует уникальные идентификаторы для различных элементов правил
 */
public class NamingManager {

    // FIXME?: могут возникнуть проблемы из-за одинаковых названий?

    /**
     * Индекс для переменных
     */
    private static int varIndex = 0;

    /**
     * Индекс для предикатов
     */
    private static int predIndex = 0;

    /**
     * Генерирует уникальное имя для переменной, не совпадающее с пользовательскими именами переменных
     * @return Имя переменной
     */
    public static String genVarName() { return JenaUtil.genVar("~" + varIndex++ + "~"); }

    /**
     * Генерирует уникальное имя для предиката, не совпадающее с пользовательскими именами предикатов
     * @return Имя предиката
     */
    public static String genPredName() { return JenaUtil.genLink(POAS_PREF, "~" + predIndex++ + "~"); }
}
