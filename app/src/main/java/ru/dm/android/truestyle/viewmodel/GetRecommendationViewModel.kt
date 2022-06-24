package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.GetRecommendationClothes

class GetRecommendationViewModel: ViewModel() {
    var liveData: MutableLiveData<List<GetRecommendationClothes>> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = listOf(
            GetRecommendationClothes(1, "Свитер со снежинками", "50 размер, белый синий, мужской, нательный", "www.url1.ru"),
            GetRecommendationClothes(2, "Новогодняя кофта с оленями", "42 размер, красный белый, женская, нательная", "www.url2.ru"),
        )
    }
}