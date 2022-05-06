/**Модель с настройками приложения*/
package ru.dm.android.truestyle.model

data class Settings(var language: String = "",
                    var theme: String = "",
                    var push: String = "") {
}