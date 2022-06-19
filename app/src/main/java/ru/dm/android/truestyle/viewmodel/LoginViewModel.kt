package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.Login

private const val TAG = "LoginViewModel"

class LoginViewModel: ViewModel() {
    var liveData: MutableLiveData<Login> = MutableLiveData()

    init {
        Log.d(TAG, "init")
        Log.d(TAG, liveData.toString())
    }


    //Метод проверки на правильность авторизации (ВРЕМЕННО)
    public fun checkLogin(): Boolean {
        val model = liveData.value
        //ВРЕМЕННО
        return model?.email.equals("pasha@mail.ru") && model?.password.equals("12345")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}