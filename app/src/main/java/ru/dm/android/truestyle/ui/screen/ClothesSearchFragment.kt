/**Фрагмент страницы с поиском одежды с помощью компьютерного зрения*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.databinding.FragmentClothesSearchBinding
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.LoadPhotoDialogFragment
import ru.dm.android.truestyle.viewmodel.ClothesSearchViewModel


private const val TAG = "ClothesSearchFragment"
private const val CODE_FIND_SIMILAR = 0    //Для таргет фрагмента к диалоговому окну загрузки фото по нажатию кнопки "Найти похожее"
private const val CODE_ADD_TO_WARDROBE = 1 //Для таргет фрагмента к диалоговому окну загрузки фото по нажатию кнопки "Добавить в гардероб"


class ClothesSearchFragment : Fragment() {

    private lateinit var clothesSearchViewModel: ClothesSearchViewModel
    private var _binding: FragmentClothesSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        clothesSearchViewModel = ViewModelProvider(this).get(ClothesSearchViewModel::class.java)
        Log.d(TAG, clothesSearchViewModel.toString())

        _binding = FragmentClothesSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Обработчик кнопки "Найти похожее"
        binding.buttonFindSimilar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                LoadPhotoDialogFragment.newInstance(
                    false
                ).apply {
                    setTargetFragment(this@ClothesSearchFragment, CODE_FIND_SIMILAR)
                    show(this@ClothesSearchFragment.parentFragmentManager, ConstantsDialog.DIALOG_LOAD_PHOTO)
                }
            }
        })

        //Обработчик для кнопки "Добавить в гардероб"
        binding.buttonAddWardrobe.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                LoadPhotoDialogFragment.newInstance(
                    true
                ).apply {
                    setTargetFragment(this@ClothesSearchFragment, CODE_ADD_TO_WARDROBE)
                    show(this@ClothesSearchFragment.parentFragmentManager, ConstantsDialog.DIALOG_LOAD_PHOTO)
                }
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "destroy")
        _binding = null
    }
}