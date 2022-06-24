package ru.dm.android.truestyle.model

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

private const val URL: String = "https://localhost:8080/"
//private const val URL: String = "https://developer.android.com/reference/android/widget/"

data class Article(val id: Int,
                   private val _nameFileArticle: String): BaseObservable() {

    @get:Bindable
    val uri: String
    get() {
//        return Uri.parse(URL)
//            .buildUpon()
//            .appendPath(_nameFileArticle)
//            .build()
//            .toString()
        return "http://192.168.1.71:8080/user/helpme"
    }
}