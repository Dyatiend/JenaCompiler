package models

import dictionaries.ClassesDictionary
import dictionaries.DecisionTreeVarsDictionary

/**
 * Модель переменной дерева рассуждения
 * @param name Имя переменной
 * @param classModel Модель класса переменной
 * @see ClassModel
 */
data class DecisionTreeVarModel(
    val name: String,
    val classModel: ClassModel
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя переменной."
        }
        require(!DecisionTreeVarsDictionary.exist(name)) {
            "Переменная $name уже объявлена в словаре."
        }
        require(ClassesDictionary.exist(classModel.name)) {
            "Класс ${classModel.name} не объявлен в словаре."
        }
    }
}