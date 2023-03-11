package compiler.operators

import compiler.Operator
import compiler.literals.ClassLiteral
import compiler.util.CompilationResult
import dictionaries.ClassesDictionary.calcExpr
import dictionaries.ClassesDictionary.isCalculable
import models.ClassModel
import util.DataType
import util.JenaUtil
import util.JenaUtil.CLASS_PREDICATE_NAME
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

    override val argsDataTypes
        get() = listOf(listOf(DataType.Object, DataType.Class))

    override val resultDataType
        get() = DataType.Boolean

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val bodies = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()

        // Если класс можно вычислить (вычисляемый класс можно получить только указав его имя т.е. через ClassValue)
        if (isCalculable((arg1 as ClassLiteral).value)) {
            // Получаем выражение для вычисления
            val calculation = calcExpr(arg1.value)

            // Проверяем корректность словаря
            requireNotNull(calculation) { "Для класса ${arg1.value} в словаре нет выражения" }

            val varName = ClassModel.CALC_EXPR_VAR_NAME
            var expression = calculation

            // Если негативная форма - добавляем отрицание
            if (isNegative) {
                expression = LogicalNot(listOf(expression))
            }

            // Компилируем выражение для вычисления
            val compiledCalculation = expression.semantic().compile()

            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules +
                    compiledCalculation.rules

            // Для всех результатов компиляции
            compiledArg0.bodies.forEach { body0 ->
                compiledArg1.bodies.forEach { body1 ->
                    compiledCalculation.bodies.forEach { calculationBody ->
                        // Собираем правило
                        var body = body0 + body1 // Собираем части первого и второго аргументов
                        body += genBindPrim(compiledArg0.value, genVar(varName)) // Инициализируем переменную
                        body += calculationBody // Добавляем результат компиляции вычисления

                        // Добавляем в массив
                        bodies.add(body)
                    }
                }
            }
        } else {
            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules

            // Для всех результатов компиляции
            compiledArg0.bodies.forEach { body0 ->
                compiledArg1.bodies.forEach { body1 ->
                    // Собираем правило
                    var body = body0 + body1 // Собираем части первого и второго аргументов

                    // Добавляем проверку класса
                    body += if (isNegative) {
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
                    bodies.add(body)
                }
            }
        }

        return CompilationResult(bodies = bodies, rules = completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        return CheckClass(newArgs)
    }

    override fun clone(newArgs: List<Operator>): Operator {
        return CheckClass(newArgs)
    }
}