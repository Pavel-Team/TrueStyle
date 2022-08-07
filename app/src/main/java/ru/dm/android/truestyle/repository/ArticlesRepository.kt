package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.Article

private const val TAG = "ArticlesRepository"


object ArticlesRepository {

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Получение статей для слайдера (3 статьи)
    suspend fun getSliderArticles(token: String): List<Article> {
        val list = networking.api.getSliderArticles(token).body().orEmpty()
        return list
    }


    //Получение статьи по её id
    suspend fun getArticleById(token: String, id: Long): Article? {
        val article = networking.api.getArticleById(token, id).body()
        return article
    }


    //Получение всех статей
    suspend fun getAllArticles(token: String): List<Article> {
        val list = networking.api.getAllArticles(token).body().orEmpty()
        return list
    }
}