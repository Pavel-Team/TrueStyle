package ru.dm.android.truestyle.repository

import android.app.Application
import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.LoginRequest
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
}