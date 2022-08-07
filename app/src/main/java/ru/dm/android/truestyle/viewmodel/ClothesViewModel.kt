package ru.dm.android.truestyle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.WardrobeRepository
import ru.dm.android.truestyle.util.Constants


class ClothesViewModel  constructor(application: Application): AndroidViewModel(application) {
    var liveData: MutableLiveData<Stuff> = MutableLiveData() //Одежда
    var liveDataHasInWardrobe: MutableLiveData<Boolean> = MutableLiveData(false) //Есть ли одежда в гардеробе пользователя

    private val wardrobeRepository = WardrobeRepository

    //Добавление одежды в гардероб
    fun addClothesInWardrobe() {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val response = wardrobeRepository.addClothes(token, liveData.value!!.id)
        }
    }


    //Удаление одежды из гардероба
    fun deleteClothesFromWardrobe() {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val response = wardrobeRepository.deleteClothes(token, liveData.value!!.id)
        }
    }


    //Проверка, есть ли одежда в гардеробе пользователя
    fun checkClothesInWardrobe() {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            liveDataHasInWardrobe.value = wardrobeRepository.checkClothesInWardrobe(token, liveData.value!!.id)
        }
    }
}