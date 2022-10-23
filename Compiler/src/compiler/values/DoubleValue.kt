package compiler.values;

import compiler.Value;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

public class DoubleValue extends Value {

    public DoubleValue(Double value) {
        super(Double.toString(value));
    }

    @Override
    public DataType resultDataType() {
        return DataType.DOUBLE;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genDoubleVal(value), "", "");
    }
}