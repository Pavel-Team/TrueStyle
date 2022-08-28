package ru.dm.android.truestyle.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize
import ru.dm.android.truestyle.BR

@Parcelize
data class NewPassword(private var _token: String?,
                       private var _newPassword: String?) : BaseObservable(), Parcelable {

    @get:Bindable
    var token: String? = _token
        set(value) {
            field = value
            notifyPropertyChanged(BR.token)
        }

    @get:Bindable
    var newPassword: String? = _newPassword
        set(value) {
            field = value
            notifyPropertyChanged(BR.newPassword)
        }
}