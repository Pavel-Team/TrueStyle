package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.model.ArticleInTopic
import javax.inject.Inject

@HiltViewModel
class ArticlesInTopicViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<List<Article>> = MutableLiveData()
    var liveDataTopic: MutableLiveData<String> = MutableLiveData()

    init {
        viewModelScope.launch {
            liveData.value = listOf(Article()) //Временно
        }
    }
}