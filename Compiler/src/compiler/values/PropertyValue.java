package compiler.values;

import compiler.Value;
import dictionaries.PropertiesDictionary;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import static util.JenaUtil.POAS_PREF;

public class PropertyValue extends Value {

    public PropertyValue(String value) {
        super(value);
        if(!PropertiesDictionary.exist(value)) throw new IllegalArgumentException("Указанного свойства не существует");
    }

    @Override
    public DataType resultDataType() {
        return DataType.PROPERTY;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genLink(POAS_PREF, value), "", "");
    }
}
