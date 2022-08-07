/**Класс, созданный для удобства работы с настройками приложения*/
package ru.dm.android.truestyle.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_LANGUAGE = "language" //Константа для настроек языка
private const val PREF_TOKEN = "token"       //Константа для токена (нужно будет зашифровать)

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

}