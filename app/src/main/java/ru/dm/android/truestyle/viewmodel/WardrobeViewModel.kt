package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.WardrobeRepository
import ru.dm.android.truestyle.util.Constants

private const val TAG = "WardrobeViewModel"


class WardrobeViewModel  constructor(application: Application): AndroidViewModel(application) {

    private val wardrobeRepository = WardrobeRepository

    var liveData: MutableLiveData<MutableList<Stuff>> = MutableLiveData()
    var liveDataTitleSeason: MutableLiveData<String> = MutableLiveData()


    init {
        liveData.value = mutableListOf()
    }


    //Костыльный метод для получения названия сезона для отправки на сервер
    private fun getServerTitleSeason(title: String): String {
        var result = Constants.SEASON_NAN
        val res = getApplication<Application>().resources

        when (title) {
            res.getString(R.string.title_summer) -> result = Constants.SEASON_SUMMER
            res.getString(R.string.title_winter) -> result = Constants.SEASON_WINTER
            res.getString(R.string.title_spring) -> result = Constants.SEASON_SPRING
            res.getString(R.string.title_autumn) -> result = Constants.SEASON_AUTUMN
        }

        Log.d(TAG, "result = $result")

        return result
    }


    //Загрузка гардероба (т.к. параметр season можем получить только ПОСЛЕ установки параметра во фрагменте)
    fun loadWardrobe() {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val season = getServerTitleSeason(liveDataTitleSeason.value!!)
            val listSeason = wardrobeRepository.getClothesBySeason(token, season)
            val listAllSeason = wardrobeRepository.getClothesBySeason(token, Constants.SEASON_NAN)
            liveData.value = listOf(listSeason!!.usersStuffs, listAllSeason!!.usersStuffs, listSeason.shopsStuffs, listAllSeason.shopsStuffs)
                .flatten().toMutableList()
            Log.d(TAG, liveData.value.toString())
        }
    }


    //Удаление одежды из гардероба
    fun deleteClothes(id: Long) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val response = wardrobeRepository.deleteClothes(token, id)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val newList = liveData.value?.toMutableList()
                newList?.removeIf { it.id==id }
                if (newList != null)
                    liveData.value = newList!!
                else
                    liveData.value = mutableListOf()
            }
        }
    }


    //Удаление пользовательской одежды из гардероба
    fun deleteUserStuffFromWardrobe(id: Long) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val response = wardrobeRepository.deleteUserStuff(token, id)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val newList = liveData.value?.toMutableList()
                newList?.removeIf { it.id==id }
                if (newList != null)
                    liveData.value = newList!!
                else
                    liveData.value = mutableListOf()
            }
        }
    }
}