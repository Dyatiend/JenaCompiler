package compiler.operators

import compiler.Operator
import compiler.values.ClassValue
import dictionaries.ClassesDictionary.howToCalculate
import dictionaries.ClassesDictionary.isComputable
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.genBindPrim
import util.JenaUtil.genLink
import util.JenaUtil.genNoValuePrim
import util.JenaUtil.genTriple
import util.JenaUtil.genVar

/**
 * Оператор проверки класса объекта
 */
class CheckClass(args: List<Operator>) : BaseOperator(args) {

    /**
     * Является ли оператор негативным (т.е. нужно ли отрицание при компиляции)
     */
    internal var isNegative = false

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Object, DataType.Class))
    }

    override fun resultDataType(): DataType {
        return DataType.Boolean
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()

        // Если класс можно вычислить (вычисляемый класс можно получить только указав его имя т.е. через ClassValue)
        if (isComputable((arg1 as ClassValue).value)) {
            // Получаем выражение для вычисления
            val calculation = howToCalculate(arg1.value)

            // Проверям корректность словаря
            requireNotNull(calculation) { "Для класса ${arg1.value} в словаре нет выражения" }

            val varName = calculation.first
            var expression = calculation.second

            // Если негативная форма - добавляем отрицание
            if (isNegative) {
                expression = LogicalNot(listOf(expression))
            }

            // Компилируем выражение для вычисления
            val compiledCalculation = expression.simplify().compile()

            // Передаем завершенные правила дальше
            completedRules += compiledArg0.completedRules +
                    compiledArg1.completedRules +
                    compiledCalculation.completedRules

            // Для всех результатов компиляции
            compiledArg0.ruleHeads.forEach { head0 ->
                compiledArg1.ruleHeads.forEach { head1 ->
                    compiledCalculation.ruleHeads.forEach { calculationHead ->
                        // Собираем правило
                        var head = head0 + head1 // Собираем части первого и второго аргументов
                        head += genBindPrim(compiledArg0.value, genVar(varName)) // Инициализируем переменную
                        head += calculationHead // Добавляем результат компиляции вычисления

                        // Добавляем в массив
                        heads.add(head)
                    }
                }
            }
        } else {
            // Передаем завершенные правила дальше
            completedRules += compiledArg0.completedRules +
                    compiledArg1.completedRules

            // Для всех результатов компиляции
            compiledArg0.ruleHeads.forEach { head0 ->
                compiledArg1.ruleHeads.forEach { head1 ->
                    // Собираем правило
                    var head = head0 + head1 // Собираем части первого и второго аргументов

                    // Добавляем проверку класса
                    head += if (isNegative) {
                        // Если форма негативная - проверяем отсутствие класса
                        genNoValuePrim(
                            compiledArg0.value,
                            genLink(JenaUtil.RDF_PREF, CLASS_PREDICATE_NAME),
                            compiledArg1.value
                        )
                    } else {
                        // Проверяем наличие класса
                        genTriple(
                            compiledArg0.value,
                            genLink(JenaUtil.RDF_PREF, CLASS_PREDICATE_NAME),
                            compiledArg1.value
                        )
                    }

                    // Добавляем в массив
                    heads.add(head)
                }
            }
        }

        return CompilationResult("", heads, completedRules)
    }

    companion object {

        /**
         * Имя предиката, используемое при компиляции
         */
        private const val CLASS_PREDICATE_NAME = "type"
    }
}