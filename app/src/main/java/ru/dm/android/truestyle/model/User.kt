/**Модель пользователя*/
package ru.dm.android.truestyle.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var username: String?,
                var style: String?,
                var gender: String?,
                var country: String?,
                val photoUrl: String?) : Parcelable {
}