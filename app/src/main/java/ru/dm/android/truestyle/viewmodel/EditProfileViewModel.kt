package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.model.User
import ru.dm.android.truestyle.repository.ProfileRepository
import javax.inject.Inject

private const val TAG = "EditProfileViewModel"

@HiltViewModel
class EditProfileViewModel @Inject constructor(application: Application,
                                               val profileRepository: ProfileRepository) : AndroidViewModel(application) {

    var liveData: MutableLiveData<User> = MutableLiveData()
    var liveDataSuccessEdit: MutableLiveData<Auth> = MutableLiveData()
    var liveDataIsCorrectUsername: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Log.d(TAG, "init")
        liveDataIsCorrectUsername.value = false
    }
}