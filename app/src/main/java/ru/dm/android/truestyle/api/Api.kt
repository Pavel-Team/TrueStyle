package ru.dm.android.truestyle.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.dm.android.truestyle.model.Registration

interface Api {

    //API регистрации
    @POST("auth/signup")
    fun registerUser(@Body registration: Registration): Call<String>
}