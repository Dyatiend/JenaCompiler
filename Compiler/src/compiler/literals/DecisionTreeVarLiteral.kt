package compiler.literals

import compiler.Literal
import compiler.Operator
import compiler.util.CompilationResult
import compiler.util.JenaUtil
import compiler.util.JenaUtil.DECISION_TREE_VAR_PREDICATE
import compiler.util.JenaUtil.POAS_PREF
import compiler.util.JenaUtil.genTriple
import compiler.util.NamingManager
import util.DataType

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