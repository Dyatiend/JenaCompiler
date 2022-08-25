package util;

import static util.JenaUtil.POAS_PREF;

/**
 * Генерирует уникальные идентификаторы для различных элементов правил
 */
public class NamingManager {
    // FIXME: могут возникнуть проблемы из-за одинаковых названий?????
    private static int varIndex = 0;
    private static int flagIndex = 0;

    /**
     * Генерирует уникальное имя для переменной, не совпадающее с пользовательскими именами переменных
     * @return Имя переменной
     */
    public static String genVarName() { return JenaUtil.genRuleVar("&" + varIndex++ + "&"); }

    /**
     * Генерирует уникальное имя для флага, не совпадающее с пользовательскими именами свойств
     * @return Имя флага
     */
    // TODO: кастомные префиксы?
    public static String genFlagName() { return JenaUtil.genRuleLink(POAS_PREF, "&" + flagIndex++ + "&"); }
}
