package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.MlRequest
import ru.dm.android.truestyle.api.response.Stuff
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "StuffRepository"

@Singleton
class StuffRepository @Inject constructor(private val networking: Networking){

    init {
        Log.d(TAG, "init")
    }


    //Получение похожей одежды
    suspend fun findClothes(stuffData: List<Int>, token: String): List<Stuff> {
        val list = networking.api.getCvStuff(stuffData, token)
        Log.d(TAG, list.message())
        Log.d(TAG, list.code().toString())
        Log.d(TAG, list.body().toString())
        Log.d(TAG, list.errorBody().toString())
        return list.body().orEmpty()
    }

}