package ru.dm.android.truestyle.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.request.UserStuff
import ru.dm.android.truestyle.api.response.Gender
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.repository.WardrobeRepository
import ru.dm.android.truestyle.util.Constants
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


private const val TAG = "AddClothesViewModel"

class AddClothesViewModel(application: Application) : AndroidViewModel(application) {
    var liveData: MutableLiveData<Stuff> = MutableLiveData(Stuff()) //Добавляемая одежда

    val liveDataIsCorrectTitle: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveDataIsCorrectCategory: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveDataIsCorrectSeason: MutableLiveData<Boolean> = MutableLiveData(false)
    val liveDataIsCorrectGender: MutableLiveData<Boolean> = MutableLiveData(false)

    var uriImage: Uri? = null

    private val wardrobeRepository = WardrobeRepository

    init {
        Log.d("AddClothesViewModel", "init")
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


    //Функция добавления одежды в гардероб пользователя
    //P.S. данные берутся также с viewModel
    fun addUserStuff(bitmap: Bitmap) {
        val token = Constants.TYPE_TOKEN + " " + ApplicationPreferences.getToken(getApplication<Application>().applicationContext)

        viewModelScope.launch {
            val file = correctSizeFile(bitmap)
            val stuffInfo = UserStuff(
                productDisplayName = liveData.value!!.titleStuff,
                articleType = liveData.value!!.categoryStuff,
                season = getServerTitleSeason(liveData.value!!.season),
                gender = liveData.value!!.gender
            )

            wardrobeRepository.addUserStuff(
                token = token,
                stuffInfo = stuffInfo,
                imageFile = file
            )
        }
    }


    //Сжатие при необходимости файла до необходимых размеров
    private fun correctSizeFile(bitmap: Bitmap): File {
        Log.d(TAG, "start correct")

        val MAX_SIZE_KB = 1024
        var options = 100
        val context = getApplication<Application>().applicationContext

        val file = File(context.cacheDir, Constants.FILE_NAME);
        file.createNewFile();

        //Convert bitmap to byte array
        var bos = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
        var bitmapdata = bos.toByteArray();

        //write the bytes in file
        var fos = FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        Log.d(TAG, "file length = ${file.length() / 1024} kb")

        options-=5
        while (file.length() / 1024 > MAX_SIZE_KB && options > 0) {
            bos = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos)
            bitmapdata = bos.toByteArray()

            //write the bytes in file
            fos = FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            options-=5

            Log.d(TAG, "$options ${file.length()}")
        }

        Log.d(TAG, "file length = ${file.length() / 1024} kb")

        return file
    }


    //Костыль из  файла WardrobeViewModel
    fun getServerTitleSeason(title: String): String {
        var result = Constants.SEASON_NAN
        val res = getApplication<Application>().resources
        val arraySeasons = res.getStringArray(R.array.seasons)

        Log.d(TAG, "title = $title")
        Log.d(TAG, "arraySeasons[0] = ${arraySeasons[0]}")

        when (title) {
            arraySeasons[0] -> result = Constants.SEASON_WINTER
            arraySeasons[1] -> result = Constants.SEASON_SPRING
            arraySeasons[2] -> result = Constants.SEASON_SUMMER
            arraySeasons[3] -> result = Constants.SEASON_AUTUMN
        }

        Log.d(TAG, "result = $result")

        return result
    }


    fun getServerGender(genderTitle: String): Gender {
        var result = ""
        var id = 0L
        val arrayGenders = getApplication<Application>().resources.getStringArray(R.array.genders)
        when(genderTitle) {
            arrayGenders[0] -> {
                result = Constants.GENDER_MAN
                id = 1L
            }
            arrayGenders[1] -> {
                result = Constants.GENDER_WOMAN
                id = 2L
            }
        }

        return Gender(id, result)
    }
}