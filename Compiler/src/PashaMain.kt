import builtins.util.registerAllCustomBuiltin
import compiler.Operator
import dictionaries.util.DictionariesUtil
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import org.apache.jena.riot.RDFDataMgr
import util.JenaUtil

fun main() {

    registerAllCustomBuiltin()

    DictionariesUtil.initAllDictionaries(
        "pashaTest/dictionaries/classes.csv",
        "pashaTest/dictionaries/vars.csv",
        "pashaTest/dictionaries/enums.csv",
        "pashaTest/dictionaries/properties.csv",
        "pashaTest/dictionaries/relationships.csv"
    )

    val expressions = mapOf(
//        "1" to Operator.fromXMLString("<CheckRelationship><Relationship name=\"can_be_applied_to\"/><GetByCondition varName=\"var1\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var1\"/></CheckRelationship></GetByCondition><DecisionTreeVar name=\"currentItem\"/></CheckRelationship>")!!,
        "1" to Operator.fromXMLString("<GetByCondition varName=\"helper\"><LogicalAnd><CheckRelationship><Relationship name=\"can_be_applied_to\"/><GetByCondition varName=\"var1\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var1\"/></CheckRelationship></GetByCondition><Variable name=\"helper\"/></CheckRelationship><CheckClass><DecisionTreeVar name=\"currentItem\"/><GetClass><Variable name=\"helper\"/></GetClass></CheckClass></LogicalAnd></GetByCondition>")!!,
        "2" to Operator.fromXMLString("<CheckRelationship><Relationship name=\"can_be_used_with\"/><GetByCondition varName=\"var2\"><CheckRelationship><Relationship name=\"operand\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var2\"/></CheckRelationship></GetByCondition><GetByCondition varName=\"var1\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var1\"/></CheckRelationship></GetByCondition></CheckRelationship>")!!,
        "3" to Operator.fromXMLString("<GetByCondition varName=\"helper\"><LogicalAnd><CheckRelationship><Relationship name=\"can_be_used_to_get_access\"/><GetByCondition varName=\"var1\"><CheckRelationship><Relationship name=\"operand\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var1\"/></CheckRelationship></GetByCondition><Variable name=\"helper\"/></CheckRelationship><LogicalAnd><CheckClass><DecisionTreeVar name=\"currentItem\"/><GetClass><Variable name=\"helper\"/></GetClass></CheckClass><ExistenceQuantifier varName=\"var2\"><LogicalAnd><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var2\"/></CheckRelationship><CheckClass><Variable name=\"var2\"/><GetClass><Variable name=\"helper\"/></GetClass></CheckClass></LogicalAnd></ExistenceQuantifier></LogicalAnd></LogicalAnd></GetByCondition>")!!,
        "4" to Operator.fromXMLString("<ExistenceQuantifier varName=\"var1\"><LogicalAnd><CheckRelationship><Relationship name=\"reachable_from\"/><DecisionTreeVar name=\"targetItem\"/><Variable name=\"var1\"/></CheckRelationship><ExistenceQuantifier varName=\"var2\"><LogicalAnd><LogicalAnd><CheckRelationship><Relationship name=\"has\"/><DecisionTreeVar name=\"currentItem\"/><Variable name=\"var2\"/></CheckRelationship><CheckRelationship><Relationship name=\"is_a\"/><Variable name=\"var1\"/><Variable name=\"var2\"/></CheckRelationship></LogicalAnd><CheckClass><Variable name=\"var2\"/><GetClass><GetByCondition varName=\"helper\"><CheckRelationship><Relationship name=\"associated_with\"/><GetByCondition varName=\"var3\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var3\"/></CheckRelationship></GetByCondition><Variable name=\"helper\"/></CheckRelationship></GetByCondition></GetClass></CheckClass></LogicalAnd></ExistenceQuantifier></LogicalAnd></ExistenceQuantifier>")!!,
        "5" to Operator.fromXMLString(
            "<GetByCondition varName=\"var1\"><LogicalAnd><CheckRelationship><Relationship name=\"linked_with\"/><GetByCondition varName=\"var4\"><CheckRelationship><Relationship name=\"operand\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var4\"/></CheckRelationship></GetByCondition><Variable name=\"var1\"/></CheckRelationship><ExistenceQuantifier varName=\"var2\"><LogicalAnd><LogicalAnd><CheckRelationship><Relationship name=\"has\"/><DecisionTreeVar name=\"currentItem\"/><Variable name=\"var2\"/></CheckRelationship><CheckRelationship><Relationship name=\"is_a\"/><Variable name=\"var1\"/><Variable name=\"var2\"/></CheckRelationship></LogicalAnd><CheckClass><Variable name=\"var2\"/><GetClass><GetByCondition varName=\"helper\"><CheckRelationship><Relationship name=\"associated_with\"/><GetByCondition varName=\"var3\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var3\"/></CheckRelationship></GetByCondition><Variable name=\"helper\"/></CheckRelationship></GetByCondition></GetClass></CheckClass></LogicalAnd></ExistenceQuantifier></LogicalAnd></GetByCondition>"
        )!!,
        "6" to Operator.fromXMLString("<CheckRelationship><Relationship name=\"reachable_from\"/><DecisionTreeVar name=\"targetItem\"/><DecisionTreeVar name=\"nextItem\"/></CheckRelationship>")!!,
        "7" to Operator.fromXMLString("<GetPropertyValue><DecisionTreeVar name=\"nextItem\"/><Property name=\"was_visited_before\"/></GetPropertyValue>")!!,
//        "2" to Operator.fromXMLString("<CheckRelationship><Relationship name=\"can_be_used_with\"/><GetByCondition varName=\"var2\"><CheckRelationship><Relationship name=\"operand\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var2\"/></CheckRelationship></GetByCondition><GetByCondition varName=\"var1\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var1\"/></CheckRelationship></GetByCondition></CheckRelationship>")!!,
//        "3" to Operator.fromXMLString("<CheckRelationship><Relationship name=\"can_be_used_to_get_access\"/><GetByCondition varName=\"var1\"><CheckRelationship><Relationship name=\"operand\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var1\"/></CheckRelationship></GetByCondition><GetByCondition varName=\"var2\"><CheckRelationship><Relationship name=\"operator\"/><DecisionTreeVar name=\"symbol\"/><Variable name=\"var2\"/></CheckRelationship></GetByCondition><DecisionTreeVar name=\"currentItem\"/></CheckRelationship>")!!,
//        "1" to Operator.fromXMLString("")!!,
//        "1" to Operator.fromXMLString("")!!,
//        "1" to Operator.fromXMLString("")!!,
//        "1" to Operator.fromXMLString("")!!,
//        "1" to Operator.fromXMLString("")!!,
//        "1" to Operator.fromXMLString("")!!,
    )

    val models = mapOf(
        "model 1" to "pashaTest/1.owl",
//        "model 2" to "testingFiles/orderOfEvaluationTestModels/2.owl",
//        "model 3" to "testingFiles/orderOfEvaluationTestModels/3.owl",
//        "model 4" to "testingFiles/orderOfEvaluationTestModels/4.owl",
//        "model 5" to "testingFiles/orderOfEvaluationTestModels/5.owl",
//        "model 6" to "testingFiles/orderOfEvaluationTestModels/6.owl",
//        "model 7" to "testingFiles/orderOfEvaluationTestModels/7.owl",
    ).map {
        val model = ModelFactory.createDefaultModel()
        val input = RDFDataMgr.open(it.value)
        it.key to model.read(input, null)
    }

    expressions.forEach { (exprLabel, operator) ->
        val result = operator.compileExpression()

        models.forEach { (modelLabel, model) ->
            var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
            val rulesSets = result.rules.split(JenaUtil.PAUSE_MARK)
            for (set in rulesSets) {
                val rules = Rule.parseRules(set)
                val reasoner = GenericRuleReasoner(rules)
                inf = ModelFactory.createInfModel(reasoner, inf)
            }

            val prop = inf.getProperty(result.value)
            val objects = inf.listObjectsOfProperty(prop)

            println(exprLabel)
            println(modelLabel)
            println("Result -> ${if (objects.hasNext()) "success, " + objects.next() else "failure"}")
            println("-------------")

//            val tmp = inf.listStatements()
//            while (tmp.hasNext()) {
//                val next = tmp.next()
//                println(next)
//
//            }
        }
        println("------------------------------------------------------------------------------")
    }

}