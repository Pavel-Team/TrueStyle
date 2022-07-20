package ru.dm.android.truestyle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.model.Setting
import ru.dm.android.truestyle.preferences.ApplicationPreferences


//
class SettingViewModel(application: Application): AndroidViewModel(application) {

    private var app = application

    //Функция настройки onClick'а настройки
    fun onClickSetting(titleSetting: String, setting: Setting) {
        var shortLanguage = ""
        when (setting.value) {
            "Русский" -> shortLanguage="ru"
            "English" -> shortLanguage="en"
        }

        when (titleSetting) {
            app.resources.getString(R.string.row_settings_language) -> {
                ApplicationPreferences.setLanguage(app.applicationContext, shortLanguage)
            }
            app.applicationContext.resources.getString(R.string.row_settings_theme) -> {}
            app.applicationContext.resources.getString(R.string.row_settings_push) -> {}
        }
    }
}