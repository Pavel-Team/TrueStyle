package ru.dm.android.truestyle.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.Article
import ru.dm.android.truestyle.model.Clothes
import ru.dm.android.truestyle.model.Recommendation

class RecommendationViewModel : ViewModel() {
    var liveDataQuote: MutableLiveData<Recommendation> = MutableLiveData()
    var liveDataClothes: MutableLiveData<List<Clothes>> = MutableLiveData()
    var liveDataArticles: MutableLiveData<List<Article>> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveDataQuote.value = Recommendation("Мода - это игра, в которую нужно играть серьезно",
            "Карел Лагерфельд")
        liveDataClothes.value = listOf(
            Clothes("Кофта женская осень-весна", "www.url1"),
            Clothes("Новогодняя кофта с оленями", "www.url2"),
            Clothes("Свитер со снежинками", "www.url3")
        )
        liveDataArticles.value = listOf(
            Article("Помада под твое платье", "www.url4"),
            Article("Пончики как стиль одежды", "www.url5"),
            Article("Тренды 2022", "www.url6"),
            Article("История моды", "www.url7"),
            Article("Первое пальто", "www.url8")
        )
    }
}