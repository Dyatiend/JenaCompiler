package dictionaries

import util.DataType

/**
 * Словарь свойств
 */
object PropertiesDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Свойства
     * row - имя свойства
     * val - First - список классов с данным свойством, null - если относится к объекту, Second - тип данных
     */
    private val properties: MutableMap<String, Pair<List<String>?, DataType>> = HashMap()

    /**
     * Enum свойства
     * row - имя свойства
     * val - Отношение, определяющее порядок значений линейного enum
     */
    private val enums: MutableMap<String, String?> = HashMap()

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    fun init(path: String) {
        // Очищаем старые значения
        properties.clear()

        // TODO: чтение из файла

        // Добавляем свойства
        properties["countOfTokens"] = Pair(listOf(
                "element", "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Integer)
        properties["arity"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Enum)
        properties["place"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Enum)
        properties["precedence"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Integer)
        properties["associativity"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Enum)
        properties["needsLeftOperand"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Boolean)
        properties["needsRightOperand"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Boolean)
        properties["needsInnerOperand"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Boolean)
        properties["hasStrictOperandOrder"] = Pair(listOf(
                "plus", "minus", "multiplication",
                "division", "squareParenthesis", "parenthesis"
        ), DataType.Boolean)

        properties["state"] = Pair(null, DataType.LinerEnum)
        properties["evaluatesTo"] = Pair(null, DataType.Enum)

        enums["state"] = "state_directlyLeftOf"
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Существует ли свойство
     * @param propertyName Имя свойства
     * @return true - если существует, иначе - false
     */
    fun exist(propertyName: String): Boolean = properties.containsKey(propertyName)

    /**
     * Является ли статическим
     * @param propertyName Имя свойства
     * @return true - если относится к классу, false - если к объекту
     */
    fun isStatic(propertyName: String): Boolean {
        return if (!exist(propertyName)) false else properties[propertyName]?.first != null
    }

    /**
     * Тип данных
     * @param propertyName Имя свойства
     * @return Тип данных
     */
    fun dataType(propertyName: String): DataType? {
        return if (!exist(propertyName)) null else properties[propertyName]!!.second
    }

    /**
     * Получить отношение, задающее порядок значений enum свойства
     */
    fun orderRelationship(propertyName: String): String? = enums[propertyName]

    /**
     * Переопределяется ли свойство
     * @param propertyName Имя свойства
     * @return true - если переопределяется, иначе - false
     */
    fun isPropertyBeingOverridden(propertyName: String): Boolean {
        if (!exist(propertyName)) return false
        if (properties[propertyName]?.first == null) return false
        val classes: List<String> = properties[propertyName]?.first!!
        for (i in classes.indices) {
            for (j in classes.indices) {
                if (i == j) continue
                if (ClassesDictionary.isParentOf(classes[i], classes[j])) {
                    return true
                }
            }
        }
        return false
    }
}