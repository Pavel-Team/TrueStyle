package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.model.Clothes
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.WardrobeRepository
import ru.dm.android.truestyle.util.Constants
import javax.inject.Inject

@HiltViewModel
class ClothesViewModel @Inject constructor(application: Application,
                                           private val wardrobeRepository: WardrobeRepository): AndroidViewModel(application) {
    var liveData: MutableLiveData<Stuff> = MutableLiveData()

    //Добавление одежды в гардероб
    fun addClothesInWardrobe() {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val response = wardrobeRepository.addClothes(token, liveData.value!!.id)
        }
    }
}