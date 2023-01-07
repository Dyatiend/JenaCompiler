package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.DECISION_TREE_VAR_PREDICATE
import util.JenaUtil.POAS_PREF
import util.JenaUtil.genTriple
import util.NamingManager

/**
 * Литерал переменной из дерева рассуждения
 * @param value Имя переменной из дерева рассуждения
 */
class DecisionTreeVarLiteral(value: String) : Literal(value) {

    override val resultDataType: DataType = DataType.DecisionTreeVar

    override fun compile(): CompilationResult {
        val resVarName = NamingManager.genVarName()
        return CompilationResult(
            value = resVarName,
            heads = listOf(
                genTriple(
                    resVarName,
                    JenaUtil.genLink(POAS_PREF, DECISION_TREE_VAR_PREDICATE),
                    JenaUtil.genVal(value)
                )
            )
        )
    }

    override fun clone(): Operator = DecisionTreeVarLiteral(value)
}