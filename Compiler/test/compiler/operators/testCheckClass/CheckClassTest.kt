package compiler.operators.testCheckClass

import builtins.util.registerAllCustomBuiltin
import compiler.operators.util.TestUtil
import dictionaries.util.DictionariesUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class CheckClassTest {

    @Test
    @DisplayName("Check immediate class of object literal (success)")
    fun test_checkClass_objectLiteral_immediateClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_immediateClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check immediate class of object literal (failure)")
    fun test_checkClass_objectLiteral_immediateClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_immediateClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check immediate class of object literal (success)")
    fun test_checkClass_objectLiteral_immediateClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_immediateClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check immediate class of object literal (failure)")
    fun test_checkClass_objectLiteral_immediateClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_immediateClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check inherited class of object literal (success)")
    fun test_checkClass_objectLiteral_inheritedClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_inheritedClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check inherited class of object literal (failure)")
    fun test_checkClass_objectLiteral_inheritedClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_inheritedClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check inherited class of object literal (success)")
    fun test_checkClass_objectLiteral_inheritedClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_inheritedClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check inherited class of object literal (failure)")
    fun test_checkClass_objectLiteral_inheritedClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_inheritedClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check calculable class of object literal (success)")
    fun test_checkClass_objectLiteral_calculableClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_calculableClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check calculable class of object literal (failure)")
    fun test_checkClass_objectLiteral_calculableClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_calculableClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check calculable class of object literal (success)")
    fun test_checkClass_objectLiteral_calculableClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_calculableClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check calculable class of object literal (failure)")
    fun test_checkClass_objectLiteral_calculableClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/objectLiteral/test_checkClass_objectLiteral_calculableClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check immediate class of found object (success)")
    fun test_checkClass_foundObject_immediateClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_immediateClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check immediate class of found object (failure)")
    fun test_checkClass_foundObject_immediateClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_immediateClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check immediate class of found object (success)")
    fun test_checkClass_foundObject_immediateClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_immediateClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check immediate class of found object (failure)")
    fun test_checkClass_foundObject_immediateClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_immediateClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check inherited class of found object (success)")
    fun test_checkClass_foundObject_inheritedClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_inheritedClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check inherited class of found object (failure)")
    fun test_checkClass_foundObject_inheritedClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_inheritedClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check inherited class of found object (success)")
    fun test_checkClass_foundObject_inheritedClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_inheritedClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check inherited class of found object (failure)")
    fun test_checkClass_foundObject_inheritedClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_inheritedClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check calculable class of found object (success)")
    fun test_checkClass_foundObject_calculableClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_calculableClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check calculable class of found object (failure)")
    fun test_checkClass_foundObject_calculableClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_calculableClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check calculable class of found object (success)")
    fun test_checkClass_foundObject_calculableClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_calculableClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check calculable class of found object (failure)")
    fun test_checkClass_foundObject_calculableClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/foundObject/test_checkClass_foundObject_calculableClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check immediate class of decision tree var (success)")
    fun test_checkClass_decisionTreeVar_immediateClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_immediateClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check immediate class of decision tree var (failure)")
    fun test_checkClass_decisionTreeVar_immediateClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_immediateClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check immediate class of decision tree var (success)")
    fun test_checkClass_decisionTreeVar_immediateClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_immediateClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check immediate class of decision tree var (failure)")
    fun test_checkClass_decisionTreeVar_immediateClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_immediateClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check inherited class of decision tree var (success)")
    fun test_checkClass_decisionTreeVar_inheritedClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_inheritedClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check inherited class of decision tree var (failure)")
    fun test_checkClass_decisionTreeVar_inheritedClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_inheritedClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check inherited class of decision tree var (success)")
    fun test_checkClass_decisionTreeVar_inheritedClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_inheritedClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check inherited class of decision tree var (failure)")
    fun test_checkClass_decisionTreeVar_inheritedClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_inheritedClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Check calculable class of decision tree var (success)")
    fun test_checkClass_decisionTreeVar_calculableClass_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_calculableClass_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Check calculable class of decision tree var (failure)")
    fun test_checkClass_decisionTreeVar_calculableClass_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_calculableClass_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    @Test
    @DisplayName("Negative check calculable class of decision tree var (success)")
    fun test_checkClass_decisionTreeVar_calculableClass_negative_success() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_calculableClass_negative_success.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата выставлен и имеет значение true
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 1)
        Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
    }

    @Test
    @DisplayName("Negative check calculable class of decision tree var (failure)")
    fun test_checkClass_decisionTreeVar_calculableClass_negative_failure() {
        // Читаем выражение
        val operator =
            TestUtil.readExpr("test/compiler/operators/testCheckClass/expressions/decisionTreeVar/test_checkClass_decisionTreeVar_calculableClass_negative_failure.xml")

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model = TestUtil.createModel("test/compiler/operators/testCheckClass/models/test_checkClass.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        // Проверяем, что флаг результата отсутствует
        val propObjects = inf.listObjectsOfProperty(prop).toList()
        Assertions.assertTrue(propObjects.size == 0)
    }

    companion object {

        /**
         * Считывает словари и регистрирует кастомные builtin
         */
        @BeforeAll
        @JvmStatic
        fun init() {
            DictionariesUtil.initAllDictionaries(
                "test/compiler/operators/testCheckClass/dictionaries/classes.csv",
                "test/compiler/operators/testCheckClass/dictionaries/decisionTreeVars.csv",
                "test/compiler/operators/testCheckClass/dictionaries/enums.csv",
                "test/compiler/operators/testCheckClass/dictionaries/properties.csv",
                "test/compiler/operators/testCheckClass/dictionaries/relationships.csv"
            )

            registerAllCustomBuiltin()
        }
    }
}