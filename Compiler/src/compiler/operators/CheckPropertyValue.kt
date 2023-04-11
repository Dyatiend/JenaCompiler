package compiler.operators

import compiler.Literal
import compiler.Operator
import compiler.literals.EnumLiteral
import compiler.literals.PropertyLiteral
import compiler.util.CompilationResult
import dictionaries.PropertiesDictionary
import dictionaries.PropertiesDictionary.isPropertyBeingOverridden
import dictionaries.RelationshipsDictionary
import dictionaries.util.DictionariesUtil.SUBCLASS_SCALE_PREDICATE
import util.DataType
import util.JenaUtil
import util.JenaUtil.CLASS_PREDICATE_NAME
import util.JenaUtil.RDFS_PREF
import util.JenaUtil.RDF_PREF
import util.JenaUtil.SUBCLASS_PREDICATE_NAME
import util.JenaUtil.genLink
import util.JenaUtil.genNoValuePrim
import util.JenaUtil.genRule
import util.JenaUtil.genTriple
import util.NamingManager.genPredicateName
import util.NamingManager.genVarName

/**
 * Оператор проверки значения свойства объекта
 */
class CheckPropertyValue(args: List<Operator>) : BaseOperator(args) {

    init {
        // TODO: больше проверок
        val propertyName = (arg(1) as Literal).value
        val valueDataType = arg(2).resultDataType!!

        require(PropertiesDictionary.dataType(propertyName) == valueDataType) {
            "Тип данных $valueDataType не соответствует типу ${PropertiesDictionary.dataType(propertyName)} свойства $propertyName."
        }
        require(arg(2) is Literal || arg(2) is GetPropertyValue) {
            "Нельзя сравнивать с динамическим значением." // FIXME?: можно?
        }

        when (val arg2 = arg(2)) {

            is EnumLiteral -> {
                require(PropertiesDictionary.enumName(propertyName)!! == arg2.owner) {
                    "Тип перечисления ${PropertiesDictionary.enumName(propertyName)} свойства $propertyName не соответствует типу перечисления ${arg2.owner} значения."
                }
            }

            is GetPropertyValue -> {
                require(
                    PropertiesDictionary.dataType(propertyName) != DataType.Enum
                            || PropertiesDictionary.enumName(propertyName)!!
                            == PropertiesDictionary.enumName((arg2.arg(1) as PropertyLiteral).value)!!
                ) {
                    "Тип перечисления ${PropertiesDictionary.enumName(propertyName)} свойства $propertyName не соответствует типу перечисления ${
                        PropertiesDictionary.enumName(
                            (arg2.arg(1) as PropertyLiteral).value
                        )
                    } значения."
                }
            }
        }
    }

    /**
     * Является ли оператор негативным (т.е. нужно ли отрицание при компиляции)
     */
    internal var isNegative = false

    override val argsDataTypes
        get() = listOf(
            listOf(DataType.Object, DataType.Property, DataType.Integer),
            listOf(DataType.Object, DataType.Property, DataType.Double),
            listOf(DataType.Object, DataType.Property, DataType.Boolean),
            listOf(DataType.Object, DataType.Property, DataType.String),
            listOf(DataType.Object, DataType.Property, DataType.Enum)
        )

    override val resultDataType
        get() = DataType.Boolean

    override fun compile(): CompilationResult {
        // Объявляем переменные
        val bodies = mutableListOf<String>()
        var completedRules = ""

        // Получаем аргументы
        val arg0 = arg(0)
        val arg1 = arg(1)
        val arg2 = arg(2)

        // Компилируем аргументы
        val compiledArg0 = arg0.compile()
        val compiledArg1 = arg1.compile()
        val compiledArg2 = arg2.compile()

        // Имя свойства
        val propName = (arg1 as Literal).value

        // Если не свойство статическое
        if (PropertiesDictionary.isStatic(propName) == false) {
            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules +
                    compiledArg2.rules

            // Для всех результатов компиляции
            compiledArg0.bodies.forEach { body0 ->
                compiledArg1.bodies.forEach { body1 ->
                    compiledArg2.bodies.forEach { body2 ->
                        var body = body0 + body1 + body2

                        // Добавляем проверку свойства
                        body += if (isNegative) {
                            genNoValuePrim(
                                compiledArg0.value,
                                compiledArg1.value,
                                compiledArg2.value
                            )
                        } else {
                            genTriple(
                                compiledArg0.value,
                                compiledArg1.value,
                                compiledArg2.value
                            )
                        }

                        // Добавляем в массив
                        bodies.add(body)
                    }
                }
            }
        }
        // Проверяем, переопределяется ли свойство
        else if (true || isPropertyBeingOverridden(propName)) {
            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules +
                    compiledArg2.rules

            // Вспомогательные переменные
            val empty0 = genVarName()
            val empty1 = genVarName()

            // Флаг, указывающий на классы объекта с заданным свойством
            val classWithPropFlag = genPredicateName()
            // Переменная с классом
            val classVar = genVarName()
            // Skolem name
            val skolemName = genVarName()

            // Флаг цикла
            val cycleFlag = genPredicateName()
            // Переменная цикла
            val cycleVar = genVarName()


            val dropped = genPredicateName()

            // Переменные аргументов
            val ruleArg1 = genVarName()
            val ruleArg2 = genVarName()
            val ruleArg3 = genVarName()

            // Для всех результатов компиляции
            compiledArg0.bodies.forEach { body0 ->
                compiledArg1.bodies.forEach { body1 ->
                    compiledArg2.bodies.forEach { body2 ->

                        // ---------------- Генерируем правило, помечающее классы объекта --------------

                        var body = body0 + body1

                        // Получаем класс
                        body += genTriple(
                            compiledArg0.value,
                            genLink(RDF_PREF, CLASS_PREDICATE_NAME),
                            classVar
                        )

                        // Добавляем проверку наличия свойства с любым значением
                        body += genTriple(
                            classVar,
                            compiledArg1.value,
                            empty0
                        )

                        // Добавляем в результат
                        completedRules += genRule(body, skolemName, classWithPropFlag, classVar)

                        // ----------- Генерируем правило, помечающее проверяемый класс ------------

                        // Собираем правило, организующее цикл
                        val cycleBody = genNoValuePrim(empty0, cycleFlag) +
                                genTriple(empty1, classWithPropFlag, cycleVar) +
                                genNoValuePrim(cycleVar, dropped)

                        // Добавляем в результат
                        completedRules += genRule(cycleBody, skolemName, cycleFlag, cycleVar)

                        // ------------------ Генерируем правило, проверяющее класс ----------------

                        // Собираем правило, выбирающее ближайший класс

                        // Инициализируем аргумент 1 - элемент цикла
                        var filterBody = genTriple(empty0, cycleFlag, ruleArg1)
                        // Инициализируем аргумент 2 - самый удаленный от объекта класс (типа Object в Java)
                        filterBody += genNoValuePrim(
                            ruleArg2, genLink(
                                RDFS_PREF,
                                SUBCLASS_PREDICATE_NAME
                            )
                        )
                        // Инициализируем аргумент 3 - класс со свойством
                        filterBody += genTriple(empty1, classWithPropFlag, ruleArg3)

                        // Вычисляем самый удаленный класс

                        // Получаем шаблон FIXME?: вынести получение/заполнение шаблона?
                        var patternBody = RelationshipsDictionary.PartialScalePatterns.IS_CLOSER_TO_THAN_PATTERN
                        patternBody = patternBody.replace(
                            "<numberPredicate>",
                            genLink(JenaUtil.POAS_PREF, SUBCLASS_SCALE_PREDICATE)
                        )

                        // Заполняем аргументы
                        patternBody = patternBody.replace("<arg1>", ruleArg1)
                        patternBody = patternBody.replace("<arg2>", ruleArg2)
                        patternBody = patternBody.replace("<arg3>", ruleArg3)

                        // Заполняем временные переменные
                        val varCount = RelationshipsDictionary.PartialScalePatterns.IS_CLOSER_TO_THAN_VAR_COUNT
                        for (i in 1..varCount) {
                            patternBody = patternBody.replace("<var$i>", genVarName())
                        }

                        // Добавляем заполненный шаблон
                        filterBody += patternBody

                        // Генерируем правило
                        var filterRule = FILTER_CLASSES_PATTER
                        filterRule = filterRule.replace("<ruleBody>", filterBody)
                        filterRule = filterRule.replace("<obj>", ruleArg1)
                        filterRule = filterRule.replace("<dropped>", dropped)

                        // Добавляем в основное правило получение найденного класса
                        var mainBody = genTriple(empty0, cycleFlag, classVar) + body2

                        // Проверяем совпадение значения свойства у класса
                        mainBody += if (isNegative) {
                            genNoValuePrim(
                                classVar,
                                compiledArg1.value,
                                compiledArg2.value
                            )
                        } else {
                            genTriple(
                                classVar,
                                compiledArg1.value,
                                compiledArg2.value
                            )
                        }

                        // Добавляем в результат
                        bodies.add(mainBody)
                        completedRules += filterRule
                    }
                }
            }

            // Добавляем паузу
            completedRules += JenaUtil.PAUSE_MARK
        }
        else {
            // Передаем завершенные правила дальше
            completedRules += compiledArg0.rules +
                    compiledArg1.rules +
                    compiledArg2.rules

            // Временная переменная для класса
            val classVar = genVarName()

            // Для всех результатов компиляции
            compiledArg0.bodies.forEach { body0 ->
                compiledArg1.bodies.forEach { body1 ->
                    compiledArg2.bodies.forEach { body2 ->
                        var body = body0 + body1 + body2

                        // Получаем класс
                        body += genTriple(
                            compiledArg0.value,
                            genLink(RDF_PREF, CLASS_PREDICATE_NAME),
                            classVar
                        )

                        // Добавляем проверку свойства
                        body += if (isNegative) {
                            genNoValuePrim(
                                classVar,
                                compiledArg1.value,
                                compiledArg2.value
                            )
                        } else {
                            genTriple(
                                classVar,
                                compiledArg1.value,
                                compiledArg2.value
                            )
                        }

                        // Добавляем в массив
                        bodies.add(body)
                    }
                }
            }
        }

        return CompilationResult(bodies = bodies, rules = completedRules)
    }

    override fun clone(): Operator {
        val newArgs = ArrayList<Operator>()

        args.forEach { arg ->
            newArgs.add(arg.clone())
        }

        return CheckPropertyValue(newArgs)
    }

    override fun clone(newArgs: List<Operator>): Operator {
        return CheckPropertyValue(newArgs)
    }

    companion object {

        /**
         * Шаблон правила фильтрации классов
         */
        private const val FILTER_CLASSES_PATTER =
            "\n[\n<ruleBody>\n->\ndrop(0)\n(<obj> <dropped> \"true\"^^${JenaUtil.XSD_PREF}boolean)\n]\n"
    }
}