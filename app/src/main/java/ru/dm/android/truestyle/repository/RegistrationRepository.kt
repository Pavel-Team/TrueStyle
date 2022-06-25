package ru.dm.android.truestyle.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.LoginRequest
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.api.response.TextMessage
import ru.dm.android.truestyle.model.Registration
import ru.dm.android.truestyle.model.User

private const val TAG = "RegistrationRepository"

object RegistrationRepository {

    private val networking = Networking
    private val sqLiteRepository = SQLiteRepository.get()

    init {
        Log.d(TAG, "init")
    }


    //Проверка имени пользователя
    fun checkUsername(username: String) : Boolean{
        val request = networking.api.checkUsername(username)

        val result = request.execute().body()
        Log.d(TAG, result.toString())
        return result != null && result.message == "Username isn't exist yet"
    }


    //Проверка email пользователя
    fun checkEmail(email: String) : Boolean{
        val request = networking.api.checkEmail(email)

        val result = request.execute().body()
        Log.d(TAG, result.toString())
        return result != null && result.message == "Email isn't exist yet"
    }


    //Регистрация пользователя
    //Возвращает модельку Auth пользователя после успешной регистрации
    fun registerUser(username: String, email: String, password: String) : Auth? {
        Log.d(TAG, "start register")

        //val responseLiveData: MutableLiveData<Auth> = MutableLiveData()

        val request = networking.api.registerUser(
            Registration(username, email, password)
        )

        /*request.enqueue(object: Callback<TextMessage> {
            override fun onResponse(call: Call<TextMessage>, response: Response<TextMessage>) {
                Log.d(TAG, response.body()!!.message)
                if (response.body()!!.message == "User CREATED") {
                    val requestAuth = networking.api.signIn(
                        LoginRequest(username, password)
                    )

                    requestAuth.enqueue(object: Callback<Auth> {
                        override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                            Log.d(TAG, response.body()!!.username)
                            responseLiveData.value = response.body()

                            //Заолнение рум
                            //...
                        }

                        override fun onFailure(call: Call<Auth>, t: Throwable) {
                            Log.d(TAG, t.toString())
                        }
                    })
                }
            }

            override fun onFailure(call: Call<TextMessage>, t: Throwable) {
                Log.d(TAG, t.toString())
            }
        })*/

        Log.d(TAG, "before execute")
        val result = request.execute().body()?.message
        if (result != null) {
            Log.d(TAG, result)
            if (result == "User CREATED") {
                val requestAuth = networking.api.signIn(
                    LoginRequest(username, password)
                )
                val resultAuth = requestAuth.execute().body()!!

                //Добавляем в SQLite
                Log.d(TAG, resultAuth.toString())
                sqLiteRepository.addUser(resultAuth)

                Log.d(TAG, "end register")

                return resultAuth
            }
        }
        return null
    }
}