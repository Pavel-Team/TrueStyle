package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.api.response.Article
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<Article> = MutableLiveData()
}