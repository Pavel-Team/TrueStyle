package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class NewToken(@SerializedName("token") val token: String = "",
                    @SerializedName("username") val username: String = "") {
}