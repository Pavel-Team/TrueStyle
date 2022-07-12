package ru.dm.android.truestyle.repository

import android.util.Log
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.response.Stuff
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "WardrobeRepository"

@Singleton
class WardrobeRepository @Inject constructor(private val networking: Networking) {

    init {
        Log.d(TAG, "init")
    }


    //Получение всей одежды по заданному id сезона
    suspend fun getClothesBySeason(token: String, season: String): List<Stuff> {
        val list = networking.api.getUserClothesBySeason(token, season).body().orEmpty()
        return list
    }


    //Добавление одежды по id в гардероб пользователя
    //В случае успеха вернет - Stuff ADDED
    //В случае если вещь добавлена - Stuff has already been added
    //Иначе - 500 ошибка
    suspend fun addClothes(token: String, id: Long): String{
        val response = networking.api.addClothesInWardrobe(token, id).message()
        return response
    }


    //Удаление одежды по id в гардеробе пользователя
    //В случае успеха вернет - Stuff DELETED
    //В случае если вещь была удалена или ее нет - Stuff was not removed because it was not in your wardrobe
    //Иначе - 500 ошибка
    suspend fun deleteClothes(token: String, id: Long): String{
        val response = networking.api.deleteClothesInWardrobe(token, id).message()
        return response
    }
}