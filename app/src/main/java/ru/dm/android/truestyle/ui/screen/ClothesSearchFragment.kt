/**Фрагмент страницы с поиском одежды с помощью компьютерного зрения*/
package ru.dm.android.truestyle.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.AssetManager
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentClothesSearchBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ClothesSearchViewModel
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


private const val TAG = "ClothesSearchFragment"
private const val FILE_NAME = "temporaryPhoto.jpg" //Название временно сохраненного файла
private const val FILE_PROVIDER = "ru.dm.android.truestyle.fileprovider" //Путь до хранилища file-provider'а (указан в манифесте)


class ClothesSearchFragment : Fragment() {

    private lateinit var clothesSearchViewModel: ClothesSearchViewModel
    private var _binding: FragmentClothesSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var photoFile: File //Файл с выбранной фотографией
    private lateinit var photoUri: Uri   //URI файла
    private var isFirstObserve = true

    // Classifier
    val IMG_SIZE: Int = 224
//    val IMAGENET_CLASSES = arrayOf("fire", "nofire")
    val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
    val std = floatArrayOf(0.229f, 0.224f, 0.225f)
    lateinit var model: Module

    private val navigation = Navigation


    //Обработчики результата активити с камерой и галереей
    private var resultLauncherCamera: ActivityResultLauncher<Intent>? = null
    private var resultLauncherGallery : ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        resultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "Сделать снимок")
            Log.d(TAG, result.resultCode.toString())
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
                val dataClothes:Int = runObjectDetection(bitmap) // Передаем объект изображения для классификации
//                Log.d(TAG, "res:" +dataClothes.toString())
                Log.d(TAG, "end analyze photo")
                clothesSearchViewModel.findClothes(dataClothes)


                //Наш файл для передачи лежит в photoFile
                //...отправка на сервер

                //Забираем разрешения и удаляем фото (УДАЛЕНИЕ СДЕЛАТЬ ПОСЛЕ УДАЧНОЙ ОТПРАВКИ)
                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                photoFile.delete()
            }
        }

        resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "Выбрать из галереи")
            Log.d(TAG, result.resultCode.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val inputStream  = context?.contentResolver?.openInputStream(result.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                Log.d(TAG, "start analyze photo")
                val dataClothes:Int = runObjectDetection(bitmap) // Передаем объект изображения для классификации

                Log.d(TAG, "end analyze photo")
                clothesSearchViewModel.findClothes(dataClothes)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        clothesSearchViewModel = ViewModelProvider(this).get(ClothesSearchViewModel::class.java)

        _binding = FragmentClothesSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Обработчик кнопки "Сделать фото"
        binding.buttonMakePhoto.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Log.d(TAG, "onClick makePhoto")
                makePhoto()
            }
        })

        //Обработчик для кнопки "Выбрать из галереи"
        binding.buttonLoadPhoto.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Log.d(TAG, "onClick loadPhoto")
                loadPhotoFromGallery()
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isFirstObserveAntiBug = true
        if (isFirstObserve)
            isFirstObserveAntiBug = false

        clothesSearchViewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (isFirstObserveAntiBug) {
                isFirstObserveAntiBug = false
            } else {
                val fragmentTo = GetRecommendationFragment.newInstance(ArrayList(it))
                navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
                isFirstObserve = false
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "destroy")
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

        Log.d(TAG, "resultCamera " + resultLauncherCamera.toString())
        resultLauncherCamera?.launch(captureImage)
    }


    //Функция загрузки фото из галереи
    private fun loadPhotoFromGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = "image/*"

        Log.d(TAG, "resultGallery " + resultLauncherGallery.toString())
        resultLauncherGallery?.launch(pickIntent)
    }


    // Определяем класс модели
    private fun runObjectDetection(bitmap: Bitmap): Int {
//        dataClothes.add(getResultDetection(bitmap, "model_class.pt", 143, 254))
        //TODO: Add object detection code here
        var result:Int = 0
        try {
//            Log.e(TAG, "path:" + this.context?.let { assetFilePath(it, "article_tye.pt") })
            this.model = Module.load(this.context?.let { assetFilePath(it, "article_type.pt") })
            val out: Int
            result = predict(bitmap)
            return result;
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "гг")
        }

        return 0; // Это прям херня, нужно будет исправить потом
    }

    // приведение размера картинки и конвертация ее в тензор
    fun preprocess(bitmap: Bitmap, size: Int): Tensor {
        var bitmap = bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap, mean, std)
    }

    // найти номер максимального элемента в массиве
    fun argMax(inputs: FloatArray): Int {
        var maxIndex = -1
        var maxvalue = -1000000000.0f
        for (i in inputs.indices) {
            if (inputs[i] > maxvalue) {
                maxIndex = i
                maxvalue = inputs[i]
            }
        }
        return maxIndex
    }

    // использование НС
    fun predict(bitmap: Bitmap): Int {
        val tensor = preprocess(bitmap, IMG_SIZE)
        val inputs = IValue.from(tensor)
        val outputs = model.forward(inputs).toTensor()
        val scores = outputs.dataAsFloatArray
        val classIndex = argMax(scores)
        return classIndex
    }


//    // Оптимизация изображения для модели
//    private fun convertBitmapToByteBuffer(bp: Bitmap, sizeImage: Int): ByteBuffer? {
//        val imgData: ByteBuffer = ByteBuffer.allocateDirect(java.lang.Float.BYTES * sizeImage * sizeImage * 3)
//        imgData.order(ByteOrder.nativeOrder())
//        val bitmap = Bitmap.createScaledBitmap(bp, sizeImage, sizeImage, true)
//        val intValues = IntArray(sizeImage * sizeImage)
//
//        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//
//        // Convert the image to floating point.
//        var pixel = 0
//        for (i in 0 until sizeImage) {
//            for (j in 0 until sizeImage) {
//                val `val` = intValues[pixel++]
//                imgData.putFloat((`val` shr 16 and 0xFF) / 255f)
//                imgData.putFloat((`val` shr 8 and 0xFF) / 255f)
//                imgData.putFloat((`val` and 0xFF) / 255f)
//            }
//        }
//        return imgData
//    }


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

    fun assetFilePath(context: Context, asset: String): String {
        val file = File(context.filesDir, asset)

        try {
            val inpStream: InputStream = context.assets.open(asset)
            try {
                val outStream = FileOutputStream(file, false)
                val buffer = ByteArray(4 * 1024)
                var read: Int

                while (true) {
                    read = inpStream.read(buffer)
                    if (read == -1) {
                        break
                    }
                    outStream.write(buffer, 0, read)
                }
                outStream.flush()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}