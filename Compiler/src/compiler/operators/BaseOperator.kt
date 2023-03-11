package compiler.operators

import compiler.Operator

/**
 * Базовый оператор
 */
abstract class BaseOperator(args: List<Operator>) : Operator {

    /**
     * Аргументы
     */
    final override val args: List<Operator>

    init {
        // Проверяем аргументы перед сохранением
        checkArgs(args)
        this.args = args
    }

    /**
     * Проверка аргументов
     * @param args Аргументы
     */
    private fun checkArgs(args: List<Operator>) {
        // Получаем список типов данных
        val actual = args.map { arg ->
            // Проверяем, что все операторы имеют возвращаемое значение
            requireNotNull(arg.resultDataType) { "Аргумент без возвращаемого значения" }
        }

        val success = argsDataTypes.any { expected ->
            // Если количество аргументов неограниченно
            if (isArgsCountUnlimited) {
                // Проверяем, что кол-во полученных аргументов больше или равно ожидаемым
                if (expected.size <= actual.size) {
                    var equals = true

                    // Сравниваем аргументы
                    actual.forEachIndexed { index, actualType ->
                        // Если дошли до последнего аргумента - сравниваем все полученные с последним ожидаемым
                        val expectedType = if (index > expected.size - 1) {
                            expected.last()
                        } else {
                            expected[index]
                        }

                        // Аргументы совпадают - если типы данных равны или могут быть преобразованы
                        equals = equals && (actualType == expectedType
                                || actualType.canCast(expectedType))
                    }

                    equals
                } else {
                    false
                }
            } else {
                // Проверяем, что кол-во полученных аргументов равно ожидаемым
                if (expected.size == actual.size) {
                    var equals = true

                    // Сравниваем аргументы
                    actual.forEachIndexed { index, actualType ->
                        val expectedType = expected[index]

                        // Аргументы совпадают - если типы данных равны или могут быть преобразованы
                        equals = equals && (actualType == expectedType
                                || actualType.canCast(expectedType))
                    }

                    equals
                } else {
                    false
                }
            }
        }

        // Если аргументы не совпали ни с одним набором - выбрасываем исключение
        require(success) { "Набор аргументов не соответствует ни одной из вариаций оператора" }
    }
}