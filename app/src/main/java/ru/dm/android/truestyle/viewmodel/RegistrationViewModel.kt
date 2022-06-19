package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.model.Registration
import ru.dm.android.truestyle.repository.RegistrationRepository
import javax.inject.Inject

private const val TAG = "RegistrationViewModel"

@HiltViewModel
class RegistrationViewModel @Inject constructor(val repository: RegistrationRepository) : ViewModel() {
    var liveData: MutableLiveData<Registration> = MutableLiveData()

    private val regexPasswordCorrect = Regex("[_?!a-zA-Z0-9]{6,}")
    private val regexSmallSymbols = Regex("[a-z]+")
    private val regexBigSymbols = Regex("[A-Z]+")
    private val regexDigits = Regex("[0-9]+")
    private val regexOtherSymbols = Regex("[_?!]+")


    init {
        Log.d(TAG, "init")
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


    //Метод для регистрации пользователя
    fun registerUser(username: String, email: String, password: String) {
        Log.d(TAG, "registerUser()")
        viewModelScope.launch {
            val answer = repository.registerUser(username, email, password)
        }
    }

}