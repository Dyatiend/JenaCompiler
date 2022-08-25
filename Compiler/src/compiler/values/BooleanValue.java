package compiler.values;

import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

public class BooleanValue extends Value {
    public BooleanValue(Boolean value) {
        super(Boolean.toString(value));
    }

    @Override
    public DataType resultDataType() {
        return DataType.BOOLEAN;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genBooleanVal(value), "", "");
    }
}
