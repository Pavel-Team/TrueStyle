package ru.dm.android.truestyle.repository

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.model.Registration

private const val TAG = "RegistrationRepository"

object RegistrationRepository {

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Регистрация пользователя
    fun registerUser(username: String, email: String, password: String){
        Log.d(TAG, "before response")
        Log.d(TAG, networking.api.toString())
        val response = networking.api.registerUser(
            Registration(username,email,password)
        )
        Log.d(TAG, "after response")
                val result = response.enqueue(object: Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d(TAG, response.body()!!)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d(TAG, t.toString())
                    }

                })
        Log.d(TAG, "result")
    }
}