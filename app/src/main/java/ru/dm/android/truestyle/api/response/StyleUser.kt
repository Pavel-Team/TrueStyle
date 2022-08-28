/**Модель стиля пользователя (городской стиляга и т.д.)*/
package ru.dm.android.truestyle.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StyleUser(@SerializedName("id") val id: Long,
                     @SerializedName("name") val name: String) : Parcelable {
}