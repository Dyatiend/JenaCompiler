package compiler.operators

import compiler.Operator
import compiler.util.CompilationResult
import util.DataType
import util.JenaUtil.COMPARE_RESULT_PREDICATE
import util.JenaUtil.POAS_PREF
import util.JenaUtil.genEqualPrim
import util.JenaUtil.genGreaterThanPrim
import util.JenaUtil.genLessThanPrim
import util.JenaUtil.genLink
import util.JenaUtil.genNotEqualPrim
import util.JenaUtil.genTriple
import util.JenaUtil.genVal
import util.NamingManager.genVarName

/**
 * Сравнение
 * TODO?: сравнение объектов на больше/меньше?
 */
class Compare(args: List<Operator>) : BaseOperator(args) {

    override val argsDataTypes
        get() = listOf(
            listOf(DataType.Integer, DataType.Double),
            listOf(DataType.Double, DataType.Integer),
            listOf(DataType.Integer, DataType.Integer),
            listOf(DataType.Double, DataType.Double),
            listOf(DataType.String, DataType.String),
            listOf(DataType.Object, DataType.Object)
        )

    override val resultDataType get() = DataType.ComparisonResult

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val value = genVarName()
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()

        // Передаем завершенные правила дальше
        completedRules += compiledArg0.rules +
                compiledArg1.rules

        // Вспомогательные переменные
        val empty = genVarName()
        val compareResultPredicate = genLink(POAS_PREF, COMPARE_RESULT_PREDICATE)

        // Для всех результатов компиляции
        compiledArg0.bodies.forEach { head0 ->
            compiledArg1.bodies.forEach { head1 ->
                // Если аргументы можно сравнивать только на эквивалентность
                if (arg0.resultDataType == DataType.Object
                    || arg0.resultDataType == DataType.String
                ) {
                    // Правило для эквивалентности
                    var equalHead = head0 + head1
                    equalHead += genEqualPrim(compiledArg0.value, compiledArg1.value)
                    // Инициализация value
                    equalHead += genTriple(
                        empty,
                        compareResultPredicate,
                        genVal(compiler.util.ComparisonResult.Equal.toString())
                    )
                    equalHead += genTriple(
                        empty,
                        compareResultPredicate,
                        value
                    )

                    // Правило для неэквивалентности
                    var notEqualHead = head0 + head1
                    notEqualHead += genNotEqualPrim(compiledArg0.value, compiledArg1.value)
                    // Инициализация value
                    notEqualHead += genTriple(
                        empty,
                        compareResultPredicate,
                        genVal(compiler.util.ComparisonResult.NotEqual.toString())
                    )
                    notEqualHead += genTriple(
                        empty,
                        compareResultPredicate,
                        value
                    )

                    // Добавляем в рзультат
                    heads.add(equalHead)
                    heads.add(notEqualHead)
                } else {
                    // Правило для эквивалентности
                    var equalHead = head0 + head1
                    equalHead += genEqualPrim(compiledArg0.value, compiledArg1.value)
                    // Инициализация value
                    equalHead += genTriple(
                        empty,
                        compareResultPredicate,
                        genVal(compiler.util.ComparisonResult.Equal.toString())
                    )
                    equalHead += genTriple(
                        empty,
                        compareResultPredicate,
                        value
                    )

                    // Правило для меньше
                    var lessHead = head0 + head1
                    lessHead += genLessThanPrim(compiledArg0.value, compiledArg1.value)
                    // Инициализация value
                    lessHead += genTriple(
                        empty,
                        compareResultPredicate,
                        genVal(compiler.util.ComparisonResult.Less.toString())
                    )
                    lessHead += genTriple(
                        empty,
                        compareResultPredicate,
                        value
                    )

                    // Правило для больше
                    var greaterHead = head0 + head1
                    greaterHead += genGreaterThanPrim(compiledArg0.value, compiledArg1.value)
                    // Инициализация value
                    greaterHead += genTriple(
                        empty,
                        compareResultPredicate,
                        genVal(compiler.util.ComparisonResult.Greater.toString())
                    )
                    greaterHead += genTriple(
                        empty,
                        compareResultPredicate,
                        value
                    )

                    // Добавляем в рзультат
                    heads.add(equalHead)
                    heads.add(lessHead)
                    heads.add(greaterHead)
                }
            }
        }

        return CompilationResult(value, heads, completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        return Compare(newArgs)
    }

    override fun clone(newArgs: List<Operator>): Operator {
        return Compare(newArgs)
    }
}