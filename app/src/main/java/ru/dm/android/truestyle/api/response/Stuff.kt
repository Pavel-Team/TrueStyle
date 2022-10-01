package ru.dm.android.truestyle.api.response

import android.os.Parcelable
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import ru.dm.android.truestyle.BR

@Parcelize
data class Stuff(@SerializedName("id") val id: Long = 0,
                 @SerializedName("productDisplayName") var productDisplayName: String = "",
                 @SerializedName("gender") var gender: Gender = Gender(),
                 @SerializedName("masterCategory") var masterCategory: String = "",
                 @SerializedName("subCategory") val subCategory: String = "",
                 @SerializedName("articleType") val articleType: String = "",
                 @SerializedName("baseColor") val baseColor: String = "",
                 @SerializedName("season") var season: String = "",
                 @SerializedName("usage") val usage: String = "",
                 @SerializedName("imageUrl") val imageUrl: String = "") : BaseObservable(), Parcelable {

    @IgnoredOnParcel
    @get:Bindable
    var titleStuff: String = productDisplayName
        set(value) {
            field = value
            notifyPropertyChanged(BR.titleStuff)
            Log.d("Stuff", value)
        }

    @IgnoredOnParcel
    @get:Bindable
    var categoryStuff: String = masterCategory
        set(value) {
            field = value
            notifyPropertyChanged(BR.categoryStuff)
        }
}