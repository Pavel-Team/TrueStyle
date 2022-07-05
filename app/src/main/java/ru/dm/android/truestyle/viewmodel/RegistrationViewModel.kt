package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.model.Registration
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.LoginRepository
import ru.dm.android.truestyle.repository.RegistrationRepository
import javax.inject.Inject

private const val TAG = "RegistrationViewModel"

@HiltViewModel
class RegistrationViewModel @Inject constructor(application: Application, val registrationRepository: RegistrationRepository,
                                                val loginRepository: LoginRepository) : AndroidViewModel(application) {
    var liveData: MutableLiveData<Registration> = MutableLiveData()
    var liveDataSuccessRegistration: MutableLiveData<Auth> = MutableLiveData()
    var liveDataIsCorrectUsername: MutableLiveData<Boolean> = MutableLiveData()
    var liveDataIsCorrectEmail: MutableLiveData<Boolean> = MutableLiveData()
    var liveDataIsCorrectPassword: MutableLiveData<Boolean> = MutableLiveData()

    private val regexPasswordCorrect = Regex("[_?!a-zA-Z0-9]{6,}")
    private val regexSmallSymbols = Regex("[a-z]+")
    private val regexBigSymbols = Regex("[A-Z]+")
    private val regexDigits = Regex("[0-9]+")
    private val regexOtherSymbols = Regex("[_?!]+")


    init {
        Log.d(TAG, "init")
        liveDataIsCorrectUsername.value = false
        liveDataIsCorrectEmail.value = false
        liveDataIsCorrectPassword.value = false
    }

    //Функция для проверки надежности пароля (от 0 до 5)
    fun checkStrongPassword(password: String): Int {
        if (regexPasswordCorrect.matches(password)) {
            var strong = 0
            if (regexSmallSymbols.containsMatchIn(password))
                strong+=1
            if (regexBigSymbols.containsMatchIn(password))
                strong+=1
            if (regexDigits.containsMatchIn(password))
                strong+=1
            if (regexOtherSymbols.containsMatchIn(password))
                strong+=1
            if (password.length >= 12)
                strong+=1
            return strong
        } else {
            return 0
        }
    }


    //Метод проверки на существования пользователя с таким же именем
    fun checkUsername(username: String) {
        viewModelScope.launch {
            val isCorrect = registrationRepository.checkUsername(username)
            liveDataIsCorrectUsername.value = isCorrect
            Log.d(TAG, "username = " + liveDataIsCorrectUsername.value.toString())
        }
    }


    //Метод проверки на существования пользователя с таким же email
    fun checkEmail(email: String) {
        viewModelScope.launch {
            liveDataIsCorrectEmail.value = registrationRepository.checkEmail(email)
            Log.d(TAG, "email = " + liveDataIsCorrectEmail.value.toString())
        }
    }


    //Метод для регистрации пользователя
    fun registerUser(username: String, email: String, password: String) {
        Log.d(TAG, "registerUser()")

        var auth: Auth? = null

        viewModelScope.launch {
            val isSuccessful = registrationRepository.registerUser(username, email, password).isSuccess
            if (isSuccessful) {
                auth = loginRepository.networking.api.signIn(
                    LoginRequest(username, password)
                ).body()

                if (auth != null) {
                    ApplicationPreferences.setToken(getApplication<Application>().applicationContext, auth!!.token)
                    liveDataSuccessRegistration.value = auth!!
                }
            }

            Log.d(TAG, auth?.token.toString())
        }
    }

}