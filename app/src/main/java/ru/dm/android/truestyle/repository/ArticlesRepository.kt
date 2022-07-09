package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.Article
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ArticlesRepository"

@Singleton
class ArticlesRepository @Inject constructor(val networking: Networking) {

    init {
        Log.d(TAG, "init")
    }


    //Получение статей для слайдера (3 статьи)
    suspend fun getSliderArticles(token: String): List<Article> {
        val list = networking.api.getSliderArticles(token).body().orEmpty()
        return list
    }
}