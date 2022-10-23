package compiler.values;

import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;
import util.NamingManager;

import static util.JenaUtil.POAS_PREF;
import static util.JenaUtil.VAR_PRED;

public class DecisionTreeVarValue extends Value {

    public DecisionTreeVarValue(String value) {
        super(value);
    }

    @Override
    public DataType resultDataType() {
        return DataType.DECISION_TREE_VAR;
    }

    @Override
    public CompilationResult compile() {
        String resVarName = NamingManager.genVarName();
        return new CompilationResult(resVarName,
                JenaUtil.genTriple(resVarName, JenaUtil.genLink(POAS_PREF, VAR_PRED), JenaUtil.genStingVal(value)),
                "");
    }
}
