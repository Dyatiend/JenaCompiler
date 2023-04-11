package compiler.operators

import compiler.Operator
import compiler.literals.BooleanLiteral
import compiler.util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.genEqualPrim
import util.JenaUtil.genNoValuePrim
import util.JenaUtil.genTriple
import util.JenaUtil.genVar
import util.NamingManager
import util.NamingManager.genVarName

/**
 * Квантор существования
 *
 */
// TODO: отрицание
class ExistenceQuantifier(
    args: List<Operator>,
    private val varName: String
) : BaseOperator(args) {

    /**
     * Является ли оператор негативным (т.е. нужно ли отрицание при компиляции)
     */
    internal var isNegative = false

    override val argsDataTypes get() = listOf(listOf(DataType.Boolean, DataType.Boolean))

    override val resultDataType get() = DataType.Boolean

    override fun compile(): CompilationResult {
        // Объявляем переменные
        var res = ""
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()

        // Передаем завершенные правила дальше
        completedRules += compiledArg0.rules + compiledArg1.rules

        when {
            arg0 is BooleanLiteral && arg1 is BooleanLiteral -> {
                // Добавляем выражение, равное значению
                val head = arg0.compileAsBody() +
                        if (isNegative) BooleanLiteral(!arg1.value.toBoolean()).compileAsBody()
                        else arg1.compileAsBody()


                // Добавляем в массив
                heads.add(head)
            }

            arg0 is BooleanLiteral -> {
                // Добавляем выражение, равное значению
                val head0 = arg0.compileAsBody()

                if (isNegative) {
                    // Skolem name
                    val skolemName = genVarName()

                    val flag = NamingManager.genPredicateName()

                    res = genVar(varName)

                    // Для всех результатов компиляции
                    compiledArg1.bodies.forEach { head1 ->
                        val head = head0 + head1

                        val rule = JenaUtil.genRule(head, skolemName, flag, res)

                        completedRules += rule

                    }

                    completedRules += JenaUtil.PAUSE_MARK

                    val checkHead = head0 + genNoValuePrim(skolemName, flag, res)

                    // Добавляем в массив
                    heads.add(checkHead)
                } else {

                    // Для всех результатов компиляции
                    compiledArg1.bodies.forEach { head1 ->
                        val head = head0 + head1

                        // Добавляем в массив
                        heads.add(head)
                    }

                }

            }

            arg1 is BooleanLiteral -> {
                // Добавляем выражение, равное значению
                val head1 = if (isNegative) BooleanLiteral(!arg1.value.toBoolean()).compileAsBody()
                else arg1.compileAsBody()

                // Для всех результатов компиляции
                compiledArg0.bodies.forEach { head0 ->
                    val head = head0 + head1

                    // Добавляем в массив
                    heads.add(head)
                }
            }

            else -> {
                if (isNegative) {
                    // Skolem name
                    val skolemName = genVarName()

                    val flag = NamingManager.genPredicateName()

                    res = genVar(varName)

                    compiledArg0.bodies.forEach { head0 ->
                        compiledArg1.bodies.forEach { head1 ->
                            val head = head0 + head1

                            val rule = JenaUtil.genBooleanRule(head, skolemName, flag)

                            completedRules += rule
                        }

                        completedRules += JenaUtil.PAUSE_MARK

                        val checkHead = head0 + genNoValuePrim(skolemName, flag)

                        // Добавляем в массив
                        heads.add(checkHead)
                    }

                } else {
                    // Для всех результатов компиляции
                    compiledArg0.bodies.forEach { head0 ->
                        compiledArg1.bodies.forEach { head1 ->
                            val head = head0 + head1

                            // Добавляем в массив
                            heads.add(head)
                        }
                    }
                }

            }
        }
//
//
//        // Если оператор булево значение
//        if (arg0 is BooleanLiteral) {
//            // Добавляем выражение, равное значению
//            val head = if (isNegative) {
//                BooleanLiteral(!arg0.value.toBoolean()).compileAsBody()
//            } else {
//                arg0.compileAsBody()
//            }
//
//            // Добавляем в массив
//            heads.add(head)
//        } else {
//            if(isNegative) {
//                // Skolem name
//                val skolemName = genVarName()
//
//                val flag = NamingManager.genPredicateName()
//
//                res = genVar(varName)
//
//                // Для всех результатов компиляции
//                compiledArg0.bodies.forEach { head0 ->
//                    val head = head0
//
//
//
//                    val rule = JenaUtil.genRule(head, skolemName, flag, res)
//
//                    completedRules += rule
//
//                }
//
//                completedRules += JenaUtil.PAUSE_MARK
//
//                val checkHead = genNoValuePrim(skolemName, flag, res)
//
//                // Добавляем в массив
//                heads.add(checkHead)
//
//            } else {
//                // Для всех результатов компиляции
//                compiledArg0.bodies.forEach { head0 ->
//                    // Добавляем инициализацию переменной
//                    val head = head0
//
//                    // Добавляем в массив
//                    heads.add(head)
//                }
//            }

//            // Для всех результатов компиляции
//            compiledArg0.bodies.forEach { head0 ->
//                // Добавляем инициализацию переменной
//                val head = head0
//
//                // Skolem name
//                val skolemName = genVarName()
//
//                val flag = NamingManager.genPredicateName()
//
//                if (isNegative) {
//                    val rule = JenaUtil.genBooleanRule(head, skolemName, flag)
//
//                    completedRules += rule + JenaUtil.PAUSE_MARK
//
//                    val checkHead = genNoValuePrim(skolemName, flag)
//
//                    // Добавляем в массив
//                    heads.add(checkHead)
//                } else {
//                    // Добавляем в массив
//                    heads.add(head)
//                }
//            }
//        }

        return CompilationResult(res, heads, completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        return ExistenceQuantifier(newArgs, varName)
    }

    override fun clone(newArgs: List<Operator>): Operator {
        return ExistenceQuantifier(newArgs, varName)
    }
}