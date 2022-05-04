package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.ArticleInTopic

class ArticlesInTopicViewModel: ViewModel() {
    var liveData: MutableLiveData<List<ArticleInTopic>> = MutableLiveData()
    var liveDataTopic: MutableLiveData<String> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = listOf(
            ArticleInTopic(1, "Помада в 2022 году и ее все цвета", "Без помады в косметичке не обойтись и это факт", "www.url1.ru"),
            ArticleInTopic(2, "Зеленый, бежевый и розовый тон как новый стиль", "Без помады в косметичке не обойтись и это факт", "www.url2.ru"),
            ArticleInTopic(3, "Кофты с показа моды 2020 январь", "Без помады в косметичке не обойтись и это факт", "www.url3.ru")
            )
    }
}