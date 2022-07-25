package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.model.User
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.ProfileRepository
import ru.dm.android.truestyle.util.Constants

private const val TAG = "ProfileViewModel"


class ProfileViewModel  constructor(application: Application): AndroidViewModel(application) {

    private val profileRepository = ProfileRepository

    var liveData: MutableLiveData<User> = MutableLiveData()

    //ВРЕМЕННО
    init {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val userResponse = profileRepository.getUserInfo(token)
            Log.d(TAG, userResponse.toString())
            Log.d(TAG, token)
            val style = profileRepository.getUserStyle(token)

            val res = getApplication<Application>().resources

            liveData.value = User(
                username = userResponse?.username ?: res.getString(R.string.hint_user_name),
                style = style,
                gender =  userResponse?.gender?.name,
                country = userResponse?.country,
                photoUrl = userResponse?.photoUrl ?: ""
            )
        }
    }
}