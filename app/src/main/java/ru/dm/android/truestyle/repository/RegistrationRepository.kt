package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.model.Registration
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "RegistrationRepository"

@Singleton
class RegistrationRepository @Inject constructor(val networking: Networking) {

    init {
        Log.d(TAG, "init")
    }


    //Метод для регистрации пользователя
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ) : Result<String> {
        Log.d(TAG, "registerUser()")
        Log.d(TAG, networking.api.toString())
        val response = networking.api.registerUser(
            Registration(username=username, email=email, password=password)
        )
        Log.d(TAG, "after response")
        //val result = response.body().orEmpty()
        val result = response.execute().body()
        Log.d(TAG, "after result")
        Log.d(TAG, result!!)
        return Result.success(result)
    }

}