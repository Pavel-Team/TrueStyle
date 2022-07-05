package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.response.Auth
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LoginRepository"

@Singleton
class LoginRepository @Inject constructor(val networking: Networking){

    init {
        Log.d(TAG, "init")
    }


    //Функция авторизации пользователя
    //Возвращает объект Auth или null
    suspend fun authUser(username: String, password: String): Auth? {
        val auth = networking.api.signIn(
            LoginRequest(username = username, password = password)
        )
        Log.d(TAG, auth.body()?.token.toString())
        return auth.body()
    }
}