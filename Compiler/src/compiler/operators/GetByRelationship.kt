package compiler.operators

import compiler.Operator
import compiler.Value
import dictionaries.RelationshipsDictionary.args
import dictionaries.RelationshipsDictionary.pattern
import dictionaries.RelationshipsDictionary.varCount
import util.CompilationResult
import util.DataType
import util.JenaUtil
import util.JenaUtil.genCountValuesPrim
import util.JenaUtil.genEqualPrim
import util.JenaUtil.genIntegerVal
import util.JenaUtil.genRule
import util.JenaUtil.genTriple
import util.NamingManager
import util.NamingManager.genVarName

/**
 * Получить объект по отношению
 */
class GetByRelationship(args: List<Operator>) : BaseOperator(args) {

    override fun argsDataTypes(): List<List<DataType>> {
        return listOf(listOf(DataType.Object, DataType.Relationship))
    }

    override fun resultDataType(): DataType {
        return DataType.Object
    }

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val value = genVarName()
        val heads = ArrayList<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)

        // Проверяем бинарность отношения
        val relName = (arg0 as Value).value
        require(args(relName)!!.size == 2) { "Отношение не является бинарным" }

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()

        // Передаем завершенные правила дальше
        completedRules += compiledArg0.completedRules +
                compiledArg1.completedRules + pattern(relName)!!.second

        // Флаг, указывающий на объекты множества
        val flag = NamingManager.genPredicateName()
        // Skolem name
        val skolemName = genVarName()

        // Вспомогательные переменные
        val empty0 = genVarName()
        val empty1 = genVarName()

        // Для всех результатов компиляции
        compiledArg0.ruleHeads.forEach { head0 ->
            compiledArg1.ruleHeads.forEach { head1 ->
                var head = head0 + head1

                // Получаем шаблон отношения и заполняем его
                var patternHead = pattern(relName)!!.first
                patternHead = patternHead.replace("<arg1>", compiledArg0.value)
                patternHead = patternHead.replace("<arg2>", value)

                val varCount = varCount(relName)!!
                for (i in 1..varCount) {
                    patternHead = patternHead.replace("<var$i>", genVarName())
                }

                head += patternHead

                // Собираем правило, помачающее найденные объекты
                val rule = genRule(head, skolemName, flag, value)

                // Добавляем в основное правило
                val mainHead = genTriple(empty0, flag, value) +
                        genCountValuesPrim(empty0, flag, empty1) +
                        genEqualPrim(empty1, genIntegerVal("1"))

                // Добавляем в рзультат
                heads.add(mainHead)
                completedRules += rule
            }
        }

        // Добавляем паузу
        completedRules += JenaUtil.PAUSE_MARK

        return CompilationResult(value, heads, completedRules)
    }
}