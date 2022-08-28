/**Модель для страницы со входом*/
package ru.dm.android.truestyle.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import ru.dm.android.truestyle.BR

data class Login(private var _email: String = "",
                 private var _password: String = "") : BaseObservable() {

    @get:Bindable
    var email: String = _email
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var password: String = _password
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }
}