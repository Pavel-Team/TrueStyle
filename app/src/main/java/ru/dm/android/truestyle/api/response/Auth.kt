package ru.dm.android.truestyle.api.response

import com.google.gson.annotations.SerializedName

data class Auth(@SerializedName("token") val token: String,
                @SerializedName("type") val type: String,
                @SerializedName("id") val id: Long,
                @SerializedName("username") val username: String,
                @SerializedName("email") val email: String,
                @SerializedName("roles") val roles: List<String>
                ) {
}