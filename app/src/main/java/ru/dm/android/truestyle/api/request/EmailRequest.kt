/**Модель для запроса смены пароля с указанием email для получения токена*/
package ru.dm.android.truestyle.api.request

import com.google.gson.annotations.SerializedName

data class EmailRequest(@SerializedName("email") val email: String = "") {
}