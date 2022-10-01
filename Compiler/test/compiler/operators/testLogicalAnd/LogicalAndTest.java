package compiler.operators.testLogicalAnd;

import builtins.AbsoluteValue;
import builtins.Bind;
import compiler.Operator;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.PrintUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.CompilationResult;
import util.JenaUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static util.JenaUtil.*;
import static util.JenaUtil.RDF_PREF_URL;

class LogicalAndTest {

    @BeforeAll
    static void init() {
        // Добавляем префиксы
        PrintUtil.registerPrefix(POAS_PREF, POAS_PREF_URL);
        PrintUtil.registerPrefix(XSD_PREF, XSD_PREF_URL);
        PrintUtil.registerPrefix(RDF_PREF, RDF_PREF_URL);

        // Регистрируем Builtin'ы
        BuiltinRegistry.theRegistry.register(new Bind());
        BuiltinRegistry.theRegistry.register(new AbsoluteValue());
    }

    @Test
    void test_calculate_trueAndTrue() {
        // Читаем выражение
        Operator operator = null;
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalAnd/expressions/test_trueAndTrue.xml");
        }
        catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        assertNotNull(operator);

        // Компилируем
        CompilationResult result = operator.compileExpression();
        assertNotNull(result);
        assertFalse(result.value().isEmpty());
        assertTrue(result.ruleHead().isEmpty());
        assertFalse(result.completedRules().isEmpty());

        // Создаем модель
        Model model = ModelFactory.createDefaultModel();

        // Запускаем ризонер
        List<Rule> rules = Rule.parseRules(result.completedRules());
        Reasoner reasoner = new GenericRuleReasoner(rules);
        InfModel inf = ModelFactory.createInfModel(reasoner, model);

        // Читаем результат
        Property property = inf.getProperty(result.value());
        assertNotNull(property);
        assertTrue(inf.listSubjectsWithProperty(property).hasNext());
    }
}