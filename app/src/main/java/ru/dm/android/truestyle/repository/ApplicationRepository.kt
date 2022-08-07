package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking

private const val TAG = "ApplicationRepository"

object ApplicationRepository {

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Проверяет, валидный ли токен
    suspend fun checkToken(token: String): Boolean {
        val code = networking.api.getRandomQuote(token).code()
        return code != 401
    }
}