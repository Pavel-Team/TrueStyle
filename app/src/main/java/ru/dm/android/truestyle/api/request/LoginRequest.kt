/**Модель, для отправки на регистрацию*/
package ru.dm.android.truestyle.api.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(@SerializedName("username") var username: String,
                        @SerializedName("password") var password: String) {
}