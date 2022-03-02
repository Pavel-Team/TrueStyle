/**Фрагмент первой страницы с рекомендациями*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentArticlesBinding
import ru.dm.android.truestyle.databinding.FragmentRecommendationBinding
import ru.dm.android.truestyle.viewmodel.ArticlesViewModel
import ru.dm.android.truestyle.viewmodel.RecommendationViewModel

public class RecommendationFragment : Fragment() {

    private lateinit var recommendationViewModel: RecommendationViewModel
    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recommendationViewModel = ViewModelProvider(this).get(RecommendationViewModel::class.java)

        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}