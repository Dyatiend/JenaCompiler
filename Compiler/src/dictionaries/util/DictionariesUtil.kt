package dictionaries.util

import dictionaries.*
import models.RelationshipModel
import util.JenaUtil.POAS_PREF
import util.JenaUtil.RDF_PREF
import util.JenaUtil.genLink

/**
 * Разделитель столбцов в CSV файле словаря
 */
internal const val COLUMNS_SEPARATOR = '|'

/**
 * Разделитель элементов списка в ячейке CSV файла словаря
 */
internal const val LIST_ITEMS_SEPARATOR = ';'

/**
 * Предикат, задающий нумерацию для порядковой шкалы классов
 */
const val SUBCLASS_SCALE_PREDICATE = "classNumber..."

/**
 * Инициализирует все словари в правильном порядке
 */
fun initAllDictionaries(
    classesDictionaryPath: String,
    decisionTreeVarsDictionaryPath: String,
    enumsDictionaryPath: String,
    propertiesDictionaryPath: String,
    relationshipsDictionaryPath: String
) {
    ClassesDictionary.init(classesDictionaryPath) // Не зависит от других словарей
    DecisionTreeVarsDictionary.init(decisionTreeVarsDictionaryPath) // Зависит от словаря классов
    EnumsDictionary.init(enumsDictionaryPath) // Не зависит от других словарей
    PropertiesDictionary.init(propertiesDictionaryPath) // Зависит от словаря классов и перечислений
    RelationshipsDictionary.init(relationshipsDictionaryPath) // Зависит от словаря классов

    generateAuxiliaryRules()
}

/**
 * Генерирует вспомогательные правила на основе информации из словарей
 */
fun generateAuxiliaryRules(): String {
    var result = ""

    // FIXME: частичный порядок
    var classNumerationRules = """
        [
        (?var1 <linerPredicate> ?var2)
        noValue(?var2, <linerPredicate>, ?var3)
        ->
        (?var2 <numberPredicate> "1"^^xsd:integer)
        ]
        [
        (?var1 <linerPredicate> ?var2)
        noValue(?var1, <numberPredicate>)
        (?var2 <numberPredicate> ?var3)
        addOne(?var3, ?var4)
        ->
        (?var1 <numberPredicate> ?var4)
        ]
    """
    classNumerationRules = classNumerationRules.replace("<linerPredicate>", genLink(RDF_PREF, "subClassOf"))
    classNumerationRules =
        classNumerationRules.replace("<numberPredicate>", genLink(POAS_PREF, SUBCLASS_SCALE_PREDICATE))
    result += classNumerationRules

    EnumsDictionary.forEach {
        if (it.isLiner) {
            var numeration = RelationshipsDictionary.LinerScalePatterns.NUMERATION_RULES_PATTERN
            numeration = numeration.replace("<linerPredicate>", genLink(POAS_PREF, it.linerPredicate!!))
            numeration = numeration.replace(
                "<numberPredicate>",
                genLink(POAS_PREF, EnumsDictionary.getScalePredicate(it.name)!!)
            )
            result += numeration
        }
    }

    RelationshipsDictionary.forEach {
        when (it.scaleType) {
            RelationshipModel.Companion.ScaleType.Liner -> {
                var numeration = RelationshipsDictionary.LinerScalePatterns.NUMERATION_RULES_PATTERN
                numeration = numeration.replace("<linerPredicate>", genLink(POAS_PREF, it.name))
                numeration = numeration.replace(
                    "<numberPredicate>",
                    genLink(POAS_PREF, RelationshipsDictionary.getScalePredicate(it.name)!!)
                )
                result += numeration
            }

            RelationshipModel.Companion.ScaleType.Partial -> {
                TODO("Правила для нумерации частичных шкал")
            }

            else -> {}
        }
    }

    return result
}