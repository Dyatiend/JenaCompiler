package compiler.operators.testLogicalAnd

import builtins.AbsoluteValue
import builtins.Bind
import builtins.CountValues
import compiler.Operator
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.Reasoner
import org.apache.jena.reasoner.rulesys.BuiltinRegistry
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import util.JenaUtil.PAUSE_MARK

// TODO: чтение словарей
internal class LogicalAndTest {
    
    @Test
    @DisplayName("TRUE and TRUE")
    fun test_calculate_trueAndTrue() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXMLFile("./test/compiler/operators/testLogicalAnd/expressions/test_trueAndTrue.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.bodies.size == 1)
        assertTrue(result.bodies.first().isEmpty())
        assertFalse(result.rules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.rules.split(PAUSE_MARK)
        for (set in rulesSets) {
            val rules = Rule.parseRules(set)
            val reasoner: Reasoner = GenericRuleReasoner(rules)
            inf = ModelFactory.createInfModel(reasoner, inf)
        }

        // Читаем результат
        val property = inf.getProperty(result.value)
        assertNotNull(property)
        assertTrue(inf.listSubjectsWithProperty(property).hasNext())
    }

    @Test
    @DisplayName("TRUE and FALSE")
    fun test_calculate_trueAndFalse() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator =
                Operator.fromXMLFile("./test/compiler/operators/testLogicalAnd/expressions/test_trueAndFalse.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.bodies.size == 1)
        assertTrue(result.bodies.first().isEmpty())
        assertFalse(result.rules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.rules.split(PAUSE_MARK)
        for (set in rulesSets) {
            val rules = Rule.parseRules(set)
            val reasoner: Reasoner = GenericRuleReasoner(rules)
            inf = ModelFactory.createInfModel(reasoner, inf)
        }

        // Читаем результат
        val property = inf.getProperty(result.value)
        assertNotNull(property)
        assertFalse(inf.listSubjectsWithProperty(property).hasNext())
    }

    @Test
    @DisplayName("FALSE and TRUE")
    fun test_calculate_falseAndTrue() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator =
                Operator.fromXMLFile("./test/compiler/operators/testLogicalAnd/expressions/test_falseAndTrue.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.bodies.size == 1)
        assertTrue(result.bodies.first().isEmpty())
        assertFalse(result.rules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.rules.split(PAUSE_MARK)
        for (set in rulesSets) {
            val rules = Rule.parseRules(set)
            val reasoner: Reasoner = GenericRuleReasoner(rules)
            inf = ModelFactory.createInfModel(reasoner, inf)
        }

        // Читаем результат
        val property = inf.getProperty(result.value)
        assertNotNull(property)
        assertFalse(inf.listSubjectsWithProperty(property).hasNext())
    }

    @Test
    @DisplayName("FALSE and FALSE")
    fun test_calculate_falseAndFalse() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator =
                Operator.fromXMLFile("./test/compiler/operators/testLogicalAnd/expressions/test_falseAndFalse.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.bodies.size == 1)
        assertTrue(result.bodies.first().isEmpty())
        assertFalse(result.rules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.rules.split(PAUSE_MARK)
        for (set in rulesSets) {
            val rules = Rule.parseRules(set)
            val reasoner: Reasoner = GenericRuleReasoner(rules)
            inf = ModelFactory.createInfModel(reasoner, inf)
        }

        // Читаем результат
        val property = inf.getProperty(result.value)
        assertNotNull(property)
        assertFalse(inf.listSubjectsWithProperty(property).hasNext())
    }

    @Test
    @DisplayName("Complex logical test")
    fun test_calculate_complex() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator =
                Operator.fromXMLFile("./test/compiler/operators/testLogicalAnd/expressions/test_complexLogical.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.bodies.size == 1)
        assertTrue(result.bodies.first().isEmpty())
        assertFalse(result.rules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.rules.split(PAUSE_MARK)
        for (set in rulesSets) {
            val rules = Rule.parseRules(set)
            val reasoner: Reasoner = GenericRuleReasoner(rules)
            inf = ModelFactory.createInfModel(reasoner, inf)
        }

        // Читаем результат
        val property = inf.getProperty(result.value)
        assertNotNull(property)
        assertTrue(inf.listSubjectsWithProperty(property).hasNext())
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            // Регистрируем Builtin'ы
            BuiltinRegistry.theRegistry.register(Bind())
            BuiltinRegistry.theRegistry.register(AbsoluteValue())
            BuiltinRegistry.theRegistry.register(CountValues())
        }
    }
}