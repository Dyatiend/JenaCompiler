package compiler.values;

import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

public class StringValue extends Value {
    public StringValue(String value) {
        super(value);
    }

    @Override
    public DataType resultDataType() { return DataType.STRING; }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genStingVal(value), "", "");
    }
}
