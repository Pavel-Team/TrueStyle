package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.api.response.Stuff


class GetRecommendationViewModel: ViewModel() {
    var liveData: MutableLiveData<List<Stuff>> = MutableLiveData()

    init {
        liveData.value = listOf()
    }
}