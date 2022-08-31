package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.model.Login
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.LoginRepository
import java.net.SocketTimeoutException

private const val TAG = "LoginViewModel"


class LoginViewModel  constructor(application: Application): AndroidViewModel(application) {

    private val loginRepository = LoginRepository

    var liveData: MutableLiveData<Login> = MutableLiveData(Login())
    var liveDataIsSignIn: MutableLiveData<Boolean> = MutableLiveData(false)
    var liveDataIsShowPassword: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        Log.d(TAG, "init")
        Log.d(TAG, liveData.toString())
    }


    //Метод проверки на правильность авторизации
    //В случае успеха - изменяет значение liveDataIsSignIn, а в фрагменте сработает обсервер этой ливДаты
    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            val auth = loginRepository.authUser(username, password)
            Log.d(TAG, auth.toString())
            Log.d(TAG, (auth==null).toString())
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