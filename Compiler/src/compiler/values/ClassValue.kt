package compiler.values;

import compiler.Value;
import dictionaries.ClassesDictionary;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import static util.JenaUtil.POAS_PREF;

public class ClassValue extends Value {

    public ClassValue(String value) {
        super(value);
        if(!ClassesDictionary.exist(value)) throw new IllegalArgumentException("Указанного класса не существует");
    }

    @Override
    public DataType resultDataType() {
        return DataType.CLASS;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genLink(POAS_PREF, value), "", "");
    }
}
