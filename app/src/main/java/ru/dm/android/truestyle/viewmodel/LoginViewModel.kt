package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.model.Login
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.LoginRepository
import ru.dm.android.truestyle.repository.RegistrationRepository
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(application: Application,
                                         val loginRepository: LoginRepository): AndroidViewModel(application) {
    var liveData: MutableLiveData<Login> = MutableLiveData()
    var liveDataIsSignIn: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Log.d(TAG, "init")
        Log.d(TAG, liveData.toString())
        liveDataIsSignIn.value = false
    }


    //Метод проверки на правильность авторизации (ВРЕМЕННО)
    //В случае успеха - изменяет значение liveDataIsSignIn, а в фрагменте сработает обсервер этой ливДаты
    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            val auth = loginRepository.networking.api.signIn(
                LoginRequest(username, password)
            ).body()

            if (auth != null) {
                ApplicationPreferences.setToken(
                    getApplication<Application>().applicationContext,
                    auth!!.token
                )
                liveDataIsSignIn.value = true
            } else {
                liveDataIsSignIn.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}