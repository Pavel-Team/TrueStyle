package ru.dm.android.truestyle.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.api.response.AppVersion
import ru.dm.android.truestyle.repository.ApplicationRepository

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel: ViewModel() {

    private val applicationRepository = ApplicationRepository

    var liveDataAppVersion: MutableLiveData<AppVersion> = MutableLiveData() //Для получения номера актуальной версии с сервера
    var liveDataCorrectAppVersion: MutableLiveData<Boolean> = MutableLiveData(false) //Является ли версия пользователя минимально допустимой


    init {
        Log.d(TAG, "init")
    }


    //Функция проверки актуальной версии приложения
    fun checkCurrentAppVersion() {
        viewModelScope.launch {
            Log.d(TAG, "before getCurrentAppVersion")
            val appVersion = applicationRepository.getCurrentAppVersion()
            Log.d(TAG, appVersion.toString())
            Log.d(TAG, "after appVersion")
            liveDataAppVersion.value = appVersion!!
            Log.d(TAG, "after appVersion2")
        }
    }


    //Метод сравнения двух версий
    //Вернет true, если версия1 >= версии2
    //Иначе вернет false
    fun compareVersion(versionName1: String, versionName2: String): Boolean {
        val array1 = versionName1.split('.')
        val array2 = versionName2.split('.')
        return (10000 * array1[0].toInt() + 100 * array1[1].toInt() + array1[2].toInt()) >=
                (10000 * array2[0].toInt() + 100 * array2[1].toInt() + array2[2].toInt())
    }
}