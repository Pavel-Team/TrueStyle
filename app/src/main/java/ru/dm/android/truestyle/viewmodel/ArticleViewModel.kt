package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.api.response.Article


class ArticleViewModel : ViewModel() {
    var liveData: MutableLiveData<Article> = MutableLiveData()
}