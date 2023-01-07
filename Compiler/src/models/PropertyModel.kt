package models

import dictionaries.ClassesDictionary
import dictionaries.EnumsDictionary
import dictionaries.PropertiesDictionary
import util.DataType

/**
 * Модель свойства в предметной области
 * @param name Имя свойства
 * @param dataType Тип данных свойства
 * @param enumModel Модель перечисления (только для свойств с типом Enum)
 * @param owners Модели классов, обладающих этим свойством
 * @param valuesRanges Диапазоны возможных значений свойства (только для свойств с типом Integer и Double)
 * @see DataType
 * @see EnumModel
 * @see ClassModel
 */
data class PropertyModel(
    val name: String,
    val dataType: DataType,
    val enumModel: EnumModel?,
    val owners: List<ClassModel>?,
    val valuesRanges: List<Pair<Double, Double>>?
) {

    init {
        require(name.isNotBlank()) {
            "Некорректное имя свойства."
        }
        require(!PropertiesDictionary.exist(name)) {
            "Свойство $name уже объявлено в словаре."
        }
        require(
            dataType == DataType.Integer
                    || dataType == DataType.Double
                    || dataType == DataType.Boolean
                    || dataType == DataType.String
                    || dataType == DataType.Enum
        ) {
            "Некорректный тип свойства $name."
        }
        require(dataType != DataType.Enum || enumModel != null && EnumsDictionary.exist(enumModel.name)) {
            "Некорректное имя перечисления ${enumModel?.name} для свойства $name."
        }
        require(owners == null || owners.isNotEmpty()) {
            "Свойством $name не обладает ни один класс."
        }
        owners?.forEach {
            require(ClassesDictionary.exist(it.name)) {
                "Класс $it не объявлен в словаре."
            }
        }
        require(dataType == DataType.Integer || dataType == DataType.Double || valuesRanges == null) {
            "У свойства $name не может быть диапазонов значений, т.к. оно имеет тип $dataType."
        }
        valuesRanges?.forEach {
            require(it.first <= it.second) {
                "Начальное значение одного из диапазонов свойства $name больше конечного."
            }
        }
    }

    /**
     * Является ли статическим
     */
    val isStatic
        get() = owners != null

    /**
     * Попадает ли значение в один из диапазонов свойства
     * @param value Значение
     */
    fun isValueInRange(value: Int): Boolean {
        if (dataType != DataType.Integer) return false

        valuesRanges?.forEach {
            if (value.toDouble() in it.first..it.second) return true
        }
        return false
    }

    /**
     * Попадает ли значение в один из диапазонов свойства
     * @param value Значение
     */
    fun isValueInRange(value: Double): Boolean {
        if (dataType != DataType.Double) return false

        valuesRanges?.forEach {
            if (value in it.first..it.second) return true
        }
        return false
    }
}