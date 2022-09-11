package compiler.values;

import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

public class IntegerValue extends Value {

    public IntegerValue(Integer value) {
        super(Integer.toString(value));
    }

    @Override
    public DataType resultDataType() {
        return DataType.INTEGER;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genIntegerVal(value), "", "");
    }
}
