package compiler

import util.DataType

/**
 * Литерал в выражении
 * @param value Значение
 */
abstract class Literal(
    val value: String
) : Operator {

    override val args: List<Operator>
        get() = emptyList()

    override val argsDataTypes: List<List<DataType>>
        get() = emptyList()

    override fun clone(newArgs: List<Operator>): Operator {
        require(newArgs.isEmpty()) { "Для литерала аргументы не требуются." }
        return clone()
    }
}