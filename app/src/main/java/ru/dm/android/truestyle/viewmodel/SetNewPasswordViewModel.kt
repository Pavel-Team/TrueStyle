package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.model.NewPassword
import ru.dm.android.truestyle.repository.LoginRepository

private const val TAG = "SetNewPasswordVM"

class SetNewPasswordViewModel : ViewModel() {

    private val loginRepository = LoginRepository

    var liveData: MutableLiveData<NewPassword> = MutableLiveData(NewPassword("", ""))
    var isCorrectToken = false
    var isCorrectNewPassword = false

    var liveDataIsSuccessfulChangePassword: MutableLiveData<Boolean> = MutableLiveData(false)
    var liveDataIsShowPassword: MutableLiveData<Boolean> = MutableLiveData(false)


    //Функция установки нового пароля
    fun setNewPassword(token: String, newPassword: String) {
        viewModelScope.launch {
            liveDataIsSuccessfulChangePassword.postValue(loginRepository.setNewPassword(token, newPassword))
        }
    }
}