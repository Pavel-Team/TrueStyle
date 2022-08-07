/**Класс для работы с API*/
package ru.dm.android.truestyle.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.dm.android.truestyle.util.Constants


object Networking {

    //P.S. Interceptor добавляется в классе Application
    var okHttpClient = OkHttpClient.Builder()
        .build()

    var retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    var api : Api = retrofit.create()
}