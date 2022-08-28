package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.repository.LoginRepository

private const val TAG = "PasswordResetViewModel"

class PasswordResetViewModel: ViewModel() {

    var liveData: MutableLiveData<String> = MutableLiveData("") //Поле с введенным email
    var liveDataIsCorrectEmail: MutableLiveData<Boolean> = MutableLiveData(false) //Тот ли email введен пользователем

    private val loginRepository = LoginRepository


    //Метод отправки письма на почту (email) с токеном для сброса пароля
    fun resetPassword(email: String) {
        viewModelScope.launch {
            liveDataIsCorrectEmail.postValue(loginRepository.resetPassword(email))
        }
    }

}