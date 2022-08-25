package compiler.values;

import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import static util.JenaUtil.POAS_PREF;

public class ObjectValue extends Value {
    public ObjectValue(String value) {
        super(value);
    }

    @Override
    public DataType resultDataType() {
        return DataType.OBJECT;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genRuleLink(POAS_PREF, value), "", "");
    }
}
