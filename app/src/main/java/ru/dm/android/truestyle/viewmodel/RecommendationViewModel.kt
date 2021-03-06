package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.api.response.Quote
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.RecommendationRepository
import ru.dm.android.truestyle.util.Constants
import java.net.SocketTimeoutException


class RecommendationViewModel  constructor(application: Application): AndroidViewModel(application) {

    val recommendationRepository = RecommendationRepository

    var liveDataQuote: MutableLiveData<Quote> = MutableLiveData()
    var liveDataClothes: MutableLiveData<List<Stuff>> = MutableLiveData()
    var liveDataArticles: MutableLiveData<List<Article>> = MutableLiveData()

    //ВРЕМЕННО
    init {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        //Берем данные с сервера
        viewModelScope.launch {
            try {
                liveDataQuote.value = recommendationRepository.getQuote(token)
                liveDataClothes.value = recommendationRepository.getRecommendedClothes(token)
                liveDataArticles.value = recommendationRepository.getRecommendedArticles(token)
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                Log.d("sss", "No internet connection")
            }
        }
    }


    //Обработчик кнопки "плюсик" (сделать фото)
    fun onClickPlus(){
        Log.d("sss", "onClick")

//        val navController =
//        val action = RecommendationFragmentDirections.actionNavigationRecommendationToNavigationClothesSearch()
//
//        navController.navigate(action)
    }
}