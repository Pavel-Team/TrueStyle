package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.User
import ru.dm.android.truestyle.repository.SQLiteRepository

class ProfileViewModel : ViewModel() {
    val liveDataSqlite: LiveData<List<ru.dm.android.truestyle.database.entity.User>>
    var liveData: MutableLiveData<User> = MutableLiveData()

    val sqLiteRepository = SQLiteRepository.get()

    //ВРЕМЕННО
    init {
        liveDataSqlite = sqLiteRepository.getUsers()
        liveData.value = User(12,
            "user.name",
            "Городской стиляга",
            181,
            67,
            "Мужской")
    }
}