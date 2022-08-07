package ru.dm.android.truestyle.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gender(@SerializedName("id") val id: Long = 0,
                  @SerializedName("gender_name") val name: String = "unknown") : Parcelable {
}