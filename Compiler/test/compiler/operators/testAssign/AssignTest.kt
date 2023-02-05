package compiler.operators.testAssign

import builtins.util.registerAllCustomBuiltin
import compiler.Operator
import compiler.util.CompilationResult
import dictionaries.util.DictionariesUtil.initAllDictionaries
import org.apache.jena.rdf.model.InfModel
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner
import org.apache.jena.reasoner.rulesys.Rule
import org.apache.jena.riot.RDFDataMgr
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import util.JenaUtil

internal class AssignTest {

    // ++++++++++++++++++ Присваивание переменной дерева мысли +++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Create new var with value of object literal")
    fun test_createNewVar_valueIs_objectLiteral() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/decisionTreeVar/test_createNewVar_valueIs_objectLiteral.xml")

        // Компилируем        
        val result = compile(operator)

        // Создаем модель         
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToVar.owl")

        // Запускаем правила        
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        val object3 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object3"))
        // Получаем свойство из модели (указатель переменной)
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, JenaUtil.DECISION_TREE_VAR_PREDICATE))

        // Проверяем что все 3 объекта находятся в переменных
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 3)
        Assertions.assertTrue(propSubjects.contains(object1))
        Assertions.assertTrue(propSubjects.contains(object2))
        Assertions.assertTrue(propSubjects.contains(object3))

        // Проверяем, что объект 1 находится в одной переменной с именем Y
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "Y")

        // Проверяем, что объект 2 находится в одной переменной с именем X
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "X")

        // Проверяем, что объект 3 находится в одной переменной с именем Z
        val object3PropObjects = inf.listObjectsOfProperty(object3, prop).toList()
        Assertions.assertTrue(object3PropObjects.size == 1)
        Assertions.assertTrue(object3PropObjects.first().asNode().literal.value == "Z")
    }

    @Test
    @DisplayName("Create new var with value of found object")
    fun test_createNewVar_valueIs_foundObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/decisionTreeVar/test_createNewVar_valueIs_foundObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель 
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToVar.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        val object3 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object3"))
        // Получаем свойство из модели (указатель переменной)
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, JenaUtil.DECISION_TREE_VAR_PREDICATE))

        // Проверяем что все 3 объекта находятся в переменных
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 3)
        Assertions.assertTrue(propSubjects.contains(object1))
        Assertions.assertTrue(propSubjects.contains(object2))
        Assertions.assertTrue(propSubjects.contains(object3))

        // Проверяем, что объект 1 находится в одной переменной с именем Y
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "Y")

        // Проверяем, что объект 2 находится в одной переменной с именем X
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "X")

        // Проверяем, что объект 3 находится в одной переменной с именем Z
        val object3PropObjects = inf.listObjectsOfProperty(object3, prop).toList()
        Assertions.assertTrue(object3PropObjects.size == 1)
        Assertions.assertTrue(object3PropObjects.first().asNode().literal.value == "Z")
    }

    @Test
    @DisplayName("Create new var with value of other var")
    fun test_createNewVar_valueIs_otherVar() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/decisionTreeVar/test_createNewVar_valueIs_otherVar.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель 
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToVar.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        val object3 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object3"))
        // Получаем свойство из модели (указатель переменной)
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, JenaUtil.DECISION_TREE_VAR_PREDICATE))

        // Проверяем что только объекты 2 и 3 находятся в переменных
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 2)
        Assertions.assertTrue(propSubjects.contains(object2))
        Assertions.assertTrue(propSubjects.contains(object3))

        // Проверяем, что объект 1 не находится ни в одной переменной
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 0)

        // Проверяем, что объект 2 находится в двух переменных с именами X и Y
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 2)
        Assertions.assertTrue(object2PropObjects.any { it.asNode().literal.value == "X" })
        Assertions.assertTrue(object2PropObjects.any { it.asNode().literal.value == "Y" })

        // Проверяем, что объект 3 находится в одной переменной с именем Z
        val object3PropObjects = inf.listObjectsOfProperty(object3, prop).toList()
        Assertions.assertTrue(object3PropObjects.size == 1)
        Assertions.assertTrue(object3PropObjects.first().asNode().literal.value == "Z")
    }

    @Test
    @DisplayName("Rewrite existing var with value of object literal")
    fun test_rewriteExistingVar_valueIs_objectLiteral() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/decisionTreeVar/test_rewriteExistingVar_valueIs_objectLiteral.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель 
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToVar.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        val object3 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object3"))
        // Получаем свойство из модели (указатель переменной)
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, JenaUtil.DECISION_TREE_VAR_PREDICATE))

        // Проверяем что только объекты 1 и 2 находятся в переменных
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 2)
        Assertions.assertTrue(propSubjects.contains(object1))
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что объект 1 находится в одной переменной с именем Z
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "Z")

        // Проверяем, что объект 2 находится в одной переменной с именем X
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "X")

        // Проверяем, что объект 3 не находится ни в одной переменной
        val object3PropObjects = inf.listObjectsOfProperty(object3, prop).toList()
        Assertions.assertTrue(object3PropObjects.size == 0)
    }

    @Test
    @DisplayName("Rewrite existing var with value of found object")
    fun test_rewriteExistingVar_valueIs_foundObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/decisionTreeVar/test_rewriteExistingVar_valueIs_foundObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель 
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToVar.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        val object3 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object3"))
        // Получаем свойство из модели (указатель переменной)
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, JenaUtil.DECISION_TREE_VAR_PREDICATE))

        // Проверяем что только объекты 1 и 2 находятся в переменных
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 2)
        Assertions.assertTrue(propSubjects.contains(object1))
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что объект 1 находится в одной переменной с именем Z
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "Z")

        // Проверяем, что объект 2 находится в одной переменной с именем X
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "X")

        // Проверяем, что объект 3 не находится ни в одной переменной
        val object3PropObjects = inf.listObjectsOfProperty(object3, prop).toList()
        Assertions.assertTrue(object3PropObjects.size == 0)
    }

    @Test
    @DisplayName("Rewrite existing var with value of other var")
    fun test_rewriteExistingVar_valueIs_otherVar() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/decisionTreeVar/test_rewriteExistingVar_valueIs_otherVar.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель 
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToVar.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        val object3 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object3"))
        // Получаем свойство из модели (указатель переменной)
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, JenaUtil.DECISION_TREE_VAR_PREDICATE))

        // Проверяем что только объект 2 находится в переменных
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что объект 1 не находится ни в одной переменной
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 0)

        // Проверяем, что объект 2 находится в двух переменных с именами X и Z
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 2)
        Assertions.assertTrue(object2PropObjects.any { it.asNode().literal.value == "Z" })
        Assertions.assertTrue(object2PropObjects.any { it.asNode().literal.value == "X" })

        // Проверяем, что объект 3 не находится ни в одной переменной
        val object3PropObjects = inf.listObjectsOfProperty(object3, prop).toList()
        Assertions.assertTrue(object3PropObjects.size == 0)
    }

    // ++++++++++++++++++ Присваивание переменной дерева мысли +++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // TODO: тесты на ошибочные ситуации

    // +++++++++++++++++++++++++++++++++ Integer +++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Create new int property with value of literal")
    fun test_createNewIntProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/integer/test_createNewIntProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "int"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == 3)
    }

    @Test
    @DisplayName("Create new int property with value of property of other object")
    fun test_createNewIntProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/integer/test_createNewIntProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "int"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == 1)
    }

    @Test
    @DisplayName("Create new int property with value of static property of other object")
    fun test_createNewIntProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/integer/test_createNewIntProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "int"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == 1)
    }

    @Test
    @DisplayName("Rewrite int property with value of literal")
    fun test_rewriteIntProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/integer/test_rewriteIntProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "int1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == 3)
    }

    @Test
    @DisplayName("Rewrite int property with value of property of other object")
    fun test_rewriteIntProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/integer/test_rewriteIntProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "int1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == 2)
    }

    @Test
    @DisplayName("Rewrite int property with value of static property of other object")
    fun test_rewriteIntProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/integer/test_rewriteIntProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "int1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == 2)
    }

    // ++++++++++++++++++++++++++++++++++ Double +++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Create new double property with value of literal")
    fun test_createNewDoubleProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/double/test_createNewDoubleProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "double"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == 3.3)
    }

    @Test
    @DisplayName("Create new double property with value of property of other object")
    fun test_createNewDoubleProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/double/test_createNewDoubleProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "double"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == 1.1)
    }

    @Test
    @DisplayName("Create new double property with value of static property of other object")
    fun test_createNewDoubleProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/double/test_createNewDoubleProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "double"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == 1.1)
    }

    @Test
    @DisplayName("Rewrite double property with value of literal")
    fun test_rewriteDoubleProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/double/test_rewriteDoubleProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "double1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == 3.3)
    }

    @Test
    @DisplayName("Rewrite double property with value of property of other object")
    fun test_rewriteDoubleProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/double/test_rewriteDoubleProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "double1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == 2.2)
    }

    @Test
    @DisplayName("Rewrite double property with value of static property of other object")
    fun test_rewriteDoubleProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/double/test_rewriteDoubleProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "double1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == 2.2)
    }

    // +++++++++++++++++++++++++++++++++ Boolean +++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Create new boolean property with value of literal")
    fun test_createNewBooleanProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/boolean/test_createNewBooleanProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "boolean"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Create new boolean property with value of property of other object")
    fun test_createNewBooleanProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/boolean/test_createNewBooleanProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "boolean"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Create new boolean property with value of static property of other object")
    fun test_createNewBooleanProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/boolean/test_createNewBooleanProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "boolean"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Rewrite boolean property with value of literal")
    fun test_rewriteBooleanProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/boolean/test_rewriteBooleanProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "boolean1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == false)
    }

    @Test
    @DisplayName("Rewrite boolean property with value of property of other object")
    fun test_rewriteBooleanProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/boolean/test_rewriteBooleanProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "boolean1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == false)
    }

    @Test
    @DisplayName("Rewrite boolean property with value of static property of other object")
    fun test_rewriteBooleanProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/boolean/test_rewriteBooleanProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "boolean1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == false)
    }

    // +++++++++++++++++++++++++++++++++ String ++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Create new string property with value of literal")
    fun test_createNewStringProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/string/test_createNewStringProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "string"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "text_text")
    }

    @Test
    @DisplayName("Create new string property with value of property of other object")
    fun test_createNewStringProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/string/test_createNewStringProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "string"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "text")
    }

    @Test
    @DisplayName("Create new string property with value of static property of other object")
    fun test_createNewStringProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/string/test_createNewStringProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "string"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().literal.value == "text")
    }

    @Test
    @DisplayName("Rewrite string property with value of literal")
    fun test_rewriteStringProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/string/test_rewriteStringProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "string1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "text_text")
    }

    @Test
    @DisplayName("Rewrite string property with value of property of other object")
    fun test_rewriteStringProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/string/test_rewriteStringProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "string1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "TEXT")
    }

    @Test
    @DisplayName("Rewrite string property with value of static property of other object")
    fun test_rewriteStringProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/string/test_rewriteStringProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "string1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().literal.value == "TEXT")
    }

    // ++++++++++++++++++++++++++++++++++ Enum +++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Create new enum property with value of literal")
    fun test_createNewEnumProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/enum/test_createNewEnumProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "enum"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().uri == JenaUtil.genLink(JenaUtil.POAS_PREF, "value1"))
    }

    @Test
    @DisplayName("Create new enum property with value of property of other object")
    fun test_createNewEnumProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/enum/test_createNewEnumProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "enum"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().uri == JenaUtil.genLink(JenaUtil.POAS_PREF, "value1"))
    }

    @Test
    @DisplayName("Create new enum property with value of static property of other object")
    fun test_createNewEnumProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/enum/test_createNewEnumProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object1 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object1"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "enum"))

        // Проверяем что только объект 1 имеет новое свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object1))

        // Проверяем, что новое значение верно присвоено
        val object1PropObjects = inf.listObjectsOfProperty(object1, prop).toList()
        Assertions.assertTrue(object1PropObjects.size == 1)
        Assertions.assertTrue(object1PropObjects.first().asNode().uri == JenaUtil.genLink(JenaUtil.POAS_PREF, "value1"))
    }

    @Test
    @DisplayName("Rewrite enum property with value of literal")
    fun test_rewriteEnumProperty_valueIs_literal() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/enum/test_rewriteEnumProperty_valueIs_literal.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "enum1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().uri == JenaUtil.genLink(JenaUtil.POAS_PREF, "value2"))
    }

    @Test
    @DisplayName("Rewrite enum property with value of property of other object")
    fun test_rewriteEnumProperty_valueIs_propertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/enum/test_rewriteEnumProperty_valueIs_propertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "enum1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().uri == JenaUtil.genLink(JenaUtil.POAS_PREF, "value2"))
    }

    @Test
    @DisplayName("Rewrite enum property with value of static property of other object")
    fun test_rewriteEnumProperty_valueIs_staticPropertyOfOtherObject() {
        // Читаем выражение
        val operator =
            readExpr("test/compiler/operators/testAssign/expressions/enum/test_rewriteEnumProperty_valueIs_staticPropertyOfOtherObject.xml")

        // Компилируем
        val result = compile(operator)

        // Создаем модель
        val model = createModel("test/compiler/operators/testAssign/models/test_assignToProperty.owl")

        // Запускаем правила
        val inf = runReasoner(model, result)

        // Проверяем результат

        // Получаем объекты из модели
        val object2 = inf.getResource(JenaUtil.genLink(JenaUtil.POAS_PREF, "object2"))
        // Получаем свойство из модели
        val prop = inf.getProperty(JenaUtil.genLink(JenaUtil.POAS_PREF, "enum1"))

        // Проверяем что только объект 2 имеет свойство
        val propSubjects = inf.listSubjectsWithProperty(prop).toList()
        Assertions.assertTrue(propSubjects.size == 1)
        Assertions.assertTrue(propSubjects.contains(object2))

        // Проверяем, что новое значение верно присвоено
        val object2PropObjects = inf.listObjectsOfProperty(object2, prop).toList()
        Assertions.assertTrue(object2PropObjects.size == 1)
        Assertions.assertTrue(object2PropObjects.first().asNode().uri == JenaUtil.genLink(JenaUtil.POAS_PREF, "value2"))
    }

    companion object {

        /**
         * Считывает словари и регистрирует кастомные builtin
         */
        @BeforeAll
        @JvmStatic
        fun init() {
            initAllDictionaries(
                "test/compiler/operators/testAssign/dictionaries/classes.csv",
                "test/compiler/operators/testAssign/dictionaries/decisionTreeVars.csv",
                "test/compiler/operators/testAssign/dictionaries/enums.csv",
                "test/compiler/operators/testAssign/dictionaries/properties.csv",
                "test/compiler/operators/testAssign/dictionaries/relationships.csv"
            )

            registerAllCustomBuiltin()
        }

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
            Assertions.assertTrue(result.value.isEmpty())
            Assertions.assertTrue(result.heads.size == 1)
            Assertions.assertTrue(result.heads.first().isEmpty())
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
}