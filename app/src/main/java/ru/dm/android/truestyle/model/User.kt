/**Модель пользователя*/
package ru.dm.android.truestyle.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import ru.dm.android.truestyle.BR
import ru.dm.android.truestyle.api.response.StyleUser

@Parcelize
data class User(private var _username: String?,
                private var _style: StyleUser?,
                private var _gender: String?,
                private var _country: String?,
                private var _photoUrl: String?) : BaseObservable(), Parcelable {

    @get:Bindable
    var username: String? = _username
        set(value) {
            field = value
            notifyPropertyChanged(BR.username)
        }

    @get:Bindable
    var style: StyleUser? = _style
        set(value) {
            field = value
            notifyPropertyChanged(BR.style)
        }

    @get:Bindable
    var gender: String? = _gender
        set(value) {
            field = value
            notifyPropertyChanged(BR.gender)
        }

    @get:Bindable
    var country: String? = _country
        set(value) {
            field = value
            notifyPropertyChanged(BR.country)
        }

    @get:Bindable
    var photoUrl: String? = _photoUrl
        set(value) {
            field = value
            notifyPropertyChanged(BR.photoUrl)
        }
}