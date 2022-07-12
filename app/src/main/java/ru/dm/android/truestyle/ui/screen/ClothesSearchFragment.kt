/**Фрагмент страницы с поиском одежды с помощью компьютерного зрения*/
package ru.dm.android.truestyle.ui.screen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.Interpreter
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentClothesSearchBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ClothesSearchViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject

private const val TAG = "ClothesSearchFragment"
private const val FILE_NAME = "temporaryPhoto.jpg" //Название временно сохраненного файла
private const val FILE_PROVIDER = "ru.dm.android.truestyle.fileprovider" //Путь до хранилища file-provider'а (указан в манифесте)

@AndroidEntryPoint
class ClothesSearchFragment : Fragment() {

    private lateinit var clothesSearchViewModel: ClothesSearchViewModel
    private var _binding: FragmentClothesSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var photoFile: File //Файл с выбранной фотографией
    private lateinit var photoUri: Uri   //URI файла
    protected var tflite: Interpreter? = null

    @Inject
    lateinit var navigation: Navigation


    //Обработчики результата активити с камерой и галереей
    var resultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            /*//Для получения миниатюры:
            val bitmap: Bitmap? = data?.extras?.getParcelable("data") as Bitmap?
            Log.d(TAG, bitmap?.width.toString())*/

            // Получаем изображение и получаем его Bitmap
            Log.d(TAG, "start decode file")
            val filePath: String = photoFile.getPath()
            val bitmap: Bitmap = BitmapFactory.decodeFile(filePath)
            Log.d(TAG, "end decode file")

            Log.d(TAG, "start analyze photo")
            val dataClothes:List<Int> = runObjectDetection(bitmap) // Передаем объект изображения для классификации
            Log.d(TAG, "end analyze photo")
            clothesSearchViewModel.findClothes(dataClothes)

            //Наш файл для передачи лежит в photoFile
            //...отправка на сервер

            //Забираем разрешения и удаляем фото (УДАЛЕНИЕ СДЕЛАТЬ ПОСЛЕ УДАЧНОЙ ОТПРАВКИ)
            requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            photoFile.delete()
        }
    }
    var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val inputStream  = context?.contentResolver?.openInputStream(result.data!!.data!!)
            //Отправляем на сервер

            //ВРЕМЕННО ДЛЯ ВИДОСА
            val fragmentTo = GetRecommendationFragment()
            navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
        }
    }


    // Определяем класс модели
    private fun runObjectDetection(bitmap: Bitmap):List<Int> {
        val modelsFirstPart:Map<String, Int> = mapOf("model_class" to 143, "model_color" to 47, "model_gender" to 5)
        val modelsSecondPart:Map<String, Int> = mapOf("model_masterCategory" to 7, "model_season" to 5, "model_subCategory" to 45)
        var dataClothes: ArrayList<Int> = ArrayList<Int>()

        // Прогоняем все модели
        for ((model, value) in modelsFirstPart){
            dataClothes.add(getResultDetection(bitmap, model, value, 56))
        }
        for ((model, value) in modelsSecondPart){
            dataClothes.add(getResultDetection(bitmap, model, value, 28))
        }
//        Log.d(TAG, "-----")
//        for (i in 0 until 6){
//            Log.d(TAG,  dataClothes[i].toString())
//        }
        return dataClothes;
    }

    // Получение результата для одной модели
    private fun getResultDetection(bitmap:Bitmap, model:String, value:Int, sizeImage: Int): Int{
        //TODO: Add object detection code here
        try {
            tflite = activity?.let { loadModelFile(it, model + ".tflite")?.let { Interpreter(it) } }
            val out = arrayOf(FloatArray(value))
            tflite!!.run(convertBitmapToByteBuffer(bitmap, sizeImage), out)
            val result: Int = out[0].indices.maxByOrNull { out[0][it] } ?: -1 ;
            return result;
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "гг")
        }
        return 0; // Это прям херня, нужно будет исправить потом
    }

    // Оптимизация изображения для модели
    private fun convertBitmapToByteBuffer(bp: Bitmap, sizeImage: Int): ByteBuffer? {
        val imgData: ByteBuffer = ByteBuffer.allocateDirect(java.lang.Float.BYTES * sizeImage * sizeImage * 3)
        imgData.order(ByteOrder.nativeOrder())
        val bitmap = Bitmap.createScaledBitmap(bp, sizeImage, sizeImage, true)
        val intValues = IntArray(sizeImage * sizeImage)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // Convert the image to floating point.
        var pixel = 0
        for (i in 0 until sizeImage) {
            for (j in 0 until sizeImage) {
                val `val` = intValues[pixel++]
                imgData.putFloat((`val` shr 16 and 0xFF) / 255f)
                imgData.putFloat((`val` shr 8 and 0xFF) / 255f)
                imgData.putFloat((`val` and 0xFF) / 255f)
            }
        }
        return imgData
    }


    //Memory-map the model file in Assets.
    // Загрузка модели из assets
    @Throws(IOException::class)
    private fun loadModelFile(activity: FragmentActivity, filename:String): MappedByteBuffer? {
        val fileDescriptor = activity.assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clothesSearchViewModel = ViewModelProvider(this).get(ClothesSearchViewModel::class.java)

        _binding = FragmentClothesSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Обработчик кнопки "Сделать фото"
        binding.buttonMakePhoto.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                makePhoto()
            }
        })

        //Обработчик для кнопки "Выбрать из галереи"
        binding.buttonLoadPhoto.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                loadPhotoFromGallery()
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Функция вызова камеры и деланья снимка
    private fun makePhoto() {
        //Проверка на наличие камеры
        val packageManager: PackageManager = requireActivity().packageManager
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolvedActivity == null) {
            Toast.makeText(
                context,
                resources.getString(R.string.not_camera),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        //Временный файл с фото
        val filesDir = context?.applicationContext?.filesDir
        photoFile = File(filesDir, FILE_NAME)
        photoUri = FileProvider.getUriForFile(requireActivity(),
            FILE_PROVIDER,
            photoFile)

        //Указываем, куда сохранять фото
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        //Получаем все активити, способные выполнить данный интент
        val cameraActivities: List<ResolveInfo> =
            packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)

        //Выдаем им разрещения для записи только в укзаанный photoUri
        for (cameraActivity in cameraActivities) {
            requireActivity().grantUriPermission(
                cameraActivity.activityInfo.packageName,
                photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }

        resultLauncherCamera.launch(captureImage)
    }


    //Функция загрузки фото из галереи
    private fun loadPhotoFromGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = "image/*"

        resultLauncherGallery.launch(pickIntent)
    }
}