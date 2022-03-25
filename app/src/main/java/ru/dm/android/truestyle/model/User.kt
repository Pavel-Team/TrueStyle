/**Модель пользователя*/
package ru.dm.android.truestyle.model

data class User(val id: Int,
                val name: String,
                val style: String = "Неизвестный стиль",
                private val _height: Int = 0,
                private val _weight: Int = 0,
                val gender: String = "Заполните"){

    var height = ""
    get() {
        if (_height == 0)
            field = "Заполните"
        else
            field = _height.toString() + "см"
        return field
    }

    var weight = ""
    get() {
        if (_weight == 0)
            field = "Заполните"
        else
            field = _weight.toString() + "кг"
        return field
    }
}