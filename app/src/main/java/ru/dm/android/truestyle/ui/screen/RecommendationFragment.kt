/**Фрагмент первой страницы с рекомендациями*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentArticlesBinding
import ru.dm.android.truestyle.databinding.FragmentRecommendationBinding
import ru.dm.android.truestyle.model.Clothes
import ru.dm.android.truestyle.ui.screen.adapter.ArticleRecommendationAdapter
import ru.dm.android.truestyle.ui.screen.adapter.ClothesRecommendationAdapter
import ru.dm.android.truestyle.viewmodel.ArticlesViewModel
import ru.dm.android.truestyle.viewmodel.RecommendationViewModel

private const val TAG = "RecommendationFragment"

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

        //Настраиваем dataBinding
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.lifecycleOwner = this@RecommendationFragment
        _binding!!.viewModel = recommendationViewModel

        //Настраиваем RecyclerView с рекомендациями для одежды и статей
        _binding!!.clothesRecommendationRecyclerView.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ClothesRecommendationAdapter(context, recommendationViewModel.liveDataClothes.value!!) //ВРЕМЕННО
        }
        _binding!!.articlesRecommendationRecyclerView.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ArticleRecommendationAdapter(context, recommendationViewModel.liveDataArticles.value!!) //ВРЕМЕННО
        }

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}