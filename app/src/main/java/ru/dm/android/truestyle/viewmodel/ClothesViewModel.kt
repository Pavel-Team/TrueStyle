package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.Clothes

class ClothesViewModel : ViewModel() {
    var liveData: MutableLiveData<Clothes> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = Clothes(1,
        "www.url1",
        "Новогодняя кофта с оленями",
        "Бежевый",
        listOf("Осень", "Зима"),
        43,
        "Женский",
        "Нательная")
    }
}