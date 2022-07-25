/**Класс приложения*/
package ru.dm.android.truestyle

import android.app.Application
import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.dm.android.truestyle.api.InternetConnectionInterceptor
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.util.Constants

private const val TAG = "Application"

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        //Настраиваем нетворкинг объект
        Networking.apply {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(InternetConnectionInterceptor(applicationContext))
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            api = retrofit.create()
        }
    }


    override fun getApplicationContext(): Context {
        var newBase = super.getApplicationContext()
        LanguageContextWrapper.wrap(newBase, ApplicationPreferences.getLanguage(newBase))
        resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
        return newBase
    }

}