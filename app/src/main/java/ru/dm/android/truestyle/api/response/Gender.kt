package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class Gender(@SerializedName("id") val id: Long,
                  @SerializedName("name") val name: String) {
}