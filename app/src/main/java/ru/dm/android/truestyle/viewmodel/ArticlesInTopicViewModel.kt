package ru.dm.android.truestyle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.model.ArticleInTopic
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.ArticlesRepository
import ru.dm.android.truestyle.util.Constants
import javax.inject.Inject

@HiltViewModel
class ArticlesInTopicViewModel @Inject constructor(application: Application,
                                                   val articlesRepository: ArticlesRepository
): AndroidViewModel(application) {
    var liveData: MutableLiveData<List<Article>> = MutableLiveData()
    var liveDataTopic: MutableLiveData<String> = MutableLiveData()

    init {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            liveData.value = articlesRepository.getAllArticles(token)
        }
    }
}