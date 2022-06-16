package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import ru.dm.android.truestyle.model.ArticleRecommendation
import ru.dm.android.truestyle.model.ClothesRecommendation
import ru.dm.android.truestyle.model.Recommendation
import ru.dm.android.truestyle.ui.screen.RecommendationFragmentDirections

class RecommendationViewModel : ViewModel() {
    var liveDataQuote: MutableLiveData<Recommendation> = MutableLiveData()
    var liveDataClothes: MutableLiveData<List<ClothesRecommendation>> = MutableLiveData()
    var liveDataArticles: MutableLiveData<List<ArticleRecommendation>> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveDataQuote.value = Recommendation("Мода - это игра, в которую нужно играть серьезно",
            "Карел Лагерфельд")
        liveDataClothes.value = listOf(
            ClothesRecommendation(1, "Кофта женская осень-весна", "www.url1"),
            ClothesRecommendation(2, "Новогодняя кофта с оленями", "www.url2"),
            ClothesRecommendation(3, "Свитер со снежинками", "www.url3")
        )
        liveDataArticles.value = listOf(
            ArticleRecommendation(1, "Помада под твое платье", "www.url4"),
            ArticleRecommendation(2, "Пончики как стиль одежды", "www.url5"),
            ArticleRecommendation(3, "Что такое мода", "www.url6"),
            ArticleRecommendation(4, "История моды", "www.url7"),
            ArticleRecommendation(5, "Первое пальто", "www.url8")
        )
    }


    //Обработчик кнопки "плюсик" (сделать фото)
    public fun onClickPlus(){
        Log.d("sss", "onClick")

//        val navController =
//        val action = RecommendationFragmentDirections.actionNavigationRecommendationToNavigationClothesSearch()
//
//        navController.navigate(action)
    }
}