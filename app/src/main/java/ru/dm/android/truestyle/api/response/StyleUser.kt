/**Модель стиля пользователя (городской стиляга и т.д.)*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class StyleUser(@SerializedName("id") val id: Long,
                     @SerializedName("name") val name: String) {
}