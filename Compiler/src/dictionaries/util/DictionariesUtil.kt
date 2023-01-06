package dictionaries.util

import dictionaries.*

/**
 * Разделитель столбцов в CSV файле словаря
 */
internal const val COLUMNS_SEPARATOR = '|'

/**
 * Разделитель элементов списка в ячейке CSV файла словаря
 */
internal const val LIST_ITEMS_SEPARATOR = ';'

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
}