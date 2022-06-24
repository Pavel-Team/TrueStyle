/**Фрагмент страницы с поиском одежды с помощью компьютерного зрения*/
package ru.dm.android.truestyle.ui.screen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentClothesSearchBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ClothesSearchViewModel
import java.io.File
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

    @Inject
    lateinit var navigation: Navigation


    //Обработчики результата активити с камерой и галереей
    var resultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            /*//Для получения миниатюры:
            val bitmap: Bitmap? = data?.extras?.getParcelable("data") as Bitmap?
            Log.d(TAG, bitmap?.width.toString())*/

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