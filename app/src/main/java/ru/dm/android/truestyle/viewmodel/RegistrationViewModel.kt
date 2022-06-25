package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.model.Registration
import ru.dm.android.truestyle.model.User
import ru.dm.android.truestyle.repository.RegistrationRepository
import ru.dm.android.truestyle.thread.CheckEmailAsyncTask
import ru.dm.android.truestyle.thread.CheckUsernameAsyncTask
import ru.dm.android.truestyle.thread.RegistrationAsyncTask

private const val TAG = "RegistrationViewModel"

class RegistrationViewModel: ViewModel() {
    var liveData: MutableLiveData<Registration> = MutableLiveData()
    var liveDataSuccessRegistration: MutableLiveData<Auth> = MutableLiveData()
    var liveDataIsCorrectUsername: MutableLiveData<Boolean> = MutableLiveData()
    var liveDataIsCorrectEmail: MutableLiveData<Boolean> = MutableLiveData()
    var liveDataIsCorrectPassword: MutableLiveData<Boolean> = MutableLiveData()

    val registrationRepository = RegistrationRepository

    init {
        liveDataIsCorrectUsername.value = false
        liveDataIsCorrectEmail.value = false
        liveDataIsCorrectPassword.value = false
    }

    private val regexPasswordCorrect = Regex("[_?!a-zA-Z0-9]{6,}")
    private val regexSmallSymbols = Regex("[a-z]+")
    private val regexBigSymbols = Regex("[A-Z]+")
    private val regexDigits = Regex("[0-9]+")
    private val regexOtherSymbols = Regex("[_?!]+")

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


    //Проверка на существование никнейма
    fun checkUserName(username: String) {
        val result = CheckUsernameAsyncTask().execute(username).get()
        liveDataIsCorrectUsername.value = result
    }


    //Проверка на существование email
    fun checkEmail(email: String) : Boolean {
        val result = CheckEmailAsyncTask().execute(email).get()
        liveDataIsCorrectEmail.value = result
        return result
    }


    //Регистрация пользователя
    fun registerUser(username: String, email: String, password: String) {
        //Переделать потом под корутины
        val registrationAuth = RegistrationAsyncTask()
        val response = registrationAuth.execute(username, email, password).get()
        if (response!=null)
            liveDataSuccessRegistration.value = response!!
    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }

}