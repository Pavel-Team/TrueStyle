/**Модель одного экземпляра одежды (страница с подробной информацией об одежде)*/
package ru.dm.android.truestyle.model

import android.util.Log

data class Clothes(val id: Int,
                   val imageUrl: String,
                   val title: String,
                   val color: String,
                   private val _season: List<String>,
                   val size: Int,
                   val gender: String,
                   val type: String) {

    var seasons = ""
    get() {
        for (value in _season)
            field += "$value, "
        field = field.substring(0, field.length-2)
        return field
    }

}