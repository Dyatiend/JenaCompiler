package compiler.values;

import compiler.Value;
import dictionaries.RelationshipsDictionary;
import util.CompilationResult;
import util.DataType;
import util.JenaUtil;

import static util.JenaUtil.POAS_PREF;

public class RelationshipValue extends Value {
    public RelationshipValue(String value) {
        super(value);
        if(!RelationshipsDictionary.exist(value)) throw new IllegalArgumentException("Указанного отношения не существует");
    }

    @Override
    public DataType resultDataType() {
        return DataType.RELATIONSHIP;
    }

    @Override
    public CompilationResult compile() {
        return new CompilationResult(JenaUtil.genRuleLink(POAS_PREF, value), "", "");
    }
}
