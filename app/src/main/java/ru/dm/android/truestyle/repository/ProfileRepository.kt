package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.SettingRequest
import ru.dm.android.truestyle.api.response.User
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ProfileRepository"

@Singleton
class ProfileRepository @Inject constructor(val networking: Networking){

    init {
        Log.d(TAG, "init")
    }


    //Получение основной информации о пользователе (кроме стиля)
    suspend fun getUserInfo(token: String): User? {
        val user = networking.api.getUserInfo(token).body()
        return user
    }


    //Получение стиля пользователя
    suspend fun getUserStyle(token: String): String? {
        val styleUser = networking.api.getUserStyle(token).body()
        return styleUser?.name
    }


    //Установка стиля пользователя
    suspend fun setUserStyle(token: String, id: Long) {
        //...
    }


    //Установка основной информации о пользователе
    //Возвращает Boolean - установились ли значения
    suspend fun setUserInfo(token: String,
                            fullNumber: String,
                            idGender: Long,
                            country: String,
                            photoUrl: String): Boolean {
        val isSuccess = networking.api.setUserInfo(
            token,
            SettingRequest(fullNumber, idGender, country, photoUrl)
        ).isSuccessful
        
        return isSuccess
    }
}