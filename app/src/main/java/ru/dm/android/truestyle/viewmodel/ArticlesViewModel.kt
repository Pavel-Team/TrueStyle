package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.ArticlesRepository
import ru.dm.android.truestyle.util.Constants

private const val TAG = "ArticlesViewModel"

class ArticlesViewModel  constructor(application: Application): AndroidViewModel(application) {
    var liveData: MutableLiveData<List<Article>> = MutableLiveData()

    private val articlesRepository = ArticlesRepository

    init {
        Log.d(TAG, "init")
        liveData.value = listOf(Article(title="Loading"))
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            liveData.value = articlesRepository.getSliderArticles(token)
        }
    }
}