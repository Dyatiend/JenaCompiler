import builtins.AbsoluteValue
import builtins.Bind
import builtins.CountValues
import builtins.MakeUniqueID
import builtins.util.registerAllCustomBuiltin
import compiler.Operator
import dictionaries.RelationshipsDictionary
import dictionaries.util.DictionariesUtil.initAllDictionaries
import org.apache.jena.assembler.JA.rules
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.Reasoner
import org.apache.jena.reasoner.rulesys.BuiltinRegistry
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import org.apache.jena.riot.RDFDataMgr
import org.checkerframework.checker.units.qual.m
import util.JenaUtil
import util.NamingManager
import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.util.jar.JarEntry
import javax.swing.text.html.HTML.Tag.P

fun main() {

    registerAllCustomBuiltin()

    initAllDictionaries(
        "testingFiles/dictionaries/classes.csv",
        "testingFiles/dictionaries/decisionTreeVars.csv",
        "testingFiles/dictionaries/enums.csv",
        "testingFiles/dictionaries/properties.csv",
        "testingFiles/dictionaries/relationships.csv"
    )

    val expressions = mapOf(
//        "TEST CHECK GET CLASS" to Operator.fromXMLString("<LogicalNot><CheckClass><DecisionTreeVar name=\"X1\"/><GetClass><DecisionTreeVar name=\"X1\"/></GetClass></CheckClass></LogicalNot>")!!,

//        "count of tokens" to Operator.fromXMLString("<GetPropertyValue><DecisionTreeVar name=\"X\"/><Property name=\"countOfTokens\"/></GetPropertyValue>")!!,
//        "right token" to Operator.fromXMLString("<GetByCondition varName=\"X2\"><LogicalAnd><CheckRelationship><Relationship name=\"has\"/><DecisionTreeVar name=\"X\"/><Variable name=\"X2\"/></CheckRelationship><Compare operator=\"NOT_EQUAL\"><Variable name=\"X2\"/><DecisionTreeVar name=\"X1\"/></Compare></LogicalAnd></GetByCondition>")!!,

        "operator between" to Operator.fromXMLString("<ExistenceQuantifier varName=\"X\"><LogicalAnd><CheckClass><Variable name=\"X\"/><Class name=\"operator\"/></CheckClass><CheckClass><DecisionTreeVar name=\"X2\"/><Class name=\"token\"/></CheckClass></LogicalAnd><ForAllQuantifier varName=\"X_token\"><CheckRelationship><Relationship name=\"has\"/><Variable name=\"X\"/><Variable name=\"X_token\"/></CheckRelationship><LogicalAnd><CheckRelationship><Relationship name=\"rightOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X1\"/></CheckRelationship><CheckRelationship><Relationship name=\"leftOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X2\"/></CheckRelationship></LogicalAnd></ForAllQuantifier></ExistenceQuantifier>")!!,
        "operator between (negative)" to Operator.fromXMLString("<LogicalNot><ExistenceQuantifier varName=\"X\"><CheckClass><Variable name=\"X\"/><Class name=\"operator\"/></CheckClass><ForAllQuantifier varName=\"X_token\"><LogicalAnd><CheckClass><Variable name=\"X\"/><Class name=\"operator\"/></CheckClass><CheckRelationship><Relationship name=\"has\"/><Variable name=\"X\"/><Variable name=\"X_token\"/></CheckRelationship></LogicalAnd><LogicalAnd><CheckRelationship><Relationship name=\"rightOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X1\"/></CheckRelationship><CheckRelationship><Relationship name=\"leftOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X2\"/></CheckRelationship></LogicalAnd></ForAllQuantifier></ExistenceQuantifier></LogicalNot>")!!,
//        "operator between (negative) (посмотреть сам оператор)" to Operator.fromXMLString("<GetByCondition varName=\"X\"><LogicalAnd><CheckClass><Variable name=\"X\"/><Class name=\"operator\"/></CheckClass><ForAllQuantifier varName=\"X_token\"><LogicalAnd><CheckClass><Variable name=\"X\"/><Class name=\"operator\"/></CheckClass><CheckRelationship><Relationship name=\"has\"/><Variable name=\"X\"/><Variable name=\"X_token\"/></CheckRelationship></LogicalAnd><LogicalAnd><CheckRelationship><Relationship name=\"leftOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X2\"/></CheckRelationship><CheckRelationship><Relationship name=\"rightOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X1\"/></CheckRelationship></LogicalAnd></ForAllQuantifier></LogicalAnd></GetByCondition>")!!,
        "find inner operator" to Operator.fromXMLString("<GetByCondition varName=\"X\"><LogicalAnd><LogicalAnd><CheckClass><Variable name=\"X\"/><Class name=\"operator\"/></CheckClass><CheckClass><DecisionTreeVar name=\"X2\"/><Class name=\"token\"/></CheckClass></LogicalAnd><ForAllQuantifier varName=\"X_token\"><CheckRelationship><Relationship name=\"has\"/><Variable name=\"X\"/><Variable name=\"X_token\"/></CheckRelationship><LogicalAnd><CheckRelationship><Relationship name=\"rightOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X1\"/></CheckRelationship><CheckRelationship><Relationship name=\"leftOf\"/><Variable name=\"X_token\"/><DecisionTreeVar name=\"X2\"/></CheckRelationship></LogicalAnd></ForAllQuantifier></LogicalAnd></GetByCondition>")!!,
//        "check parenthesis" to Operator.fromXMLString("<ExistenceQuantifier varName=\"P\"><ExistenceQuantifier varName=\"P1\"><ExistenceQuantifier varName=\"P2\"><LogicalAnd><CheckClass><Variable name=\"P\"/><Class name=\"parenthesis\"/></CheckClass><LogicalAnd><CheckRelationship><Relationship name=\"has\"/><Variable name=\"P\"/><Variable name=\"P1\"/></CheckRelationship><LogicalAnd><CheckRelationship><Relationship name=\"has\"/><Variable name=\"P\"/><Variable name=\"P2\"/></CheckRelationship><LogicalAnd><Compare operator=\"NOT_EQUAL\"><Variable name=\"P1\"/><Variable name=\"P2\"/></Compare><LogicalAnd><CheckRelationship><Relationship name=\"leftOf\"/><Variable name=\"P1\"/><Variable name=\"P2\"/></CheckRelationship><LogicalAnd><ForAllQuantifier varName=\"X_token\"><CheckRelationship><Relationship name=\"has\"/><DecisionTreeVar name=\"X\"/><Variable name=\"X_token\"/></CheckRelationship><CheckRelationship><Relationship name=\"isBetween\"/><Variable name=\"X_token\"/><Variable name=\"P1\"/><Variable name=\"P2\"/></CheckRelationship></ForAllQuantifier><ForAllQuantifier varName=\"Y_token\"><CheckRelationship><Relationship name=\"has\"/><DecisionTreeVar name=\"Y\"/><Variable name=\"Y_token\"/></CheckRelationship><LogicalAnd><CheckRelationship><Relationship name=\"rightOf\"/><Variable name=\"Y_token\"/><Variable name=\"P1\"/></CheckRelationship><CheckRelationship><Relationship name=\"leftOf\"/><Variable name=\"Y_token\"/><Variable name=\"P2\"/></CheckRelationship></LogicalAnd></ForAllQuantifier></LogicalAnd></LogicalAnd></LogicalAnd></LogicalAnd></LogicalAnd></LogicalAnd></ExistenceQuantifier></ExistenceQuantifier></ExistenceQuantifier>")!!,
//        "compare" to Operator.fromXMLString("<Compare><GetPropertyValue><DecisionTreeVar name=\"X\"/><Property name=\"precedence\"/></GetPropertyValue><GetPropertyValue><DecisionTreeVar name=\"Y\"/><Property name=\"precedence\"/></GetPropertyValue></Compare>")!!,
//        "right token" to Operator.fromXMLString("<GetPropertyValue><DecisionTreeVar name=\"X\"/><Property name=\"countOfTokens\"/></GetPropertyValue>")!!,
    )

    val models = mapOf(
        "model 1" to "testingFiles/orderOfEvaluationTestModels/1.owl",
        "model 2" to "testingFiles/orderOfEvaluationTestModels/2.owl",
        "model 3" to "testingFiles/orderOfEvaluationTestModels/3.owl",
        "model 4" to "testingFiles/orderOfEvaluationTestModels/4.owl",
        "model 5" to "testingFiles/orderOfEvaluationTestModels/5.owl",
        "model 6" to "testingFiles/orderOfEvaluationTestModels/6.owl",
        "model 7" to "testingFiles/orderOfEvaluationTestModels/7.owl",
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


//    """
//    exist P {
//        exist P1 {
//            exist P2 {
//                $P is parenthesis and
//                $P->has($P1) and
//                $P->has($P2) and
//                $P1 != $P2 and
//                $P1->leftOf($P2) and
//                forall X_token [
//                    var:X->has($X_token)
//                ] {
//                    $X_token->isBetween($P1, $P2)
//                } and
//                forall Y_token [
//                    var:Y->has($Y_token)
//                ] {
//                    $Y_token->leftOf($P1) or $Y_token->rightOf($P2)
//                }
//            }
//        }
//    }
//    """
    val rsta = """
        
        
        
        [
        (?var1 http://www.w3.org/2000/01/rdf-schema#subClassOf ?var2)
        noValue(?var2, http://www.w3.org/2000/01/rdf-schema#subClassOf, ?var3)
        noValue(?var2, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2...)
        makeUniqueID(?var4)
        ->
        (?var2 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var4)
        ]

        [
        (?var1 http://www.w3.org/2000/01/rdf-schema#subClassOf ?var2)
        noValue(?var1, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2...)
        (?var2 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var3)
        makeUniqueID(?var4)
        strConcat(?var3, ".", ?var4, ?var5)
        ->
        (?var1 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var5)
        ]

        [
        (?var1 http://www.vstu.ru/poas/code#state_next ?var2)
        noValue(?var3, http://www.vstu.ru/poas/code#state_next, ?var1)
        noValue(?var1, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate3...)
        ->
        (?var1 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate3... "1"^^xsd:integer)
        ]

        [
        (?var1 http://www.vstu.ru/poas/code#state_next ?var2)
        noValue(?var2, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate3...)
        (?var1 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate3... ?var3)
        addOne(?var3, ?var4)
        ->
        (?var2 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate3... ?var4)
        ]

        [
        (?var1 http://www.vstu.ru/poas/code#directlyLeftOf ?var2)
        noValue(?var3, http://www.vstu.ru/poas/code#directlyLeftOf, ?var1)
        noValue(?var1, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4...)
        ->
        (?var1 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... "1"^^xsd:integer)
        ]

        [
        (?var1 http://www.vstu.ru/poas/code#directlyLeftOf ?var2)
        noValue(?var2, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4...)
        (?var1 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var3)
        addOne(?var3, ?var4)
        ->
        (?var2 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var4)
        ]
        <pause>[makeSkolem(?tmp) -> (?tmp http://www.vstu.ru/poas/code#predicate1... "true"^^http://www.w3.org/2001/XMLSchema#boolean)]
        [(?a ?p ?b), (?p http://www.w3.org/2000/01/rdf-schema#domain ?c) -> (?a http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?c)]
        [(?a ?p ?b), (?p http://www.w3.org/2000/01/rdf-schema#range ?c) -> (?b http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?c)]
        [(?a ?p ?b), (?p http://www.w3.org/2000/01/rdf-schema#subPropertyOf ?q) -> (?a ?q ?b)]
        [(?a http://www.w3.org/2000/01/rdf-schema#subClassOf ?b), (?b http://www.w3.org/2000/01/rdf-schema#subClassOf ?c) -> (?a http://www.w3.org/2000/01/rdf-schema#subClassOf ?c)]
        [(?a http://www.w3.org/2000/01/rdf-schema#subClassOf ?b), (?c http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?a) -> (?c http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?b)]<pause>[
        (?var4... ?var5... ?var6...)
        (?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.vstu.ru/poas/code#element)
        (?X http://www.vstu.ru/poas/code#state http://www.vstu.ru/poas/code#unevaluated)
        (?X http://www.vstu.ru/poas/code#has ?X_token)
        (?var37... http://www.vstu.ru/poas/code#var... "X1"^^http://www.w3.org/2001/XMLSchema#string)
        (?X_token http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var39...)
        (?var37... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var40...)
        le(?var39..., ?var40...)(?var41... http://www.vstu.ru/poas/code#var... "X2"^^http://www.w3.org/2001/XMLSchema#string)
        (?X_token http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var43...)
        (?var41... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var44...)
        ge(?var43..., ?var44...)makeSkolem(?var61...)
        ->
        (?var61... http://www.vstu.ru/poas/code#predicate8... ?X)
        ]

        [
        (?var4... ?var5... ?var6...)
        (?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.vstu.ru/poas/code#element)
        (?X http://www.vstu.ru/poas/code#state http://www.vstu.ru/poas/code#unevaluated)
        (?X http://www.vstu.ru/poas/code#has ?X_token)
        (?var45... http://www.vstu.ru/poas/code#var... "X1"^^http://www.w3.org/2001/XMLSchema#string)
        (?X_token http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var47...)
        (?var45... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var48...)
        greaterThan(?var47..., ?var48...)(?var49... http://www.vstu.ru/poas/code#var... "X2"^^http://www.w3.org/2001/XMLSchema#string)
        (?X_token http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var51...)
        (?var49... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var52...)
        ge(?var51..., ?var52...)makeSkolem(?var61...)
        ->
        (?var61... http://www.vstu.ru/poas/code#predicate8... ?X_token)
        ]

        [
        (?var4... ?var5... ?var6...)
        (?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.vstu.ru/poas/code#element)
        (?X http://www.vstu.ru/poas/code#state http://www.vstu.ru/poas/code#unevaluated)
        (?X http://www.vstu.ru/poas/code#has ?X_token)
        (?var53... http://www.vstu.ru/poas/code#var... "X1"^^http://www.w3.org/2001/XMLSchema#string)
        (?X_token http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var55...)
        (?var53... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var56...)
        le(?var55..., ?var56...)(?var57... http://www.vstu.ru/poas/code#var... "X2"^^http://www.w3.org/2001/XMLSchema#string)
        (?X_token http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var59...)
        (?var57... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate4... ?var60...)
        lessThan(?var59..., ?var60...)makeSkolem(?var61...)
        ->
       
        (?var61... http://www.vstu.ru/poas/code#predicate8... ?X_token)
        ]

        <pause>[
        (?var4... ?var5... ?var6...)
        (?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.vstu.ru/poas/code#element)
        (?X http://www.vstu.ru/poas/code#state http://www.vstu.ru/poas/code#unevaluated)
        (?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.vstu.ru/poas/code#element)
        (?X http://www.vstu.ru/poas/code#state http://www.vstu.ru/poas/code#unevaluated)
        (?X http://www.vstu.ru/poas/code#has ?X_token)
        noValue(?var61...,http://www.vstu.ru/poas/code#predicate8...)
        makeSkolem(?var62...)
        ->
        (?var62... http://www.vstu.ru/poas/code#predicate9... "true"^^xsd:boolean)
        ]

        <pause>[
        (?var4... ?var5... ?var6...)
        (?X http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.vstu.ru/poas/code#element)
        (?X http://www.vstu.ru/poas/code#state http://www.vstu.ru/poas/code#unevaluated)
        noValue(?var62...,http://www.vstu.ru/poas/code#predicate9...)
        makeSkolem(?var35...)
        ->
        (?var35... http://www.vstu.ru/poas/code#predicate7... "true"^^xsd:boolean)
        ]
        
    """.trimIndent()


//
//
//    println(generateAuxiliaryRules())
//
//    val operator = Operator.fromXMLFile("testingFiles/expr.xml")
//    PropertiesDictionary.init("")
//    RelationshipsDictionary.init("")
//    ClassesDictionary.init("")
//
//    BuiltinRegistry.theRegistry.register(Bind())
//    BuiltinRegistry.theRegistry.register(AbsoluteValue())
//    BuiltinRegistry.theRegistry.register(CountValues())
//    BuiltinRegistry.theRegistry.register(MakeUniqueID())


//    var ruls1 = RelationshipsDictionary.PartialScalePatterns.NUMERATION_RULES_PATTERN
//    ruls1 = ruls1.replace("<partialPredicate>", JenaUtil.genLink(JenaUtil.RDFS_PREF, "subClassOf"))
//    ruls1 = ruls1.replace("<numberPredicate>", JenaUtil.genLink(JenaUtil.POAS_PREF, "num1"))
//
//    var ruls2 = RelationshipsDictionary.PartialScalePatterns.NUMERATION_RULES_PATTERN
//    ruls2 = ruls2.replace("<partialPredicate>", JenaUtil.genLink(JenaUtil.POAS_PREF, "someRel"))
//    ruls2 = ruls2.replace("<numberPredicate>", JenaUtil.genLink(JenaUtil.POAS_PREF, "num2"))
//
//
//    val rsta = """
//
//
//
//    [
//    (?var1 http://www.w3.org/2000/01/rdf-schema#subClassOf ?var2)
//    noValue(?var2, http://www.w3.org/2000/01/rdf-schema#subClassOf, ?var3)
//    noValue(?var2, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2...)
//    makeUniqueID(?var4)
//    ->
//    (?var2 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var4)
//    ]
//
//    [
//    (?var1 http://www.w3.org/2000/01/rdf-schema#subClassOf ?var2)
//    noValue(?var1, http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2...)
//    (?var2 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var3)
//    makeUniqueID(?var4)
//    strConcat(?var3, ".", ?var4, ?var5)
//    ->
//    (?var1 http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var5)
//    ]
//    <pause>[makeSkolem(?tmp) -> (?tmp http://www.vstu.ru/poas/code#predicate1... "true"^^http://www.w3.org/2001/XMLSchema#boolean)]
//    [(?a ?p ?b), (?p http://www.w3.org/2000/01/rdf-schema#domain ?c) -> (?a http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?c)]
//    [(?a ?p ?b), (?p http://www.w3.org/2000/01/rdf-schema#range ?c) -> (?b http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?c)]
//    [(?a ?p ?b), (?p http://www.w3.org/2000/01/rdf-schema#subPropertyOf ?q) -> (?a ?q ?b)]
//    [(?a http://www.w3.org/2000/01/rdf-schema#subClassOf ?b), (?b http://www.w3.org/2000/01/rdf-schema#subClassOf ?c) -> (?a http://www.w3.org/2000/01/rdf-schema#subClassOf ?c)]
//    [(?a http://www.w3.org/2000/01/rdf-schema#subClassOf ?b), (?c http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?a) -> (?c http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?b)]<pause>[
//    (?var1... ?var2... ?var3...)
//    (http://www.vstu.ru/poas/code#object3 http://www.w3.org/1999/02/22-rdf-syntax-ns#type ?var10...)
//    (?var10... http://www.vstu.ru/poas/code#intStatic2 ?var8...)
//    makeSkolem(?var11...)
//    ->
//    (?var11... http://www.vstu.ru/poas/code#predicate4... ?var10...)
//    ]
//
//    [
//    (?var1... ?var2... ?var3...)
//    noValue(?var8...,http://www.vstu.ru/poas/code#predicate5...)
//    (?var9... http://www.vstu.ru/poas/code#predicate4... ?var12...)
//    noValue(?var12...,http://www.vstu.ru/poas/code#predicate6...)
//    makeSkolem(?var11...)
//    ->
//    (?var11... http://www.vstu.ru/poas/code#predicate5... ?var12...)
//    ]
//
//
//    [
//    (?var8... http://www.vstu.ru/poas/code#predicate5... ?var13...)
//    noValue(?var14...,http://www.w3.org/2000/01/rdf-schema#subClassOf)
//    (?var9... http://www.vstu.ru/poas/code#predicate4... ?var15...)
//    (?var13... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var16...)
//    (?var14... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var17...)
//    (?var15... http://www.vstu.ru/poas/code#http://www.vstu.ru/poas/code#predicate2... ?var18...)
//    partialScaleDistance(?var17..., ?var16..., ?var19...)
//    partialScaleDistance(?var17..., ?var18..., ?var20...)
//    lessThan(?var19..., ?var20...)
//    ->
//    drop(0)
//    (?var13... http://www.vstu.ru/poas/code#predicate6... "true"^^http://www.w3.org/2001/XMLSchema#boolean)
//    ]
//    <pause>[
//    (?var4... ?var5... ?var6...)
//    (?var8... http://www.vstu.ru/poas/code#predicate5... ?var10...)
//    (?var10... http://www.vstu.ru/poas/code#intStatic2 "5"^^http://www.w3.org/2001/XMLSchema#integer)
//    makeSkolem(?var7...)
//    ->
//    (?var7... http://www.vstu.ru/poas/code#predicate3... "true"^^xsd:boolean)
//    ]
//
//    """.trimIndent()
//
//    val rlsldasd = """
// (?a ?b ?c) -> print(?a) print(1) print(----)]
// (?a ?b ?c) -> print(?a) print(2) print(----)]
//    """.trimIndent()
//
//    _________________________________________________________________________________________________________________________________________________________________
    var model = ModelFactory.createDefaultModel()
    val input = RDFDataMgr.open("testingFiles/orderOfEvaluationTestModels/5.owl")
    model.read(input, null)

    var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
    val rulesSets = rsta.split(JenaUtil.PAUSE_MARK)
    for (set in rulesSets) {
        val rules = Rule.parseRules(set)
        val reasoner = GenericRuleReasoner(rules)
        inf = ModelFactory.createInfModel(reasoner, inf)
    }

    val tmp = inf.listStatements()
    while (tmp.hasNext()) {
        val next = tmp.next()
        println(next)
    }
// __________________________________________________________________________________________________________________________________________

//    ___________________________________________________________________________________________________________________
//    val rules = Rule.parseRules(rsta)

//
//    val reasoner: Reasoner = GenericRuleReasoner(rules)
//    val inf = ModelFactory.createInfModel(reasoner, model)
//
//    val p = inf.getProperty("http://www.vstu.ru/poas/code#predicate2...")

//    val b1 = BooleanLiteral(true)
//    val b2 = BooleanLiteral(false)
//    val b3 = BooleanLiteral(true)
//    val b4 = BooleanLiteral(true)
//
//    val or1 = LogicalOr(listOf(b1, b2))
//    val or2 = LogicalOr(listOf(b3, b4))
//    val root = LogicalNot(listOf(LogicalAnd(listOf(or1, or2))))
//
//    val result = root.compileExpression()
//
//    val model = ModelFactory.createDefaultModel()
//    val input = RDFDataMgr.open("testingFiles/model.owl")
//    model.read(input, null)
//
//    var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
//    val rulesSets =
//
//    for (set in rulesSets) {
//        val rules = Rule.parseRules(set)
//        val reasoner: Reasoner = GenericRuleReasoner(rules)
//        inf = ModelFactory.createInfModel(reasoner, inf)
//    }
//
//    val tmp = inf.listStatements()
//    while (tmp.hasNext())
//        println(tmp.nextStatement())
//
//    val property = inf.getProperty("http://www.vstu.ru/poas/code#state")
//    println(property)
//
//    val tmp1 = inf.listSubjectsWithProperty(property)
//    while (tmp1.hasNext())
//        println(tmp1.nextResource())
//    println(inf.listObjectsOfProperty(property).hasNext())
//    println(inf.listObjectsOfProperty(property).nextNode())
}