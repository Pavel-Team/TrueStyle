/**Модель со статьей*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class Article(@SerializedName("id_article") val id: Long = 0,
                   @SerializedName("title") val title: String = "",
                   @SerializedName("description") val description: String = "",
                   @SerializedName("url") val url: String = "",
                   @SerializedName("image_url") val imageUrl: String = "") {
}