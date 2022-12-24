import builtins.AbsoluteValue
import builtins.Bind
import builtins.CountValues
import compiler.literals.BooleanLiteral
import compiler.operators.LogicalAnd
import compiler.operators.LogicalNot
import compiler.operators.LogicalOr
import dictionaries.ClassesDictionary
import dictionaries.PropertiesDictionary
import dictionaries.RelationshipsDictionary
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.Reasoner
import org.apache.jena.reasoner.rulesys.BuiltinRegistry
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import org.apache.jena.riot.RDFDataMgr
import util.JenaUtil

fun main() {
    PropertiesDictionary.init("")
    RelationshipsDictionary.init("")
    ClassesDictionary.init("")

    BuiltinRegistry.theRegistry.register(Bind())
    BuiltinRegistry.theRegistry.register(AbsoluteValue())
    BuiltinRegistry.theRegistry.register(CountValues())

    val b1 = BooleanLiteral(true)
    val b2 = BooleanLiteral(false)
    val b3 = BooleanLiteral(true)
    val b4 = BooleanLiteral(true)

    val or1 = LogicalOr(listOf(b1, b2))
    val or2 = LogicalOr(listOf(b3, b4))
    val root = LogicalNot(listOf(LogicalAnd(listOf(or1, or2))))

    val result = root.compileExpression()

    val model = ModelFactory.createDefaultModel()
    val input = RDFDataMgr.open("in.owl")
    model.read(input, null)

    var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
    val rulesSets = result.rules.split(JenaUtil.PAUSE_MARK)
    for (set in rulesSets) {
        val rules = Rule.parseRules(set)
        val reasoner: Reasoner = GenericRuleReasoner(rules)
        inf = ModelFactory.createInfModel(reasoner, inf)
    }

    val tmp = inf.listStatements()
    while (tmp.hasNext())
        println(tmp.nextStatement())

    val property = inf.getProperty(result.value)
    println(property)
    println(inf.listObjectsOfProperty(property).hasNext())
    println(inf.listObjectsOfProperty(property).nextNode())
}