/**Модель для кастомного EditText в окне с регистрацией*/
package ru.dm.android.truestyle.model

data class ValueEditTextRegistration(val title: String = "",
                                     var value: String = "",
                                     val error: String = "") {
}