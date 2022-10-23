package compiler.values

import compiler.Value
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.POAS_PREF
import util.JenaUtil.VAR_PRED
import util.JenaUtil.genTriple
import util.NamingManager

class DecisionTreeVarValue(value: String) : Value(value) {

    override fun resultDataType(): DataType = DataType.DecisionTreeVar

    override fun compile(): List<CompilationResult> {
        val resVarName = NamingManager.genVarName()
        return listOf(CompilationResult(
            resVarName,
            genTriple(resVarName, JenaUtil.genLink(POAS_PREF, VAR_PRED), JenaUtil.genStingVal(value)),
            ""
        ))
    }
}