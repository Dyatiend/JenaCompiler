package util

/**
 * Генерирует уникальные идентификаторы для различных элементов правил
 */
object NamingManager {

    /**
     * Индекс для переменных
     */
    private var varIndex = 0

    /**
     * Индекс для предикатов
     */
    private var predIndex = 0

    /**
     * Генерирует уникальное имя для переменной, не совпадающее с пользовательскими именами переменных
     * @return Имя переменной
     */
    fun genVarName(): String = JenaUtil.genVar("__${varIndex++}__")

    /**
     * Генерирует уникальное имя для предиката, не совпадающее с пользовательскими именами предикатов
     * @return Имя предиката
     */
    fun genPredName(): String = JenaUtil.genLink(JenaUtil.POAS_PREF, "__${predIndex++}__")
}