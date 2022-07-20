package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.model.User
import ru.dm.android.truestyle.repository.ProfileRepository

private const val TAG = "EditProfileViewModel"


class EditProfileViewModel  constructor(application: Application) : AndroidViewModel(application) {

    private val profileRepository = ProfileRepository

    var liveData: MutableLiveData<User> = MutableLiveData()
    var liveDataSuccessEdit: MutableLiveData<Auth> = MutableLiveData()
    var liveDataIsCorrectUsername: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Log.d(TAG, "init")
        liveDataIsCorrectUsername.value = false
    }
}