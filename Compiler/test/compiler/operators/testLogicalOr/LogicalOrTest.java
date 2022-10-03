package compiler.operators.testLogicalOr;

import builtins.AbsoluteValue;
import builtins.Bind;
import compiler.Operator;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.PrintUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.CompilationResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static util.JenaUtil.*;

// TODO: чтение словарей
class LogicalOrTest {

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
    @DisplayName("TRUE or TRUE")
    void test_calculate_trueOrTrue() {
        // Читаем выражение
        Operator operator = null;
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalOr/expressions/test_trueOrTrue.xml");
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
        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
        String[] rulesSets = result.completedRules().split(PAUSE_MARK);
        for(String set : rulesSets) {
            List<Rule> rules = Rule.parseRules(set);
            Reasoner reasoner = new GenericRuleReasoner(rules);
            inf = ModelFactory.createInfModel(reasoner, inf);
        }

        // Читаем результат
        Property property = inf.getProperty(result.value());
        assertNotNull(property);
        assertTrue(inf.listSubjectsWithProperty(property).hasNext());
    }

    @Test
    @DisplayName("TRUE or FALSE")
    void test_calculate_trueOrFalse() {
        // Читаем выражение
        Operator operator = null;
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalOr/expressions/test_trueOrFalse.xml");
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
        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
        String[] rulesSets = result.completedRules().split(PAUSE_MARK);
        for(String set : rulesSets) {
            List<Rule> rules = Rule.parseRules(set);
            Reasoner reasoner = new GenericRuleReasoner(rules);
            inf = ModelFactory.createInfModel(reasoner, inf);
        }

        // Читаем результат
        Property property = inf.getProperty(result.value());
        assertNotNull(property);
        assertTrue(inf.listSubjectsWithProperty(property).hasNext());
    }

    @Test
    @DisplayName("FALSE or TRUE")
    void test_calculate_falseOrTrue() {
        // Читаем выражение
        Operator operator = null;
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalOr/expressions/test_falseOrTrue.xml");
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
        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
        String[] rulesSets = result.completedRules().split(PAUSE_MARK);
        for(String set : rulesSets) {
            List<Rule> rules = Rule.parseRules(set);
            Reasoner reasoner = new GenericRuleReasoner(rules);
            inf = ModelFactory.createInfModel(reasoner, inf);
        }

        // Читаем результат
        Property property = inf.getProperty(result.value());
        assertNotNull(property);
        assertTrue(inf.listSubjectsWithProperty(property).hasNext());
    }

    @Test
    @DisplayName("FALSE or FALSE")
    void test_calculate_falseOrFalse() {
        // Читаем выражение
        Operator operator = null;
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalOr/expressions/test_falseOrFalse.xml");
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
        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
        String[] rulesSets = result.completedRules().split(PAUSE_MARK);
        for(String set : rulesSets) {
            List<Rule> rules = Rule.parseRules(set);
            Reasoner reasoner = new GenericRuleReasoner(rules);
            inf = ModelFactory.createInfModel(reasoner, inf);
        }

        // Читаем результат
        Property property = inf.getProperty(result.value());
        assertNotNull(property);
        assertFalse(inf.listSubjectsWithProperty(property).hasNext());
    }

    @Test
    @DisplayName("Complex logical test")
    void test_calculate_complex() {
        // Читаем выражение
        Operator operator = null;
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalOr/expressions/test_complexLogical.xml");
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
        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
        String[] rulesSets = result.completedRules().split(PAUSE_MARK);
        for(String set : rulesSets) {
            List<Rule> rules = Rule.parseRules(set);
            Reasoner reasoner = new GenericRuleReasoner(rules);
            inf = ModelFactory.createInfModel(reasoner, inf);
        }

        // Читаем результат
        Property property = inf.getProperty(result.value());
        assertNotNull(property);
        assertTrue(inf.listSubjectsWithProperty(property).hasNext());
    }
}