package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.model.RecommendedArticle
import javax.inject.Inject

@HiltViewModel
public class ArticlesViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<List<RecommendedArticle>> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = listOf(
            RecommendedArticle(1, "Тренды моды 2022", "www.url1"),
            RecommendedArticle(2, "История помады", "www.url2"),
            RecommendedArticle(3, "Hello world!", "www.url3")
        )
    }
}