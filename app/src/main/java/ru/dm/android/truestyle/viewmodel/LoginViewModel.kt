package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.Login

class LoginViewModel: ViewModel() {
    var liveData: MutableLiveData<Login> = MutableLiveData()


    //Метод проверки на правильность авторизации
    public fun checkLogin(): Boolean {
        val model = liveData.value
        //ВРЕМЕННО
        return model?.email.equals("pasha@mail.ru") && model?.password.equals("12345")
    }
}