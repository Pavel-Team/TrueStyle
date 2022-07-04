package ru.dm.android.truestyle.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.api.response.TextMessage
import ru.dm.android.truestyle.model.Registration

interface Api {

    //API регистрации
    @POST("auth/signup")
    fun registerUser(@Body registration: Registration): Call<TextMessage>

    @GET("auth/check/username")
    fun checkUsername(@Query("username") username: String): Call<TextMessage>

    @GET("auth/check/email")
    fun checkEmail(@Query("username") username: String): Call<TextMessage>

    //API авторизации
    @POST("auth/signin")
    fun signIn(@Body loginRequest: LoginRequest): Call<Auth>

    //API рекомендаций (главная страница)
    @GET("clothes/recommended")
    fun getRecommendedClothes(@Header("Authorization") token: String): Call<List<Stuff>>
}