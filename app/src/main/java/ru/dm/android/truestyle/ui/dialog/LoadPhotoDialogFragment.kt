/**Диалоговое окно со способом загрузки фото (из камеры или из галереи)*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentLoadPhotoBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.AddClothesFragment
import ru.dm.android.truestyle.ui.screen.CropPhotoFragment
import ru.dm.android.truestyle.ui.screen.GetRecommendationFragment
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.ClothesSearchViewModel
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


private const val TAG = "LoadPhotoDialogFragment"
private const val ARG_IS_ADD_TO_WARDROBE = "IS_ADD_TO_WARDROBE" //Аргумент, передаваемый в диалоговое окно, говорящий, нужно ли открывать окно с добавлением одежды в гардероб


class LoadPhotoDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentLoadPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ClothesSearchViewModel

    private var isAddToWardrobe: Boolean? = null //Нужно ли открывать окно с добавлением одежды в гардероб после загрузки фото

    private lateinit var photoFile: File //Файл с выбранной фотографией
    private lateinit var photoUri: Uri   //URI файла
    private var isFirstObserve = true
    private var isFirstObserveAntiBug = true

    private val navigation = Navigation
    //private var bitmapImage: Bitmap? = null


    //Обработчики результата активити с камерой и галереей
    private var resultLauncherCamera: ActivityResultLauncher<Intent>? = null
    private var resultLauncherGallery : ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        viewModel = ViewModelProvider(targetFragment!!).get(ClothesSearchViewModel::class.java)

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
                Log.d(TAG, "end decode file")

                cropPhoto(photoUri, isAddToWardrobe!!)
                //Забираем разрешения и удаляем фото (УДАЛЕНИЕ СДЕЛАТЬ ПОСЛЕ УДАЧНОЙ ОТПРАВКИ)
                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        }

        resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "Выбрать из галереи")
            Log.d(TAG, result.resultCode.toString())
            if (result.resultCode == Activity.RESULT_OK) {

                Log.d(TAG, "***")
                Log.d(TAG, "data data = " + result.data!!.data!!.toString())
                Log.d(TAG, "***")

                cropPhoto(result.data!!.data!!, isAddToWardrobe!!)
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentLoadPhotoBinding.inflate(requireActivity().layoutInflater)

        //Получаем аргументы
        isAddToWardrobe = arguments?.getBoolean(ARG_IS_ADD_TO_WARDROBE)

        //Обработчик кнопки "Сделать фото"
        binding.layoutMakePhoto.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Log.d(TAG, "onClick makePhoto")
                makePhoto()
            }
        })

        //Обработчик для кнопки "Выбрать из галереи"
        binding.layoutLoadPhoto.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Log.d(TAG, "onClick loadPhoto")
                loadPhotoFromGallery()
            }
        })

        Log.d(TAG, "onCreateDialog")
        Log.d(TAG, isFirstObserve.toString())
        Log.d(TAG, isFirstObserveAntiBug.toString())

        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        return dialog
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        isFirstObserve = true
        isFirstObserveAntiBug = true
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
        photoFile = File(filesDir, Constants.FILE_NAME)
        photoUri = FileProvider.getUriForFile(requireActivity(),
            Constants.FILE_PROVIDER,
            photoFile)

        Log.d(TAG, "########")
        Log.d(TAG, "photoFile.path = " + photoFile.path)
        Log.d(TAG, "photoUri.path = " + photoUri.path)
        Log.d(TAG, "########")

        Log.d(TAG, "***")
        Log.d(TAG, "photoUri = " + photoUri.toString())
        Log.d(TAG, "***")

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


    //Метод для обрезки фото
    private fun cropPhoto(imageUri: Uri, isAddToWardrobe: Boolean) {
        Toast.makeText(requireContext(), R.string.crop_photo, Toast.LENGTH_SHORT).show()

        val fragmentTo = CropPhotoFragment.newInstance(imageUri, isAddToWardrobe)
        navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
        dismiss()
    }



    companion object {
        fun newInstance(isAddToWardrobe: Boolean): LoadPhotoDialogFragment {
            val args = Bundle().apply {
                putBoolean(ARG_IS_ADD_TO_WARDROBE, isAddToWardrobe)
            }
            return LoadPhotoDialogFragment().apply {
                arguments = args
            }
        }
    }

}