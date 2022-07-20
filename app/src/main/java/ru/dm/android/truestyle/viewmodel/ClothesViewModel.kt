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
    var liveData: MutableLiveData<Stuff> = MutableLiveData()

    private val wardrobeRepository = WardrobeRepository

    //Добавление одежды в гардероб
    fun addClothesInWardrobe() {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val response = wardrobeRepository.addClothes(token, liveData.value!!.id)
        }
    }
}