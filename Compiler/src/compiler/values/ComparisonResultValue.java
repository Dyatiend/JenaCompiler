package compiler.values;

import compiler.Value;
import util.ComparisonResult;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

public class ComparisonResultValue extends Value {
    public ComparisonResultValue(ComparisonResult value) {
        super(ComparisonResult.toString(value));
    }

    @Override
    public DataType resultDataType() {
        return DataType.COMPARISON_RESULT;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genStingVal(value), "", "");
    }
}
