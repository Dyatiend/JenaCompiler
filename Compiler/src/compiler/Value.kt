package compiler

import util.DataType

/**
 * Значение в выражении
 */
abstract class Value (
    open val value: String
) : Operator {

    override fun argsDataTypes(): List<List<DataType>> = ArrayList()
}