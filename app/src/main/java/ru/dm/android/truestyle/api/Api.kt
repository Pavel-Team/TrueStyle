package ru.dm.android.truestyle.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.dm.android.truestyle.model.Registration

interface Api {

    //API регистрации
    @POST("/auth/signup")
    suspend fun registerUser(@Body registration: Registration): Response<String>
}