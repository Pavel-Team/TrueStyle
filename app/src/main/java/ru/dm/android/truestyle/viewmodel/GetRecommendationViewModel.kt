package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.model.GetRecommendationClothes
import javax.inject.Inject

@HiltViewModel
class GetRecommendationViewModel @Inject constructor(): ViewModel() {
    //var liveData: MutableLiveData<List<GetRecommendationClothes>> = MutableLiveData()
    var liveData: MutableLiveData<List<Stuff>> = MutableLiveData()

    init {
        liveData.value = listOf()
    }
}