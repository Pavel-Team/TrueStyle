package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.model.WardrobeClothes
import javax.inject.Inject

@HiltViewModel
class WardrobeViewModel @Inject constructor(): ViewModel() {
    var liveData: MutableLiveData<List<WardrobeClothes>> = MutableLiveData()
    var liveDataTitleSeason: MutableLiveData<String> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = listOf(
            WardrobeClothes(1, "Кофта белая", "46 размер, белый, женская, нательная", "www.url1.ru"),
            WardrobeClothes(2, "Свитер белый синий", "50 размер, белый синий, мужской, нательный", "www.url2.ru")
        )
    }
}