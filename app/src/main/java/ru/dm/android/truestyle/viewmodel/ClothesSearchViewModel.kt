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
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.StuffRepository
import ru.dm.android.truestyle.util.Constants
import javax.inject.Inject

private const val TAG = "ClothesSearchViewModel"

@HiltViewModel
class ClothesSearchViewModel @Inject constructor(application: Application,
                                                 private val stuffRepository: StuffRepository): AndroidViewModel(application) {

    var liveData: MutableLiveData<List<Stuff>> = MutableLiveData()

    //Получение рекомендованной одежды
    fun findClothes(stuffData: List<Int>) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            Log.d(TAG, "stuffData:")
            Log.d(TAG, stuffData.toString())
            Log.d(TAG, "start find photo")
            liveData.value = stuffRepository.findClothes(stuffData, token)
            Log.d(TAG, "end find photo")
            Log.d(TAG, liveData.value.toString())
        }
    }
}