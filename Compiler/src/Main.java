import builtins.AbsoluteValue;
import builtins.Bind;
import compiler.Operator;
import compiler.Variable;
import compiler.operators.*;
import compiler.values.*;
import compiler.values.ClassValue;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.util.PrintUtil;
import util.DataType;

import java.util.List;

import static util.JenaUtil.*;

public class Main {
    public static void main(String[] args) {
        // Добавляем префиксы
        PrintUtil.registerPrefix(POAS_PREF, POAS_PREF_URL);
        PrintUtil.registerPrefix(XSD_PREF, XSD_PREF_URL);
        PrintUtil.registerPrefix(RDF_PREF, RDF_PREF_URL);

        // Регистрируем Builtin'ы
        BuiltinRegistry.theRegistry.register(new Bind());
        BuiltinRegistry.theRegistry.register(new AbsoluteValue());




//        Operator boolVal1 = new BooleanValue(true);
//        Operator boolVal2 = new BooleanValue(true);
//        Operator boolVal3 = new BooleanValue(true);
//        Operator forAll1 = new ForAllQuantifier(List.of(boolVal1, boolVal2), "A");
//        Operator forAll2 = new ForAllQuantifier(List.of(boolVal3, forAll1), "B");
//        System.out.println(forAll2.compileExpression());


        Operator o = Operator.fromXML("src/test/data/test1.xml");


//        Operator var = new Variable("A", DataType.OBJECT);
//        Operator X = new DecisionTreeVarValue("X");
//        Operator relVal1 = new RelationshipValue("token_leftOf");
//        Operator checkRel1 = new CheckRelationship(List.of(relVal1, var, X));
//
//        Operator strVal = new StringValue("used");
//        Operator propVal = new PropertyValue("state");
//        Operator checkProp = new CheckPropertyValue(List.of(var, propVal, strVal));
//        Operator not = new LogicalNot(List.of(checkProp));
//
//        Operator classVal = new ClassValue("operand");
//        Operator checkClass = new CheckClass(List.of(var, classVal));
//
//        Operator and1 = new LogicalAnd(List.of(checkRel1, not));
//        Operator and2 = new LogicalAnd(List.of(and1, checkClass));
//
//        Operator extremeVar = new Variable("extremeA", DataType.OBJECT);
//        Operator relVal2 = new RelationshipValue("token_isCloserToThan");
//
//        // Здесь extremeA и A поменялись местами, т.к. идет вычисление "от обратного"
//        // т.е. здесь при вычислении связи будет считаться, что extremeA не экстремальный
//        // TODO: придумать способ определения позиции для экстремума и остальных элементов?
//        Operator checkRel2 = new CheckRelationship(List.of(relVal2, var, X, extremeVar));
//
//        Operator getExtreme = new GetExtreme(List.of(and2, checkRel2), "A", "extremeA");
//
//        System.out.println(getExtreme.compileExpression());





//        Operator classValue = new ClassValue("token");
//        Operator X = new DecisionTreeVarValue("X");
//        Operator operator1 = new CheckClass(List.of(X, classValue));
//
//        System.out.println(operator1.compileExpression());

//        Model model = ModelFactory.createDefaultModel();
//        InputStream in = RDFDataMgr.open("in.owl");
//        model.read(in, null);
//
//        String rule = """
//                [
//                (?x poas:state poas:evaluated)
//                ->
//                (?x poas:asd0 "-1"^^xsd:integer)
//                ]
//                [
//                (?x poas:asd0 ?y)
//                absoluteValue(?y, ?z)
//                ->
//                (?x poas:asd1 ?z)
//                ]
//                """;
//
//        List<Rule> rules = Rule.parseRules(rule);
//        Reasoner reasoner = new GenericRuleReasoner(rules);
//        InfModel inf = ModelFactory.createInfModel(reasoner, model);
//
//        Property p0 = inf.getProperty("poas:asd0");
//        Property p1 = inf.getProperty("poas:asd1");
//        NodeIterator iter = inf.listObjectsOfProperty(p0);
//
//        while(iter.hasNext()) {
//            RDFNode r = iter.nextNode();
//            System.out.println(r);
//        }
//
//        System.out.println("---------");
//
//        iter = inf.listObjectsOfProperty(p1);
//
//        while(iter.hasNext()) {
//            RDFNode r = iter.nextNode();
//            System.out.println(r);
//        }
    }
}
