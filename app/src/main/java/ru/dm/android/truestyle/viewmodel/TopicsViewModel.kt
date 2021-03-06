package ru.dm.android.truestyle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.android.truestyle.model.Topic


class TopicsViewModel: ViewModel() {
    var liveData: MutableLiveData<List<Topic>> = MutableLiveData()

    //ВРЕМЕННО
    init {
        liveData.value = listOf(
            Topic(1, "Современные тенденции в моде", "www.url1.ru"),
            Topic(2, "Мода XX века", "www.url2.ru"),
            Topic(3, "Собери свой стиль", "www.url3.ru"),
        )
    }
}