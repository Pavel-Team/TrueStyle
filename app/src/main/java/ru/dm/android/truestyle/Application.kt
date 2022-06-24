/**Класс приложения*/
package ru.dm.android.truestyle

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper

private const val TAG = "Application"

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
    }


    override fun getApplicationContext(): Context {
        var newBase = super.getApplicationContext()
        LanguageContextWrapper.wrap(newBase, ApplicationPreferences.getLanguage(newBase))
        resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
        return newBase
    }

}