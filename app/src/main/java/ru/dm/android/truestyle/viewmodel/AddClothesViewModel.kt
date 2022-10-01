package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.dm.android.truestyle.api.response.Gender
import ru.dm.android.truestyle.api.response.Stuff
import java.lang.RuntimeException

class AddClothesViewModel(application: Application) : AndroidViewModel(application) {
    var liveData: MutableLiveData<Stuff> = MutableLiveData(Stuff()) //Добавляемая одежда
    var liveDataArrayCategories: MutableLiveData<Array<String>> = MutableLiveData(arrayOf())

    val liveDataIsCorrectTitle: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveDataIsCorrectCategory: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveDataIsCorrectSeason: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveDataIsCorrectGender: MutableLiveData<Boolean> = MutableLiveData(false)

    var uriImage: Uri? = null

    init {
        Log.d("AddClothesViewModel", "init")
        liveData.value = Stuff(1, "", Gender(1, "Men"), "Куртки")
        liveDataArrayCategories.value = arrayOf("Куртки", "Не куртки", "Шапки", "Не шапки")
    }

    override fun onCleared() {
        super.onCleared()

        //Удаление файла (если эта фотка из галереи - то не удалится. Иначе - фрагмент 100% закрылся, файл очистился)
        try {
            getApplication<Application>().applicationContext.contentResolver.delete(uriImage!!, null, null)
            Log.d("add", "delete photo")
        } catch (e: RuntimeException) {
            Log.d("add", "dont delete")
        }
    }
}