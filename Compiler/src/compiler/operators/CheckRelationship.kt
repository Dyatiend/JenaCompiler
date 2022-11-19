package compiler.operators

import compiler.Operator
import compiler.Value
import compiler.values.RelationshipValue
import dictionaries.RelationshipsDictionary
import dictionaries.RelationshipsDictionary.isLinerScaleRelationship
import dictionaries.RelationshipsDictionary.pattern
import dictionaries.RelationshipsDictionary.varCount
import util.CompilationResult
import util.DataType
import util.NamingManager

/**
 * Проверка наличия отношения между объектами
 * TODO?: переходы между классами?
 */
class CheckRelationship(args: List<Operator>) : BaseOperator(args) {

    init {
        val arg0 = arg(0) as RelationshipValue
        val arg1 = arg(1)
        require(!(
                arg1.resultDataType() == DataType.Relationship
                        && !isLinerScaleRelationship(arg0.value)
        )) { "Отношение ${arg0.value} не вычисляется на основе другого отношения" }
    }

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(
            listOf(DataType.Relationship, DataType.Relationship, DataType.Object),
            listOf(DataType.Relationship, DataType.Object)
        )
    }

    override val isArgsCountUnlimited: Boolean
        get() = true

    override fun resultDataType(): DataType {
        return DataType.Boolean
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val heads = ArrayList<String>()
        var completedRules = ""
        val completedRulesBuilder = StringBuilder()

        // Компилируем аргументы
        val compiledArgs = ArrayList<CompilationResult>()
        val argValues = ArrayList<String>()

        for (arg in args()) {
            val res = arg.compile()
            compiledArgs.add(res)
            argValues.add(res.value)
            completedRulesBuilder.append(res.completedRules)
        }

        completedRules += completedRulesBuilder

        // Если отношение вычисляется через другое отношение
        if (arg(1).resultDataType() == DataType.Relationship) {
            // Проверяем кол-во аргументов
            val relValue = arg(0) as RelationshipValue
            require(RelationshipsDictionary.args(relValue.value)!!.size == args().size - 2) {
                "Некорректное количество аргументов"
            }

            // Получаем имя отношения
            val mainRelName = (arg(0) as Value).value
            val supportRelName = (arg(1) as Value).value

            // Для всех результатов компиляции
            val indices = ArrayList<Int>() // Индексы
            compiledArgs.forEach { _ ->
                indices.add(0)
            }

            // Пока не дошли до первого иднекса
            while (indices.first() != compiledArgs.first().ruleHeads.size) {
                // Собираем все части
                var head = ""
                for (i in compiledArgs.indices) {
                    head += compiledArgs[i].ruleHeads[indices[i]]
                }

                // Добавляем проверку отношения
                var pattern = pattern(mainRelName, supportRelName)!!.first
                // Заполняем аргументы
                for(i in RelationshipsDictionary.args(mainRelName)!!.indices) {
                    pattern = pattern.replace("<arg$i>", argValues[i + 2])
                }
                // Заполняем переменные
                for(i in 0 until varCount(mainRelName)!!) {
                    pattern = pattern.replace("<var$i>", NamingManager.genVarName())
                }

                // Добавляем шаблон
                head += pattern

                // Добавляем в рзультат
                heads.add(head)

                // Меняем индексы
                var i = indices.size - 1
                while (true) {
                    if (indices[i] != compiledArgs[i].ruleHeads.size - 1 || i == 0) {
                        ++indices[i]
                        break
                    } else {
                        indices[i] = 0
                    }

                    --i
                }
            }

            // Сохраняем вспомогательные правила
            completedRules += pattern(mainRelName)!!.second
        } else {
            // Проверяем кол-во аргументов
            val relValue = arg(0) as RelationshipValue
            require(RelationshipsDictionary.args(relValue.value)!!.size == args().size - 1) {
                "Некорректное количество аргументов"
            }

            // Получаем имя отношения
            val mainRelName = (arg(0) as Value).value

            // Для всех результатов компиляции
            val indices = ArrayList<Int>() // Индексы
            compiledArgs.forEach { _ ->
                indices.add(0)
            }

            // Пока не дошли до первого иднекса
            while (indices.first() != compiledArgs.first().ruleHeads.size) {
                // Собираем все части
                var head = ""
                for (i in compiledArgs.indices) {
                    head += compiledArgs[i].ruleHeads[indices[i]]
                }

                // Добавляем проверку отношения
                var pattern = pattern(mainRelName)!!.first
                // Заполняем аргументы
                for(i in RelationshipsDictionary.args(mainRelName)!!.indices) {
                    pattern = pattern.replace("<arg$i>", argValues[i + 1])
                }
                // Заполняем переменные
                for(i in 0 until varCount(mainRelName)!!) {
                    pattern = pattern.replace("<var$i>", NamingManager.genVarName())
                }

                // Добавляем шаблон
                head += pattern

                // Добавляем в рзультат
                heads.add(head)

                // Меняем индексы
                var i = indices.size - 1
                while (true) {
                    if (indices[i] != compiledArgs[i].ruleHeads.size - 1 || i == 0) {
                        ++indices[i]
                        break
                    } else {
                        indices[i] = 0
                    }

                    --i
                }
            }

            // Сохраняем вспомогательные правила
            completedRules += pattern(mainRelName)!!.second
        }

        return CompilationResult("", heads, completedRules)
    }
}