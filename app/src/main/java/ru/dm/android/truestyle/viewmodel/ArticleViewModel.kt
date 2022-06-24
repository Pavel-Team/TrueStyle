package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.model.Article
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<Article> = MutableLiveData()


    //Метод загрузки страницы
    fun loadArticle(idArticle: Int?) {
        //ВРЕМЕННО
        //liveData.value = idArticle?.let { Article(it, "ProgressBar#attr_android:max") }
        liveData.value = idArticle?.let { Article(it, "user/helpme") }
    }
}