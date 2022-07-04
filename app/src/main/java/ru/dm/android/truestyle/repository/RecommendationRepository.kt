package ru.dm.android.truestyle.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.Stuff

private const val TAG = "RecommendationRep"

object RecommendationRepository {

    private val networking = Networking
    private val sqLiteRepository = SQLiteRepository.get()

    init {
        Log.d(TAG, "init")
    }


    //Получение рекомендуемой одежды
    fun getRecommendedClothes(token: String, type: String): LiveData<List<Stuff>> {

        val result: MutableLiveData<List<Stuff>> = MutableLiveData()
        val request = networking.api.getRecommendedClothes("$type $token")

        request.enqueue(object: Callback<List<Stuff>> {
            override fun onResponse(call: Call<List<Stuff>>, response: Response<List<Stuff>>) {
                Log.d(TAG, response.body().toString())
                result.value = response.body()
            }

            override fun onFailure(call: Call<List<Stuff>>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })

        return result
    }
}