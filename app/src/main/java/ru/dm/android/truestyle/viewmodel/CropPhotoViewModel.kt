package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.StuffRepository
import ru.dm.android.truestyle.util.Constants

private const val TAG = "CropPhotoViewModel"

class CropPhotoViewModel constructor(application: Application): AndroidViewModel(application) {
    val liveDataPhotoUri: MutableLiveData<Uri> = MutableLiveData(null)
    var liveData: MutableLiveData<List<Stuff>> = MutableLiveData(listOf())
    private val stuffRepository = StuffRepository


    //Получение рекомендованной одежды
    fun findClothes(stuffData: String) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.async {
            Log.d(TAG, "stuffData:")
            Log.d(TAG, stuffData.toString())
            Log.d(TAG, "start find photo")
            liveData.value = stuffRepository.findClothes(stuffData, token)
            Log.d(TAG, "end find photo")
            Log.d(TAG, liveData.value.toString())
        }
    }
}