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
import ru.dm.android.truestyle.repository.RegistrationRepository
import ru.dm.android.truestyle.util.Constants
import java.net.SocketTimeoutException

private const val TAG = "ProfileViewModel"


class ProfileViewModel  constructor(application: Application): AndroidViewModel(application) {

    private val profileRepository = ProfileRepository
    private val registrationRepository = RegistrationRepository

    var liveData: MutableLiveData<User> = MutableLiveData()
    var liveDataIsCorrectUsername: MutableLiveData<Boolean> = MutableLiveData(false)

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
                _username = userResponse?.username ?: res.getString(R.string.hint_user_name),
                _style = style,
                _gender =  userResponse?.gender?.name,
                _country = userResponse?.country,
                _photoUrl = userResponse?.photoUrl ?: ""
            )
        }
    }


    //Проверка - существует ли пользователь с таким же ником
    fun checkUsername(username: String) {
        viewModelScope.launch {
            try {
                val isCorrect = registrationRepository.checkUsername(username)
                liveDataIsCorrectUsername.value = isCorrect
                Log.d(TAG, "username = " + liveDataIsCorrectUsername.value.toString())
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                Log.d("sss", "No internet connection")
            }
        }
    }


    //Установка нового имени для пользователя
    fun setNewUsername(username: String) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            //profileRepository.
        }

        liveData.value?.username = username
    }


    //Установка новой информации о пользователе
    fun setNewUserInfo(country: String, gender: String) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        val idGender: Long = if (gender == Constants.GENDER_MAN) 1L else 2L
        viewModelScope.launch {
            profileRepository.setUserInfo(
                token = token,
                fullNumber = "",
                idGender = idGender,
                country = country,
                photoUrl = ""
            )
        }

        liveData.value?.country = country
        liveData.value?.gender = gender
    }
}