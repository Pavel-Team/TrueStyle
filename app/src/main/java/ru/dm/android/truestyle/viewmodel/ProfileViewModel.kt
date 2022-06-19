package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.model.User
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<User> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = User(12,
            "user.name",
            "Городской стиляга",
            181,
            67,
            "Мужской")
    }
}