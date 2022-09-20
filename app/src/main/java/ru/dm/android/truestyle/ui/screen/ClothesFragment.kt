package ru.dm.android.truestyle.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.FragmentClothesBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ClothesViewModel

private const val TAG = "ClothesFragment"
private const val ARG_CLOTHES = "clothes" //Константа для получения аргументов из Bundle


class ClothesFragment : Fragment() {

    private lateinit var clothesViewModel: ClothesViewModel
    private var _binding: FragmentClothesBinding? = null
    private val binding get() = _binding!!

    private var clothes: Stuff? = Stuff()
    private var width = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Достаем аргументы
        clothes = arguments?.getParcelable(ARG_CLOTHES)

        //Получаем размеры ширины экрана
        val windowManager: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            width = windowManager.currentWindowMetrics.bounds.width()
        } else {
            width = windowManager.defaultDisplay.width
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clothesViewModel = ViewModelProvider(this).get(ClothesViewModel::class.java)
        clothesViewModel.liveData.value = clothes

        //Проверка, есть ли данная одежда в гардеробе
        clothesViewModel.checkClothesInWardrobe()

        //Настраиваем dataBinding
        _binding = FragmentClothesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@ClothesFragment
        binding.viewModel = clothesViewModel

        //Делаем высоту imageView равную ширине для вертикальной ориентации. Для горизонтальной - после создания элемента
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.imageViewClothes.layoutParams = ViewGroup.LayoutParams(width, width)
        else {
            //После рассчета берем высоту
            binding.root.post(object : Runnable {
                override fun run() {
                    val height = binding.scrollViewClothes.getChildAt(0).measuredHeight
                    binding.imageViewClothes.layoutParams = ViewGroup.LayoutParams(height, height)
                }
            })
        }

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clothesViewModel.liveDataHasInWardrobe.observe(viewLifecycleOwner, Observer {
            if (it) {
                //Меняем стиль кнопки
                binding.buttonAddWardrobe.background = resources.getDrawable(R.drawable.red_button)
                binding.buttonAddWardrobe.text = resources.getString(R.string.button_delete_from_wardrobe)

                //Листенер для кнопки "Удалить из гардероба"
                binding.buttonAddWardrobe.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        clothesViewModel.deleteClothesFromWardrobe()
                        clothesViewModel.liveDataHasInWardrobe.value = false
                        Toast.makeText(requireContext(), R.string.toast_clothes_deleted, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                //Меняем стиль кнопки
                binding.buttonAddWardrobe.background = resources.getDrawable(R.drawable.light_blue_button)
                binding.buttonAddWardrobe.text = resources.getString(R.string.button_add_to_wardrobe)

                //Листенер для кнопки "Добавить в гардероб"
                binding.buttonAddWardrobe.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        clothesViewModel.addClothesInWardrobe()
                        clothesViewModel.liveDataHasInWardrobe.value = true
                        Toast.makeText(requireContext(), R.string.toast_clothes_added, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(clothes: Stuff): ClothesFragment {
            val args = Bundle().apply {
                //putSerializable(ARG_CLOTHES_ID, clothesId)
                putParcelable(ARG_CLOTHES, clothes)
            }
            return ClothesFragment().apply {
                arguments = args
            }
        }
    }

}