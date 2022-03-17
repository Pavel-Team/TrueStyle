/**Фрагмент страницы с поиском одежды с помощью компьютерного зрения*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.databinding.FragmentClothesSearchBinding
import ru.dm.android.truestyle.viewmodel.ClothesSearchViewModel

class ClothesSearchFragment : Fragment() {

    private lateinit var clothesSearchViewModel: ClothesSearchViewModel
    private var _binding: FragmentClothesSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clothesSearchViewModel = ViewModelProvider(this).get(ClothesSearchViewModel::class.java)

        _binding = FragmentClothesSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}