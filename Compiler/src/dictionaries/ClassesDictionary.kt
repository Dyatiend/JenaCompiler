package dictionaries

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import compiler.Operator
import compiler.Variable
import compiler.operators.CheckPropertyValue
import compiler.operators.LogicalNot
import compiler.values.EnumValue
import compiler.values.PropertyValue

/**
 * Словарь классов
 */
object ClassesDictionary {

    // +++++++++++++++++++++++++++++++++ Свойства ++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Список классов
     * key - класс
     * val - предок
     */
    private var classes: MutableMap<String, String?> = HashMap()

    /**
     * Вычисляемые классы
     * Класс вычисляется путем вычисления boolean выражения над объектом
     * key - класс
     * val - First - имя переменной, куда подставить объект, Second - выражение для вычисления
     */
    private var calculations: MutableMap<String, Pair<String, Operator>> = HashMap()

    /**
     * Переходы между классами
     * row - источник
     * col - назначение
     * val - отношение, по которому идет переход
     */
    private var transitions: Table<String, String, String> = HashBasedTable.create()

    /**
     * Переменные дерева мысли
     * key - имя переменной
     * val - класс переменной
     */
    private var decisionTreeVars: MutableMap<String, String> = HashMap()

    // ++++++++++++++++++++++++++++++++ Инициализация ++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    fun init(path: String) {
        // Очищаем старые значения
        classes.clear()
        calculations.clear()
        transitions.clear()
        decisionTreeVars.clear()

        // TODO: чтение из файла

        // Добавляем классы
        classes["token"] = null
        classes["element"] = null
        classes["operand"] = null
        classes["operator"] = null
        classes["plus"] = "element"
        classes["minus"] = "element"
        classes["multiplication"] = "element"
        classes["division"] = "element"
        classes["squareParenthesis"] = "element"
        classes["parenthesis"] = "element"

        // Добавляем выражения для вычисления классов
        var variable: Operator = Variable("obj")
        var property: Operator = PropertyValue("state")
        var value: Operator = EnumValue("unevaluated", true)
        var checkProperty: Operator = CheckPropertyValue(listOf(variable, property, value))
        val not: Operator = LogicalNot(listOf(checkProperty))

        calculations["operand"] = Pair("obj", not)

        variable = Variable("obj")
        property = PropertyValue("state")
        value = EnumValue("unevaluated", true)
        checkProperty = CheckPropertyValue(listOf(variable, property, value))

        calculations["operator"] = Pair("obj", checkProperty)

        // Добавляем переходы
        transitions.put("element", "token", "has")
        transitions.put("token", "element", "belongsTo")

        // Добавляем переменные
        decisionTreeVars["X"] = "element"
        decisionTreeVars["X1"] = "token"
        decisionTreeVars["X2"] = "token"
        decisionTreeVars["Y"] = "element"
        decisionTreeVars["Y1"] = "token"
        decisionTreeVars["Y2"] = "token"
        decisionTreeVars["Z"] = "element"
        decisionTreeVars["Z1"] = "token"
        decisionTreeVars["Z2"] = "token"
        decisionTreeVars["A"] = "element"
        decisionTreeVars["B"] = "element"
        decisionTreeVars["T"] = "element"
        decisionTreeVars["U"] = "element"
    }

    // ++++++++++++++++++++++++++++++++++++ Методы +++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Существует ли класс
     * @param className Имя класса
     * @return true - если существует, иначе - false
     */
    fun exist(className: String): Boolean = classes.containsKey(className)

    /**
     * Является ли класс родителем другого
     * @param parent Родитель
     * @param child Ребенок
     * @return true - если является, иначе - false
     */
    fun isParentOf(parent: String, child: String): Boolean {
        if (!exist(parent) || !exist(child)) return false
        return if (classes[child] == parent) true else isParentOf(parent, classes[child]!!)
    }

    /**
     * Вычисляемый ли класс
     * @param className Имя класса
     * @return true - если вычисляемый, иначе - false
     */
    fun isComputable(className: String): Boolean = calculations.containsKey(className)

    /**
     * Как вычислить класс
     * @param className Имя класса
     * @return Выражение для вычисления и имя переменной, в которую надо подставить объект
     */
    fun howToCalculate(className: String): Pair<String, Operator>? {
        return if (!isComputable(className)) null else calculations[className]
    }

    /**
     * Есть ли переход между классами
     * @param from Источник
     * @param to Назначение
     * @return true - если есть переход, иначе - false
     */
    fun hasTransition(from: String, to: String): Boolean = transitions.contains(from, to)

    /**
     * Переход между классами
     * @param from Источник
     * @param to Назначение
     * @return Имя отношения, по которому идет переход
     */
    fun transition(from: String, to: String): String? {
        return if (!hasTransition(from, to)) null else transitions[from, to]
    }

    /**
     * Получить класс переменной дерева мысли
     * @param varName Имя переменной
     * @return Класс переменной
     */
    fun decisionTreeVarClass(varName: String): String? = decisionTreeVars[varName]
}