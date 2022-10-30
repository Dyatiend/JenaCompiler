package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF
import util.JenaUtil.VAR_PREDICATE
import util.JenaUtil.genTriple
import util.NamingManager

class DecisionTreeVarValue(value: String) : Value(value) {

    override fun resultDataType(): DataType = DataType.DecisionTreeVar

    override fun compile(): CompilationResult {
        val resVarName = NamingManager.genVarName()
        return CompilationResult(
            resVarName,
            listOf(genTriple(
                resVarName,
                JenaUtil.genLink(POAS_PREF, VAR_PREDICATE),
                JenaUtil.genStingVal(value)
            )),
            ""
        )
    }
}