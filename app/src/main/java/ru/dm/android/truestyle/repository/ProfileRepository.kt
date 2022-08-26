package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.SettingRequest
import ru.dm.android.truestyle.api.response.NewToken
import ru.dm.android.truestyle.api.response.StyleUser
import ru.dm.android.truestyle.api.response.User

private const val TAG = "ProfileRepository"


object ProfileRepository{

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Получение основной информации о пользователе (кроме стиля)
    suspend fun getUserInfo(token: String): User? {
        val user = networking.api.getUserInfo(token).body()
        return user
    }


    //Получение списка всех стилей
    suspend fun getStyles(token: String): List<StyleUser> {
        val styles = networking.api.getStyles(token).body().orEmpty()
        return styles
    }


    //Получение стиля пользователя
    suspend fun getUserStyle(token: String): StyleUser? {
        val styleUser = networking.api.getUserStyle(token).body()
        return styleUser
    }


    //Установка стиля пользователя
    suspend fun setUserStyle(token: String, id: Long): Boolean {
        val response = networking.api.setUserStyle(token, id).isSuccessful
        return response
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


    //Установка нового имени пользователя
    suspend fun setNewUsername(token: String, username: String): NewToken? {
        val response = networking.api.setUsername(token, username).body()
        return response
    }
}