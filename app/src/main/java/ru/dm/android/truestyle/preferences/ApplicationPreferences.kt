/**Класс, созданный для удобства работы с настройками приложения*/
package ru.dm.android.truestyle.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_LANGUAGE = "language" //Константа для настроек языка
private const val PREF_TOKEN = "token"       //Константа для токена (нужно будет зашифровать)
private const val PREF_DATE_APP_VERSION = "date_app_version" //Константа со временем в мс, прошедших с последнего показа диалогового окна с новой версией

object ApplicationPreferences {

    //Получение сохраненного языка
    fun getLanguage(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_LANGUAGE, "")!!
    }

    fun setLanguage(context: Context, language: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                putString(PREF_LANGUAGE, language)
            }
    }


    //Получение токена
    fun getToken(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_TOKEN, "")
    }

    fun setToken(context: Context, token: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                putString(PREF_TOKEN, token)
            }
    }


    //Получение времени в мс, прошедших с последнего показа диалогового окна с новой версией приложения
    fun getDateDialogCurrentVersion(context: Context): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(PREF_DATE_APP_VERSION, 0L)
    }

    fun setDateDialogCurrentVersion(context: Context, date: Long) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                putLong(PREF_DATE_APP_VERSION, date)
            }
    }

}