package ru.dm.android.truestyle.repository

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
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
        Log.d(TAG, "before appVersion")
        val response = networking.api.getCurrentAppVersion()
        Log.d(TAG, "after appVersion")
        Log.d(TAG, response?.code().toString())
        Log.d(TAG, response.toString())
        return response?.body()
    }


    //Проверяет, валидный ли токен
    suspend fun checkToken(token: String): Boolean {
        val code = networking.api.getRandomQuote(token).code()
        return code != 401
    }
}