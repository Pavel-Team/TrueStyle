/**Класс, созданный для удобства работы с настройками приложения*/
package ru.dm.android.truestyle.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_LANGUAGE = "language" //Константа для настроек языка

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
}