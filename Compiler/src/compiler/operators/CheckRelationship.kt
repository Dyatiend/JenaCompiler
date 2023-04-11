package compiler.operators

import compiler.Literal
import compiler.Operator
import compiler.literals.RelationshipLiteral
import compiler.util.CompilationResult
import dictionaries.RelationshipsDictionary
import org.apache.jena.vocabulary.SchemaDO.pattern
import util.DataType
import util.NamingManager
import javax.xml.crypto.Data

/**
 * Проверка наличия отношения между объектами
 * TODO?: переходы между классами? relation type
 * TODO: negative form
 */
class CheckRelationship(args: List<Operator>) : BaseOperator(args) {

    /**
     * Является ли оператор негативным (т.е. нужно ли отрицание при компиляции)
     */
    internal var isNegative = false

    override val argsDataTypes
        get() = listOf(
            listOf(DataType.Relationship, DataType.Object)
        )


    override val isArgsCountUnlimited: Boolean
        get() = true

    override val resultDataType: DataType
        get() = DataType.Boolean


    override fun compile(): CompilationResult {
        // Объявляем переменные
        val heads = ArrayList<String>()
        var completedRules = ""
        val completedRulesBuilder = StringBuilder()

        // Компилируем аргументы
        val compiledArgs = ArrayList<CompilationResult>()
        val argValues = ArrayList<String>()

        for (arg in args) {
            val res = arg.compile()
            compiledArgs.add(res)
            argValues.add(res.value)
            completedRulesBuilder.append(res.rules)
        }

        completedRules += completedRulesBuilder

        // Если отношение вычисляется через другое отношение
        if (false) {
//            // Проверяем кол-во аргументов
//            val relValue = arg(0) as RelationshipLiteral
//            require(RelationshipsDictionary.args(relValue.value)!!.size == args.size - 2) {
//                "Некорректное количество аргументов"
//            }
//
//            // Получаем имя отношения
//            val mainRelName = (arg(0) as Literal).value
//            val supportRelName = (arg(1) as Literal).value
//
//            // Для всех результатов компиляции
//            val indices = ArrayList<Int>() // Индексы
//            compiledArgs.forEach { _ ->
//                indices.add(0)
//            }
//
//            // Пока не дошли до первого иднекса
//            while (indices.first() != compiledArgs.first().heads.size) {
//                // Собираем все части
//                var head = ""
//                for (i in compiledArgs.indices) {
//                    head += compiledArgs[i].heads[indices[i]]
//                }
//
//                // Добавляем проверку отношения
//                var pattern = pattern(mainRelName, supportRelName)!!.first
//                // Заполняем аргументы
//                for(i in RelationshipsDictionary.args(mainRelName)!!.indices) {
//                    pattern = pattern.replace("<arg$i>", argValues[i + 2])
//                }
//                // Заполняем переменные
//                for(i in 0 until varCount(mainRelName)!!) {
//                    pattern = pattern.replace("<var$i>", NamingManager.genVarName())
//                }
//
//                // Добавляем шаблон
//                head += pattern
//
//                // Добавляем в рзультат
//                heads.add(head)
//
//                // Меняем индексы
//                var i = indices.size - 1
//                while (true) {
//                    if (indices[i] != compiledArgs[i].heads.size - 1 || i == 0) {
//                        ++indices[i]
//                        break
//                    } else {
//                        indices[i] = 0
//                    }
//
//                    --i
//                }
//            }
//
//            // Сохраняем вспомогательные правила
//            completedRules += pattern(mainRelName)!!.second
        } else {
            // Проверяем кол-во аргументов
            val relValue = arg(0) as RelationshipLiteral
            require(RelationshipsDictionary.args(relValue.value)!!.size == args.size - 1) {
                "Некорректное количество аргументов"
            }

            // Получаем имя отношения
            val mainRelName = (arg(0) as Literal).value

            // Для всех результатов компиляции
            val indices = ArrayList<Int>() // Индексы
            compiledArgs.forEach { _ ->
                indices.add(0)
            }

            // Пока не дошли до первого иднекса
            while (indices.first() != compiledArgs.first().bodies.size) {
                // Собираем все части
                var head = ""
                for (i in compiledArgs.indices) {
                    head += compiledArgs[i].bodies[indices[i]]
                }

                // Добавляем проверку отношения
                var pattern =
                    if (isNegative) RelationshipsDictionary.negativeBody(mainRelName)!! else RelationshipsDictionary.body(
                        mainRelName
                    )!!
                // Заполняем аргументы

                for (i in RelationshipsDictionary.args(mainRelName)!!.indices) {

                    pattern = pattern.replace("<arg${i + 1}>", argValues[i + 1])
                }
                // Заполняем переменные
                for (i in 0..if (isNegative) RelationshipsDictionary.negativeVarsCount(mainRelName)!! else RelationshipsDictionary.varsCount(
                    mainRelName
                )!!) {
                    pattern = pattern.replace("<var$i>", NamingManager.genVarName())
                }

                // Добавляем шаблон
                head += pattern

                // Добавляем в рзультат
                heads.add(head)

                // Меняем индексы
                var i = indices.size - 1
                while (true) {
                    if (indices[i] != compiledArgs[i].bodies.size - 1 || i == 0) {
                        ++indices[i]
                        break
                    } else {
                        indices[i] = 0
                    }

                    --i
                }
            }

            // Сохраняем вспомогательные правила
            if (isNegative)
                RelationshipsDictionary.negativeRules(mainRelName)?.let {
                    completedRules += it
                }
            else
                RelationshipsDictionary.rules(mainRelName)?.let {
                    completedRules += it
                }

        }

        return CompilationResult("", heads, completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        val res = CheckRelationship(newArgs)
        res.isNegative = isNegative
        return res
    }

    override fun clone(newArgs: List<Operator>): Operator {
        val res = CheckRelationship(newArgs)
        res.isNegative = isNegative
        return res
    }
}