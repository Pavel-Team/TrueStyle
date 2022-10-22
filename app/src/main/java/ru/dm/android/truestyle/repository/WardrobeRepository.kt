package ru.dm.android.truestyle.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.request.UserStuff
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.api.response.WardrobeResponse
import java.io.File


private const val TAG = "WardrobeRepository"


object WardrobeRepository{

    private val networking = Networking

    init {
        Log.d(TAG, "init")
    }


    //Получение всей одежды по заданному id сезона
    suspend fun getClothesBySeason(token: String, season: String): WardrobeResponse? {
        val lists = networking.api.getUserClothesBySeason(token, season).body()
        return lists
    }


    //Добавление одежды по id в гардероб пользователя
    //В случае успеха вернет - Stuff ADDED
    //В случае если вещь добавлена - Stuff has already been added
    //Иначе - 500 ошибка
    suspend fun addClothes(token: String, id: Long): String? {
        val response = networking.api.addClothesInWardrobe(token, id).body()?.message
        Log.d(TAG, "response = $response")
        return response
    }


    //Удаление одежды по id в гардеробе пользователя
    //В случае успеха вернет - Stuff DELETED
    //В случае если вещь была удалена или ее нет - Stuff was not removed because it was not in your wardrobe
    //Иначе - 500 ошибка
    suspend fun deleteClothes(token: String, id: Long): String{
        val response = networking.api.deleteClothesInWardrobe(token, id).message()
        Log.d(TAG, response)
        return response
    }


    //Проверка, есть ли одежда с данным id в гардеробе пользователя
    suspend fun checkClothesInWardrobe(token: String, id: Long, type: String): Boolean {
        val response = networking.api.checkClothesInWardrobe(token, type, id)
        Log.d(TAG, "response = " + response.body()?.message)
        Log.d(TAG, "message = " + response.message())
        return response.isSuccessful
    }


    //Добавление фото одежды пользователя в гардероб
    //В случае успеха вернет - Stuff ADDED
    //В случае неудачи - Stuff has already been added, because ...
    //Если фотографий больше чем 110 - то Stuff didn't add, because The wardrobe is limited to 110 items!
    suspend fun addUserStuff(token: String,
                             stuffInfo: UserStuff,
                             imageFile: File): String? {
        val gson: Gson = GsonBuilder().create()

        val imgFormData = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            RequestBody.create(
                MediaType.parse("image/*"),
                imageFile
            )
        )

        val stuffInfoFormData =
            RequestBody.create(
                MediaType.parse("application/json"),
                gson.toJson(stuffInfo)
            )


        val response = networking.api.addUserStuff(
            stuffInfo = stuffInfoFormData,
            img = imgFormData,
            token = token
        )

        return response.body()?.message
    }


    //Удаление одежды по id в гардеробе пользователя
    //В случае успеха вернет - Stuff DELETED
    //В случае если вещь была удалена или ее нет - Stuff was not removed because it was not in your wardrobe
    //Иначе - 500 ошибка
    suspend fun deleteUserStuff(token: String, id: Long): String{
        val response = networking.api.deleteUserStuff(token, id).message()
        Log.d(TAG, response)
        return response
    }


    //Получение всех категорий одежды
    //Возвращает список всех категорий одежды
    suspend fun getAllCategories(token: String): List<String>? {
        val response = networking.api.getAllCategories(token).body()
        Log.d(TAG, "response = $response")
        return response
    }

}