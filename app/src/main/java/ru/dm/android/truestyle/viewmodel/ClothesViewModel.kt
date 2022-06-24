package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.model.Clothes
import javax.inject.Inject

@HiltViewModel
class ClothesViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<Clothes> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = Clothes(1,
        "www.url1",
        "Новогодняя кофта с оленями",
        "Синий белый",
        listOf("Осень", "Зима"),
        50,
        "Мужской",
        "Нательный")
    }
}