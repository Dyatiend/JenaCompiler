
fun rec(a: List<List<Int>>, i: ArrayList<Int>) {
    var z = 25
    while (i.first() != a.first().size) {
        for (j in a.indices) {
            println(a[j][i[j]])
        }
        println()



        --z

        if(z == 0) break
    }
}

fun main() {
    val arr = listOf(
        listOf(1, 10),
        listOf(2),
        listOf(5, 6, 7, 8),
        listOf(11, 12)
    )

    rec(arr, mutableListOf(0, 0, 0, 0) as ArrayList<Int>)

//    BuiltinRegistry.theRegistry.register(Bind())
//    BuiltinRegistry.theRegistry.register(AbsoluteValue())
//    BuiltinRegistry.theRegistry.register(CountValues())
//
//    val b1 = BooleanValue(true)
//    val b2 = BooleanValue(false)
//    val b3 = BooleanValue(false)
//    val b4 = BooleanValue(true)
//
//    val or1 = LogicalOr(listOf(b1, b2))
//    val or2 = LogicalOr(listOf(b3, b4))
//    val and1 = LogicalAnd(listOf(b2, b3))
//
//    //val result = and1.compileExpression()
//
//    val model = ModelFactory.createDefaultModel()
//
//    // Запускаем ризонер
//    var inf = ModelFactory.createInfModel(GenericRuleReasoner(listOf()), model)
//    val rulesSets = result.completedRules.split(PAUSE_MARK)
//    for (set in rulesSets) {
//        val rules = Rule.parseRules(set)
//        val reasoner: Reasoner = GenericRuleReasoner(rules)
//        inf = ModelFactory.createInfModel(reasoner, inf)
//    }
//
//    // Читаем результат
//
//    // Читаем результат
//    val property = inf.getProperty(result.value)
//    println(property)
//    println(inf.listObjectsOfProperty(property).hasNext())

//
//    val input = RDFDataMgr.open("in.owl")
//    model.read(input, null)
//
//    val rule = """
//        [
//        (?a ?b "2"^^xsd:integer)
//        (?a ?b ?c)
//        makeSkolem(?f)
//        ->
//        (?f ?b ?c)
//        ]
//    """
//
//    val rules = Rule.parseRules(rule)
//    val reasoner = GenericRuleReasoner(rules)
//    val inf = ModelFactory.createInfModel(reasoner, model)
//
//    val i = inf.listStatements()
//    while (i.hasNext())
//        println(i.nextStatement())
}

//
//object Main {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        // Добавляем префиксы
//        PrintUtil.registerPrefix(POAS_PREF, POAS_PREF_URL)
//        PrintUtil.registerPrefix(XSD_PREF, XSD_PREF_URL)
//        PrintUtil.registerPrefix(RDF_PREF, RDF_PREF_URL)
//
//        // Регистрируем Builtin'ы
//        BuiltinRegistry.theRegistry.register(Bind())
//        BuiltinRegistry.theRegistry.register(AbsoluteValue())
//        PropertiesDictionary.init("")
//        RelationshipsDictionary.init("")
//        ClassesDictionary.init("")
//        val a: `val` = ComparisonResult.Greater
//        System.out.println(ComparisonResult.Greater)

//
//        // Читаем выражение
//        Operator operator = null;
//        try {
//            operator = Operator.fromXML("in.xml");
//        }
//        catch (IllegalAccessException ex) {
//            ex.printStackTrace();
//        }
//
//        // Компилируем
//        CompilationResult result = operator.compileExpression();
//        var a = result.completedRules();
//        System.out.println(a);
//
//        // Создаем модель
//        Model model = ModelFactory.createDefaultModel();
//        InputStream in = RDFDataMgr.open("in.owl");
//        model.read(in, null);
//
//        // Запускаем ризонер
//        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
//        String[] rulesSets = a.split(PAUSE_MARK);
//
//        for(String set : rulesSets) {
//            List<Rule> rules = Rule.parseRules(set);
//            Reasoner reasoner = new GenericRuleReasoner(rules);
//            inf = ModelFactory.createInfModel(reasoner, inf);
//        }
//
//        // Читаем результат
//        Property property = inf.getProperty("poas:~2~");
//
//        var iter = inf.listStatements();
//        while(iter.hasNext()) {
//            System.out.println(iter.next());
//        }

//        Operator boolVal1 = new BooleanValue(true);
//        Operator boolVal2 = new BooleanValue(true);
//        Operator boolVal3 = new BooleanValue(true);
//        Operator forAll1 = new ForAllQuantifier(List.of(boolVal1, boolVal2), "A");
//        Operator forAll2 = new ForAllQuantifier(List.of(boolVal3, forAll1), "B");
//        System.out.println(forAll2.compileExpression());


//        try {
//            Operator o = Operator.fromXML("src/test/data/test1.xml");
//        } catch (IllegalAccessException ex) {
//            ex.printStackTrace();
//        }
//
//        // Читаем выражение
//        Operator operator = null;
//        try {
//            operator = Operator.fromXML("./test/compiler/operators/testLogicalNot/expressions/test_notTrue.xml");
//        }
//        catch (IllegalAccessException ex) {
//            ex.printStackTrace();
//        }
//
//        // Компилируем
//        CompilationResult result = operator.compileExpression();
//
//        // Создаем модель
//        Model model = ModelFactory.createDefaultModel();
//
//        // Запускаем ризонер
//        InfModel inf = ModelFactory.createInfModel(new GenericRuleReasoner(List.of()), model);
//        String[] rulesSets = result.completedRules().split(PAUSE_MARK);
//
//        String tmp = "[equal(0,1)makeSkolem(?~0~)->(?~0~ poas:~0~ 1)]";
//
//        List<Rule> rules = Rule.parseRules(tmp);
//        Reasoner reasoner = new GenericRuleReasoner(rules);
//        inf = ModelFactory.createInfModel(reasoner, inf);
//
//        StmtIterator i = inf.listStatements();
//        while(i.hasNext()) {
//            System.out.println(i.next());
//        }
//
//        System.out.println("-----------------");
//
//        tmp = "[(?~1~ ?~2~ ?~3~)makeSkolem(?~4~)->(?~4~ poas:~1~ 1)]";
//
//        List<Rule> rules1 = Rule.parseRules(tmp);
//        Reasoner reasoner1 = new GenericRuleReasoner(rules1);
//        InfModel inf1 = ModelFactory.createInfModel(reasoner1, inf);
//
//         i = inf1.listStatements();
//        while(i.hasNext()) {
//            System.out.println(i.next());
//        }

//        http://www.vstu.ru/poas/code#
//        for(String set : rulesSets) {
//            List<Rule> rules = Rule.parseRules(set);
//            Reasoner reasoner = new GenericRuleReasoner(rules);
//            inf = ModelFactory.createInfModel(reasoner, inf);
//
//            StmtIterator i = inf.listStatements();
//            while(i.hasNext()) {
//                System.out.println(i.next());
//            }
//            System.out.println("-----------------");
//        }

        // Читаем результат
        //Property property = inf.getProperty(result.value());


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
//    }
//}