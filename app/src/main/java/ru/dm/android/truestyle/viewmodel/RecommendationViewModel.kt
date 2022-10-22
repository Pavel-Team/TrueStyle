package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Advertisement
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.api.response.Quote
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.ApplicationRepository
import ru.dm.android.truestyle.repository.PartnerRepository
import ru.dm.android.truestyle.repository.RecommendationRepository
import ru.dm.android.truestyle.util.Constants
import java.net.SocketTimeoutException

private const val TAG = "RecommendViewModel"

class RecommendationViewModel  constructor(application: Application): AndroidViewModel(application) {

    val recommendationRepository = RecommendationRepository
    val applicationRepository = ApplicationRepository
    val partnerRepository = PartnerRepository

    var liveDataValidToken: MutableLiveData<Boolean> = MutableLiveData(true) //Сделано палкой, но из активити Response не возвращается
    var liveDataQuote: MutableLiveData<Quote> = MutableLiveData()
    var liveDataClothes: MutableLiveData<List<Stuff>> = MutableLiveData()
    var liveDataArticles: MutableLiveData<List<Article>> = MutableLiveData()
    val liveDataPartners: MutableLiveData<List<Advertisement>> = MutableLiveData(listOf())

    //ВРЕМЕННО
    init {
        loadData()
    }


    //Обработчик кнопки "плюсик" (сделать фото)
    fun onClickPlus(){
        Log.d("sss", "onClick")

//        val navController =
//        val action = RecommendationFragmentDirections.actionNavigationRecommendationToNavigationClothesSearch()
//
//        navController.navigate(action)
    }


    //Метод подгрузки данных с сервера
    fun loadData() {
        Log.d(TAG, "loadData()")
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        //Берем данные с сервера
        viewModelScope.launch {
            try {
                liveDataValidToken.value = applicationRepository.checkToken(token)
                Log.d("sssss", liveDataValidToken.value.toString())
                liveDataQuote.value = recommendationRepository.getQuote(token)
                liveDataClothes.value = recommendationRepository.getRecommendedClothes(token)
                liveDataArticles.value = recommendationRepository.getRecommendedArticles(token)
                liveDataPartners.value = partnerRepository.getPartners(token)
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                Log.d("sss", "No internet connection")
            }
        }
    }
}