/**Модель для добавления пользовательской одежды*/
package ru.dm.android.truestyle.api.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import ru.dm.android.truestyle.api.response.Gender

@Parcelize
data class UserStuff(@SerializedName("productDisplayName") var productDisplayName: String = "",
                     @SerializedName("gender") var gender: Gender = Gender(),
                     @SerializedName("articleType") var articleType: String = "",
                     @SerializedName("baseColor") val baseColor: String = "",
                     @SerializedName("season") var season: String = "",
                     @SerializedName("imageUrl") val imageUrl: String = "",
                     @SerializedName("usage") var usage: String = "",) : Parcelable {
}