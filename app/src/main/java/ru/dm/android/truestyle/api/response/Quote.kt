/**Модель цитаты*/
package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class Quote(@SerializedName("value") val quote: String,
                 @SerializedName("author") val author: String) {
}