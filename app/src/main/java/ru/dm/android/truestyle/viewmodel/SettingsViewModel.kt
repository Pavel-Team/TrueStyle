package ru.dm.android.truestyle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.model.Settings
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import java.util.*


class SettingsViewModel(application: Application): AndroidViewModel(application) {
    var liveData: MutableLiveData<Settings> = MutableLiveData()

    init {
        initViewModel()
    }


    //Инициализиурет значения в соответствии с языком
    fun initViewModel() {
        val context = getApplication<Application>().applicationContext
        var shortLanguage =  ApplicationPreferences.getLanguage(context)
        if (shortLanguage == "") {
            shortLanguage = Locale.getDefault().language
            ApplicationPreferences.setLanguage(context, shortLanguage)
        }
        var language = ""
        when (shortLanguage) {
            "ru" -> {language="Русский"}
            "en" -> {language="English"}
        }

        liveData.value = Settings(
            language,
            context.resources.getString(R.string.soon),
            context.resources.getString(R.string.soon)
        )
    }


    //Выход из приложения (удаление токена из настроек)
    fun quit() {
        ApplicationPreferences.setToken(getApplication<Application>().applicationContext, "")
    }
}