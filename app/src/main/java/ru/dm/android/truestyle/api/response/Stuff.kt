package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class Stuff(@SerializedName("id") val id: Long,
                 @SerializedName("productDisplayName") val productDisplayName: String,
                 @SerializedName("gender") val gender: Gender,
                 @SerializedName("masterCategory") val masterCategory: String,
                 @SerializedName("subCategory") val subCategory: String,
                 @SerializedName("articleType") val articleType: String,
                 @SerializedName("baseColor") val baseColor: String,
                 @SerializedName("season") val season: String,
                 @SerializedName("usage") val usage: String,
                 @SerializedName("imageUrl") val imageUrl: String) {
}