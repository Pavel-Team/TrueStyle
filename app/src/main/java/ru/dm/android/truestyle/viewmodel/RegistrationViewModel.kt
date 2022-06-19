package ru.dm.android.truestyle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.Registration

class RegistrationViewModel: ViewModel() {
    var liveData: MutableLiveData<Registration> = MutableLiveData()

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

}