/**Модель магазина-партнера*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class Advertisement(@SerializedName("id") val id: Long = 0,
                         @SerializedName("imgUrl") val imgUrl: String = "",
                         @SerializedName("shopUrl") val shopUrl: String = "") {
}