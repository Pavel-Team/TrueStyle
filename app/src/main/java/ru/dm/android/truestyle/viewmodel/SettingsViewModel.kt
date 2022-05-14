package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.model.Settings
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import java.util.*

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    var liveData: MutableLiveData<Settings> = MutableLiveData()

    init {
        var shortLanguage =  ApplicationPreferences.getLanguage(application.applicationContext)
        if (shortLanguage == "") {
            shortLanguage = Locale.getDefault().language
            ApplicationPreferences.setLanguage(application.applicationContext, shortLanguage)
        }
        var language = ""
        when (shortLanguage) {
            "ru" -> {language="Русский"}
            "en" -> {language="English"}
        }

        liveData.value = Settings(
            language,
            application.applicationContext.resources.getString(R.string.soon),
            application.applicationContext.resources.getString(R.string.soon)
        )
    }
}