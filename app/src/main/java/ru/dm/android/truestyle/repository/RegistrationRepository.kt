package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.model.Registration

private const val TAG = "RegistrationRepository"


object RegistrationRepository{

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Метод для регистрации пользователя
    //Возвращает Boolean - успешно ли прошла регистрация
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ) : Result<Boolean> {
        Log.d(TAG, "registerUser()")
        val response = networking.api.registerUser(
            Registration(username=username, email=email, password=password)
        )
        val result = response.isSuccessful
        return Result.success(result)
    }


    //Функция проверки существует ли пользователь с таким же именем
    //Возвращает Boolean - существует ли пользователь с таким же именем
    suspend fun checkUsername(username: String): Boolean {
        val response = networking.api.checkUsername(username)
        val result = response.isSuccessful
        return result
    }


    //Функция проверки существует ли пользователь с таким же email
    //Возвращает Boolean - существует ли пользователь с таким же email
    suspend fun checkEmail(email: String): Boolean {
        val response = networking.api.checkEmail(email)
        val result = response.isSuccessful
        return result
    }

}