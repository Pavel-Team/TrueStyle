/**Модель для страницы с регистрацией*/
package ru.dm.android.truestyle.model

import ru.dm.android.truestyle.model.networking.Role

data class Registration(var username: String = "",
                        var email: String = "",
                        var password: String = ""/*,
                        val roles: List<Role> = listOf(Role())*/) {
}