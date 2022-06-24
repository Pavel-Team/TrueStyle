/**Модель одного экземляра статьи в рекомендациях*/
package ru.dm.android.truestyle.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class ArticleRecommendation(val id: Int,
                                 private val _title: String,
                                 private val _imageUrl: String): BaseObservable() {

    @get:Bindable
    var title: String = _title
    set(value) {
        field = value
        notifyChange()
    }

    @get:Bindable
    var imageUrl: String = _imageUrl
    set(value) {
        field = value
        notifyChange()
    }

}