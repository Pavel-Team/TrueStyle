package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.model.Clothes
import javax.inject.Inject

@HiltViewModel
class ClothesViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<Stuff> = MutableLiveData()
}