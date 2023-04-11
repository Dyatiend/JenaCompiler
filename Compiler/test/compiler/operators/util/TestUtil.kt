package compiler.operators.util

import compiler.Operator
import compiler.util.CompilationResult
import org.apache.jena.rdf.model.InfModel
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import org.apache.jena.riot.RDFDataMgr
import org.junit.jupiter.api.Assertions
import util.JenaUtil

object TestUtil {

    /**
     * Считывает выражение и проверяет корректность считывания
     * @param path Пусть к XML файлу с выражением
     * @return Выражение
     */
    fun readExpr(path: String): Operator {
        var operator: Operator? = null
        try {
            operator =
                Operator.fromXMLFile(path)
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        Assertions.assertNotNull(operator)
        return operator!!
    }

    /**
     * Компилирует выражение и проверяет корректность компиляции
     * @param operator Выражение
     * @return Результат компиляции
     */
    fun compile(operator: Operator): CompilationResult {
        val result = operator.compileExpression()
        Assertions.assertTrue(result.value.isNotBlank())
        Assertions.assertTrue(result.bodies.size == 1)
        Assertions.assertTrue(result.bodies.first().isEmpty())
        Assertions.assertFalse(result.rules.isEmpty())
        return result
    }

    /**
     * Компилирует выражение без возвращаемого значения и проверяет корректность компиляции
     * @param operator Выражение
     * @return Результат компиляции
     */
    fun compileVoid(operator: Operator): CompilationResult {
        val result = operator.compileExpression()
        Assertions.assertTrue(result.value.isBlank())
        Assertions.assertTrue(result.bodies.size == 1)
        Assertions.assertTrue(result.bodies.first().isEmpty())
        Assertions.assertFalse(result.rules.isEmpty())
        return result
    }

    /**
     * Создает модель
     * @param path Путь в RDF файлу
     * @return Модель
     */
    fun createModel(path: String): Model {
        val model = ModelFactory.createDefaultModel()
        val input = RDFDataMgr.open(path)
        model.read(input, null)
        return model
    }

    /**
     * Запускает Jena Reasoner на правилах, полученных в результате компиляции
     * @param model Входная модель
     * @param compilationResult Результат компиляции
     * @return Результирующая модель
     */
    fun runReasoner(model: Model, compilationResult: CompilationResult): InfModel {
        var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
        val rulesSets = compilationResult.rules.split(JenaUtil.PAUSE_MARK)
        for (set in rulesSets) {
            val rules = Rule.parseRules(set)
            val reasoner = GenericRuleReasoner(rules)
            inf = ModelFactory.createInfModel(reasoner, inf)
        }
        return inf
    }
}