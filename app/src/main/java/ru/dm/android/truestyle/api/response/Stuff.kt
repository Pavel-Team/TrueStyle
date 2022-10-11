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
                 @SerializedName("articleType") var articleType: String = "",
                 @SerializedName("baseColor") val baseColor: String = "",
                 @SerializedName("season") var season: String = "",
                 @SerializedName("imageUrl") val imageUrl: String = "",
                 @SerializedName("storeLink") val storeLink: String? = "") : BaseObservable(), Parcelable {

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
    var categoryStuff: String = articleType
        set(value) {
            field = value
            notifyPropertyChanged(BR.categoryStuff)
        }
}