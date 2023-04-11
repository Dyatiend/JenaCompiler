package compiler.operators.testCheckPropertyValue

import builtins.util.registerAllCustomBuiltin
import compiler.operators.util.TestUtil
import dictionaries.util.DictionariesUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class CheckPropertyValueTest {

    @ParameterizedTest
    @MethodSource("source")
    fun test(expectedResult: Boolean, exprPath: String) {
        // Читаем выражение
        val operator =
            TestUtil.readExpr(exprPath)

        // Компилируем
        val result = TestUtil.compile(operator)

        // Создаем модель
        val model =
            TestUtil.createModel("test/compiler/operators/testCheckPropertyValue/models/test_checkPropertyValue.owl")

        // Запускаем правила
        val inf = TestUtil.runReasoner(model, result)

        // Проверяем результат

        // Получаем свойство из модели (указатель на результат)
        val prop = inf.getProperty(result.value)

        if (expectedResult) {
            // Проверяем, что флаг результата выставлен и имеет значение true
            val propObjects = inf.listObjectsOfProperty(prop).toList()
            Assertions.assertTrue(propObjects.size == 1)
            Assertions.assertTrue(propObjects.first().asNode().literal.value == true)
        } else {
            // Проверяем, что флаг результата отсутствует
            val propObjects = inf.listObjectsOfProperty(prop).toList()
            Assertions.assertTrue(propObjects.size == 0)
        }
    }

    /*

    object literal
    found object
    decision tree var

    static not overridden
    static overridden
    not static
    not exist

    int
    double
    boolean
    string
    enum

    literal
    getPropValue

    success
    failure

    positive
    negative

     */
    companion object {

        /**
         * Считывает словари и регистрирует кастомные builtin
         */
        @BeforeAll
        @JvmStatic
        fun init() {
            DictionariesUtil.initAllDictionaries(
                "test/compiler/operators/testCheckPropertyValue/dictionaries/classes.csv",
                "test/compiler/operators/testCheckPropertyValue/dictionaries/decisionTreeVars.csv",
                "test/compiler/operators/testCheckPropertyValue/dictionaries/enums.csv",
                "test/compiler/operators/testCheckPropertyValue/dictionaries/properties.csv",
                "test/compiler/operators/testCheckPropertyValue/dictionaries/relationships.csv"
            )

            registerAllCustomBuiltin()
        }

        @JvmStatic
        fun source() = listOf(
            Arguments.of(
                true,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/literal/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_integerLiteral_success.xml"
            ),
            Arguments.of(
                false,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/literal/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_doubleLiteral_failure.xml"
            ),
            Arguments.of(
                true,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/literal/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_booleanLiteral_success_negative.xml"
            ),
            Arguments.of(
                false,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/literal/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_stringLiteral_failure_negative.xml"
            ),

            Arguments.of(
                true,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/getPropertyValue/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_getPropertyValue_success.xml"
            ),
            Arguments.of(
                false,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/getPropertyValue/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_getPropertyValue_failure.xml"
            ),
            Arguments.of(
                true,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/getPropertyValue/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_getPropertyValue_success_negative.xml"
            ),
            Arguments.of(
                false,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticNotOverridden/getPropertyValue/test_checkPropertyValue_static_notOverridden_of_objectLiteral_valueIs_getPropertyValue_failure_negative.xml"
            ),

            Arguments.of(
                true,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/literal/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_integerLiteral_success.xml"
            ),
            Arguments.of(
                false,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/literal/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_enumLiteral_failure.xml"
            ),
            Arguments.of(
                true,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/literal/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_stringLiteral_success_negative.xml"
            ),
            Arguments.of(
                false,
                "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/literal/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_doubleLiteral_failure_negative.xml"
            ),

//            Arguments.of(true, "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/getPropertyValue/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_getPropertyValue_success.xml"),
//            Arguments.of(false, "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/getPropertyValue/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_getPropertyValue_failure.xml"),
//            Arguments.of(true, "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/getPropertyValue/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_getPropertyValue_success_negative.xml"),
//            Arguments.of(false, "test/compiler/operators/testCheckPropertyValue/expressions/objectLiteral/staticOverridden/getPropertyValue/test_checkPropertyValue_static_overridden_of_objectLiteral_valueIs_getPropertyValue_failure_negative.xml"),
        )
    }
}