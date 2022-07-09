package ru.dm.android.truestyle.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.response.*
import ru.dm.android.truestyle.model.Registration

interface Api {

    //API регистрации
    @POST("auth/signup")
    suspend fun registerUser(@Body registration: Registration): Response<TextMessage>

    @GET("auth/check/username")
    suspend fun checkUsername(@Query("username") username: String): Response<TextMessage>

    @GET("auth/check/email")
    suspend fun checkEmail(@Query("username") username: String): Response<TextMessage>

    //API авторизации
    @POST("auth/signin")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<Auth>

    //API рекомендаций (главная страница)
    @GET("quote/random")
    suspend fun getRandomQuote(@Header("Authorization") token: String): Response<Quote>

    @GET("clothes/recommended")
    suspend fun getRecommendedClothes(@Header("Authorization") token: String): Response<List<Stuff>>

    @GET("art/recommended/five")
    suspend fun getRecommendedArticles(@Header("Authorization") token: String): Response<List<Article>>

    //API статей (страница со слайдером)
    @GET("art/recommended/tree")
    suspend fun getSliderArticles(@Header("Authorization") token: String): Response<List<Article>>
}