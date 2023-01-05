package dictionaries.util

import dictionaries.*

/**
 * Разделитель столбцов в CSV файле словаря
 */
const val COL_DELIMITER = '|'

/**
 * Разделитель списков в ячейке CSV файла словаря
 */
const val LIST_DELIMITER = ';'

/**
 * Инициализирует все словари в правильном порядке
 */
fun initAllDictionaries(
    classesDictionaryPath: String,
    enumsDictionaryPath: String,
    decisionTreeVarsDictionaryPath: String,
    propertiesDictionaryPath: String,
    relationshipsDictionaryPath: String
) {
    ClassesDictionary.init(classesDictionaryPath) // Не зависит от других словарей
    EnumsDictionary.init(enumsDictionaryPath) // Не зависит от других словарей
    DecisionTreeVarsDictionary.init(decisionTreeVarsDictionaryPath) // Зависит от словаря классов
    PropertiesDictionary.init(propertiesDictionaryPath) // Зависит от словаря классов и перечислений
    RelationshipsDictionary.init(relationshipsDictionaryPath) // Зависит от словаря классов
}