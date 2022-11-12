package ru.dm.android.truestyle.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentCropPhotoBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.util.startLoadAnimation
import ru.dm.android.truestyle.viewmodel.CropPhotoViewModel
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


private const val TAG = "CropPhotoFragment"
private const val ARG_URI_PHOTO = "URI_PHOTO"
private const val ARG_IS_ADD_TO_WARDROBE = "IS_ADD_TO_WARDROBE" //Аргумент, передаваемый в диалоговое окно, говорящий, нужно ли открывать окно с добавлением одежды в гардероб


class CropPhotoFragment: Fragment() {

    private lateinit var viewModel: CropPhotoViewModel
    private var _binding: FragmentCropPhotoBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation
    private var isAddToWardrobe: Boolean? = null
    private var isFirstObserve = true

    // Classifier
    val IMG_SIZE: Int = 224
    //    val IMAGENET_CLASSES = arrayOf("fire", "nofire")
    val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
    val std = floatArrayOf(0.229f, 0.224f, 0.225f)
    lateinit var model: Module


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CropPhotoViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Получаем аргументы
        viewModel.liveDataPhotoUri.value = arguments?.get(ARG_URI_PHOTO) as Uri
        isAddToWardrobe = arguments?.getBoolean(ARG_IS_ADD_TO_WARDROBE)

        _binding = FragmentCropPhotoBinding.inflate(inflater, container, false)

        val root: View = binding.root
        binding.lifecycleOwner = this@CropPhotoFragment

        //Листенер для кнопки "обрезать"
        binding.buttonApply.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val cropBitmap = binding.cropView.getCropBitmap() //Выделенная область в CropView

                if (cropBitmap != null) {

                    //Запрещаем нажимать на кнопку
                    binding.buttonApply.isEnabled = false

                    //Сохраняем в файл
                    val filesDir = context?.applicationContext?.filesDir
                    val photoFile = File(filesDir, Constants.FILE_NAME)
                    val photoUri = FileProvider.getUriForFile(requireActivity(),
                        Constants.FILE_PROVIDER,
                        photoFile)

                    try {
                        var fos: FileOutputStream? = null
                        try {
                            fos = FileOutputStream(photoFile)
                            cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            Log.d(TAG, "файл сохранен")
                        } finally {
                            fos?.close()
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                    Log.d(TAG, "start analyze photo")
                    val dataClothes:String = runObjectDetection(cropBitmap!!) // Передаем объект изображения для классификации
                    Log.d(TAG, "end analyze photo")

                    //В зависимости от надобности добавлять в гардероб - шлем запрос на сервер (навигация с запросом в обсервере)
                    if (isAddToWardrobe!!) {
                        val fragmentTo = AddClothesFragment.newInstance(photoUri, dataClothes)
                        navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
                        navigation.popStack(R.id.navigation_clothes_search)
                        binding.buttonApply.isEnabled = true
                    } else {
                        viewModel.findClothes(dataClothes)
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.error_crop, Toast.LENGTH_SHORT).show()
                }
            }

        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDataPhotoUri.observe(viewLifecycleOwner, Observer {
            if (it!=null) {
                val inputStream = context?.contentResolver?.openInputStream(it)
                val bitmapImage = BitmapFactory.decodeStream(inputStream)
                binding.cropView.setImageBitmap(bitmapImage)
            }
        })

        //Слушатель на ливдату с одеждой, подобранной нейронкой
        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            if (isFirstObserve) {
                isFirstObserve = false
            } else {
                val fragmentTo = GetRecommendationFragment.newInstance(ArrayList(it))
                navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
                navigation.popStack(R.id.navigation_clothes_search)
                binding.buttonApply.isEnabled = true
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    // Определяем класс модели
    private fun runObjectDetection(bitmap: Bitmap): String {
//        dataClothes.add(getResultDetection(bitmap, "model_class.pt", 143, 254))
        //TODO: Add object detection code here
        try {
//            Log.e(TAG, "path:" + this.context?.let { assetFilePath(it, "article_tye.pt") })
            Log.d(TAG, "runObjectDetection")
            this.model = Module.load(this.context?.let { assetFilePath(it, Constants.FILE_NAME_NN) })
            val out: Int
            var result = predict(bitmap)
            val arrayCategories = resources.getStringArray(R.array.categories_stuff)
            var resultCategory = arrayCategories[result]
            Log.d(TAG, "result=$result")
            return resultCategory;
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "гг")
        }

        return ""; // Это прям херня, нужно будет исправить потом
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
        Log.d(TAG, "scores=${scores}")
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

    // Загрузка модели
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



    companion object {
        fun newInstance(uri: Uri?, isAddToWardrobe: Boolean) : CropPhotoFragment {
            val args = Bundle().apply {
                putParcelable(ARG_URI_PHOTO, uri)
                putBoolean(ARG_IS_ADD_TO_WARDROBE, isAddToWardrobe)
            }
            return CropPhotoFragment().apply {
                arguments = args
            }
        }
    }
}