/**Модель со всей информацией о пользователе (кроме его стиля)*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("username") val username: String? = "",
                @SerializedName("country") val country: String? = "",
                @SerializedName("gender") val gender: Gender? = Gender(),
                @SerializedName("photoUrl") val photoUrl: String? = "") {
}