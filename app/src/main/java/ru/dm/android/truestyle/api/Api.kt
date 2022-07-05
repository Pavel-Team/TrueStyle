package ru.dm.android.truestyle.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.api.response.TextMessage
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
}