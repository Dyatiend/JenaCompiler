package models

import dictionaries.ClassesDictionary
import dictionaries.DecisionTreeVarsDictionary

/**
 * Модель переменной дерева рассуждения
 * @param name Имя переменной
 * @param className Имя класса переменной
 */
data class DecisionTreeVarModel(
    val name: String,
    val className: String
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя переменной."
        }
        require(!DecisionTreeVarsDictionary.exist(name)) {
            "Переменная $name уже объявлена в словаре."
        }
        require(ClassesDictionary.exist(className)) {
            "Класс $className не объявлен в словаре."
        }
    }
}