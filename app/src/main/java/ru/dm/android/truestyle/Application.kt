/**Класс приложения*/
package ru.dm.android.truestyle

import android.app.Application
import android.content.Context
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.repository.SQLiteRepository

private const val TAG = "Application"

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        SQLiteRepository.initialize(this)
    }


    override fun getApplicationContext(): Context {
        var newBase = super.getApplicationContext()
        LanguageContextWrapper.wrap(newBase, ApplicationPreferences.getLanguage(newBase))
        resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
        return newBase
    }

}