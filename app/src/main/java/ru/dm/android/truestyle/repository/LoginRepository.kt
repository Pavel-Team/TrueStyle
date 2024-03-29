package ru.dm.android.truestyle.repository

import android.app.Application
import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.request.NewPasswordRequest
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import java.net.SocketTimeoutException

private const val TAG = "LoginRepository"


object LoginRepository {

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Функция входа пользователя
    //Возвращает Auth
    suspend fun signIn(username: String, password: String): Auth? {
        var auth : Auth? = null
        auth = networking.api.signIn(LoginRequest(username, password)).body()
        Log.d(TAG, auth.toString())
        return auth
    }


    //Функция авторизации пользователя
    //Возвращает объект Auth или null
    suspend fun authUser(username: String, password: String): Auth? {
        val auth = networking.api.signIn(
            LoginRequest(username = username, password = password)
        )
        Log.d(TAG, auth.body().toString())
        return auth.body()
    }


    //Функция сброса пароля и отправки нового токена на заданный email
    suspend fun resetPassword(email: String): Boolean {
        val response = networking.api.resetPassword(email)
        return response.code() == 200 && !response.body()!!.equals("Error, Email isn't correct")
    }


    //Функция установки нового пароля
    suspend fun setNewPassword(token: String, newPassword: String): Boolean {
        val response = networking.api.savePassword(NewPasswordRequest(token, newPassword))
        return response.code() == 200 && !response.body()!!.equals("Error, Code isn't correct") && !response.body()!!.equals("Error, User isn't found")
    }
}