package models

import util.DataType

/**
 * Модель свойства в предметной области
 * @param name Имя свойства
 * @param dataType Тип данных свойства
 * @param enumName Имя перечисления (только для свойств с типом Enum)
 * @param owners Имена классов, обладающих этим свойством
 * @param valuesRanges Диапазоны возможных значений свойства (только для свойств с типом Integer и Double)
 * @see DataType
 */
data class PropertyModel(
    val name: String,
    val dataType: DataType,
    val enumName: String?,
    val owners: List<String>?,
    val valuesRanges: List<Pair<Double, Double>>?
)