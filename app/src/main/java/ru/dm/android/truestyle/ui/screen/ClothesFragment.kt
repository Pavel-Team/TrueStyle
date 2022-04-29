package ru.dm.android.truestyle.ui.screen

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.databinding.FragmentClothesBinding
import ru.dm.android.truestyle.viewmodel.ClothesViewModel

private const val TAG = "ClothesFragment"
private const val ARG_CLOTHES_ID = "clothes_id" //Константа для получения аргументов из Bundle

class ClothesFragment : Fragment() {

    private lateinit var clothesViewModel: ClothesViewModel
    private var _binding: FragmentClothesBinding? = null
    private val binding get() = _binding!!

    private var clothesId: Int? = -1
    private var width = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Достаем аргументы
        clothesId = arguments?.getInt(ARG_CLOTHES_ID, -1)

        //Получаем размеры ширины экрана
        val windowManager: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            width = windowManager.currentWindowMetrics.bounds.width()
        else
            width = windowManager.defaultDisplay.width
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clothesViewModel = ViewModelProvider(this).get(ClothesViewModel::class.java)

        //Настраиваем dataBinding
        _binding = FragmentClothesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@ClothesFragment
        binding.viewModel = clothesViewModel

        //Делаем высоту imageView равную ширине
        _binding!!.imageViewClothes.layoutParams = ViewGroup.LayoutParams(width, width)

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(clothesId: Int): ClothesFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CLOTHES_ID, clothesId)
            }
            return ClothesFragment().apply {
                arguments = args
            }
        }
    }

}