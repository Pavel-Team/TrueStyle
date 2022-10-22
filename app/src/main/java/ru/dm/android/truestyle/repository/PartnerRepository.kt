package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.Advertisement

private const val TAG = "PartnerRepository"

object PartnerRepository {

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Функция получения листа партнеров
    suspend fun getPartners(token: String): List<Advertisement>? {
        val response = networking.api.getPartners(token)
        return response.body()
    }

}