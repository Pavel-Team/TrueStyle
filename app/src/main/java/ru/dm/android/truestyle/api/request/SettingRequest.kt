/**Модель для изменения основной информации о пользователе*/
package ru.dm.android.truestyle.api.request

import com.google.gson.annotations.SerializedName

data class SettingRequest(@SerializedName("fullNumber") val fullNumber: String = "",
                          @SerializedName("gender") val idGender: Long = 0,
                          @SerializedName("country") val country: String = "",
                          @SerializedName("photoUrl") val photoUrl: String = "") {
}