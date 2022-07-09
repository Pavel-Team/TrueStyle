/**Модель со статьей*/
package ru.dm.android.truestyle.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(@SerializedName("id_article") val id: Long = 0,
                   @SerializedName("title") val title: String = "",
                   @SerializedName("description") val description: String = "",
                   @SerializedName("url") val url: String = "",
                   @SerializedName("image_url") val imageUrl: String = "") : Parcelable {
}