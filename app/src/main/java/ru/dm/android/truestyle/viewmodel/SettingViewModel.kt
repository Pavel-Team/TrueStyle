package ru.dm.android.truestyle.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.model.Setting
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import java.util.*
import android.os.Build

import android.annotation.TargetApi
import android.util.Log


class SettingViewModel(application: Application): AndroidViewModel(application) {

    private var app = application

    //Функция настройки onClick'а настройки
    fun onClickSetting(titleSetting: String, setting: Setting) {
        var shortLanguage = ""
        when (setting.value) {
            "Русский" -> shortLanguage="ru"
            "English" -> shortLanguage="en"
        }

        //ПАЛКА: НУЖНО КАК ТО МЕНЯТЬ ЛОКАЛЬ СТРОК ТОЖЕ
        when (titleSetting) {
            "Язык" -> {
                ApplicationPreferences.setLanguage(app.applicationContext, shortLanguage)
            }
            "Language" -> {
                ApplicationPreferences.setLanguage(app.applicationContext, shortLanguage)

                //Смена языка
                //...
            }
            app.applicationContext.resources.getString(R.string.row_settings_theme) -> {}
            app.applicationContext.resources.getString(R.string.row_settings_push) -> {}
        }
    }
}