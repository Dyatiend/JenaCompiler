package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.ComparisonResult
import compiler.util.CompilationResult
import util.DataType
import util.JenaUtil

/**
 * Литерал результата сравнения
 * @param value Результат сравнения
 * @see ComparisonResult
 */
class ComparisonResultLiteral(value: ComparisonResult) : Literal(value.toString()) {

    override val resultDataType: DataType = DataType.ComparisonResult

    override fun compile(): CompilationResult =
        CompilationResult(value = JenaUtil.genLink(JenaUtil.POAS_PREF, value))

    override fun clone(): Operator = ComparisonResultLiteral(ComparisonResult.valueOf(value)!!)
}