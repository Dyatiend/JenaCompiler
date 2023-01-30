package dictionaries.util

import dictionaries.*
import models.RelationshipModel
import util.JenaUtil.POAS_PREF
import util.JenaUtil.RDFS_PREF
import util.JenaUtil.genLink
import util.NamingManager.genPredicateName

object DictionariesUtil {

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
    val SUBCLASS_SCALE_PREDICATE = genPredicateName()

    /**
     * Инициализирует все словари и проверят их валидность
     */
    fun initAllDictionaries(
        classesDictionaryPath: String,
        decisionTreeVarsDictionaryPath: String,
        enumsDictionaryPath: String,
        propertiesDictionaryPath: String,
        relationshipsDictionaryPath: String
    ) {
        ClassesDictionary.init(classesDictionaryPath)
        DecisionTreeVarsDictionary.init(decisionTreeVarsDictionaryPath)
        EnumsDictionary.init(enumsDictionaryPath)
        PropertiesDictionary.init(propertiesDictionaryPath)
        RelationshipsDictionary.init(relationshipsDictionaryPath)

        ClassesDictionary.validate()
        DecisionTreeVarsDictionary.validate()
        EnumsDictionary.validate()
        PropertiesDictionary.validate()
        RelationshipsDictionary.validate()
    }

    /**
     * Генерирует вспомогательные правила на основе информации из словарей
     */
    fun generateAuxiliaryRules(): String {
        var result = ""

        var classNumerationRules = RelationshipsDictionary.PartialScalePatterns.NUMERATION_RULES_PATTERN

        classNumerationRules = classNumerationRules.replace("<partialPredicate>", genLink(RDFS_PREF, "subClassOf"))
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
                    var numeration = RelationshipsDictionary.PartialScalePatterns.NUMERATION_RULES_PATTERN
                    numeration = numeration.replace("<partialPredicate>", genLink(POAS_PREF, it.name))
                    numeration = numeration.replace(
                        "<numberPredicate>",
                        genLink(POAS_PREF, RelationshipsDictionary.getScalePredicate(it.name)!!)
                    )
                    result += numeration
                }

                else -> {}
            }
        }

        return result
    }
}