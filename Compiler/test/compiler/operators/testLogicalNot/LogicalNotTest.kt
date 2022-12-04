package compiler.operators.testLogicalNot

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
class LogicalNotTest {

    @Test
    @DisplayName("Not TRUE")
    fun test_calculate_notTrue() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notTrue.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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
    @DisplayName("Not FALSE")
    fun test_calculate_notFalse() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notFalse.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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
    @DisplayName("Not not TRUE")
    fun test_calculate_notNotTrue() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notNotTrue.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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
    @DisplayName("Not not FALSE")
    fun test_calculate_notNotFalse() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notNotFalse.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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
    @DisplayName("Not not not TRUE")
    fun test_calculate_notNotNotTrue() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notNotNotTrue.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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
    @DisplayName("Not not not FALSE")
    fun test_calculate_notNotNotFalse() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notNotNotFalse.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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
    @DisplayName("Complex logical test")
    fun test_calculate_complex() {
        // Читаем выражение
        var operator: Operator? = null
        try {
            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_complexLogical.xml")
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        assertNotNull(operator)

        // Компилируем
        val result = operator!!.compileExpression()
        assertNotNull(result)
        assertFalse(result.value.isEmpty())
        assertTrue(result.ruleHeads.size == 1)
        assertTrue(result.ruleHeads.first().isEmpty())
        assertFalse(result.completedRules.isEmpty())

        // Создаем модель
        val model = ModelFactory.createDefaultModel()

        // Запускаем ризонер
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = result.completedRules.split(PAUSE_MARK)
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