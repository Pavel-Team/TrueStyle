package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.AppVersion

private const val TAG = "ApplicationRepository"

object ApplicationRepository {

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Получение последней и минимальной актуальной версии приложения
    suspend fun getCurrentAppVersion(): AppVersion? {
        val appVersion = networking.api.getCurrentAppVersion().body()
        return appVersion
    }


    //Проверяет, валидный ли токен
    suspend fun checkToken(token: String): Boolean {
        val code = networking.api.getRandomQuote(token).code()
        return code != 401
    }
}