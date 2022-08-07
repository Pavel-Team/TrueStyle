package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.api.response.Quote
import ru.dm.android.truestyle.api.response.Stuff

private const val TAG = "RecommendRepository"


object RecommendationRepository{

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Получение цитаты дня
    suspend fun getQuote(token: String): Quote? {
        val quote = networking.api.getRandomQuote(token).body()
        return quote
    }


    //Получение рекомендованной одежды
    //Возвращает лист с одеждой
    suspend fun getRecommendedClothes(token: String): List<Stuff> {
        val list = networking.api.getRecommendedClothes(token).body().orEmpty()
        Log.d(TAG, list.toString())
        return list
    }


    //Получение рекомендованных статей для главной страницы
    //Возвращает список с рекомендованными статьями
    suspend fun getRecommendedArticles(token: String): List<Article> {
        val list = networking.api.getRecommendedArticles(token).body().orEmpty()
        return list
    }
}