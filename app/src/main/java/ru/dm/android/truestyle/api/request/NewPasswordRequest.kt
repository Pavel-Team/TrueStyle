/**Модель для смены нового пароля*/
package ru.dm.android.truestyle.api.request

import com.google.gson.annotations.SerializedName

data class NewPasswordRequest(@SerializedName("token") val token: String = "",
                              @SerializedName("password") val password: String = "") {
}