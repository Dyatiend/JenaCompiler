package compiler

import util.DataType

/**
 * Литерал в выражении
 * @param value Значение
 */
abstract class Literal(
    val value: String
) : Operator {

    override val args: List<Operator> = ArrayList()

    override val argsDataTypes: List<List<DataType>> = ArrayList()

    override fun clone(newArgs: List<Operator>): Operator {
        require(newArgs.isEmpty()) { "Для литерала аргументы не требуются." }
        return clone()
    }
}