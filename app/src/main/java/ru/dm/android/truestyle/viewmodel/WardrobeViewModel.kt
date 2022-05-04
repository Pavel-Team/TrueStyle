package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.WardrobeClothes

class WardrobeViewModel: ViewModel() {
    var liveData: MutableLiveData<List<WardrobeClothes>> = MutableLiveData()
    var liveDataTitleSeason: MutableLiveData<String> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = listOf(
            WardrobeClothes(1, "Кофта с оленями", "32 размер, бежевый, женская, нательная", "www.url1.ru"),
            WardrobeClothes(2, "Кофта белая", "34 размер, бежевый, женская, нательная", "www.url1.ru"),
            WardrobeClothes(3, "Кофта", "36 размер, красная, женская, нательная", "www.url1.ru")
        )
    }
}