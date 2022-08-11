/**Модель для проверки актуальной версии приложения*/
package ru.dm.android.truestyle.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppVersion(@SerializedName("actualVersionNumber") val actualVersionNumber: String? = "",
                      @SerializedName("minActualVersionNumber") val minActualVersionNumber: String? = "") : Parcelable {
}