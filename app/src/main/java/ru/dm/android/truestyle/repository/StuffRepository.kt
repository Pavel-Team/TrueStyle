package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.ShopStuffCVData
import ru.dm.android.truestyle.api.response.Stuff

private const val TAG = "StuffRepository"


object StuffRepository{

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Получение похожей одежды
    suspend fun findClothes(stuffData: String, token: String): List<Stuff> {
        val list = networking.api.getCvStuff(
            ShopStuffCVData(articleType = stuffData),
            token
        )
        Log.d(TAG, list.message())
        Log.d(TAG, list.code().toString())
        Log.d(TAG, list.body().toString())
        Log.d(TAG, list.errorBody().toString())
        return list.body().orEmpty()
    }

}