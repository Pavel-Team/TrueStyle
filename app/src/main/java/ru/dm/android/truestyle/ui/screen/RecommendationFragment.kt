/**Фрагмент первой страницы с рекомендациями*/
package ru.dm.android.truestyle.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.FragmentRecommendationBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.adapter.ArticleRecommendationAdapter
import ru.dm.android.truestyle.ui.screen.adapter.ClothesRecommendationAdapter
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.RecommendationViewModel


private const val TAG = "RecommendationFragment"


class RecommendationFragment : Fragment() {

    private lateinit var recommendationViewModel: RecommendationViewModel
    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        recommendationViewModel = ViewModelProvider(this).get(RecommendationViewModel::class.java)

        //Настраиваем dataBinding
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@RecommendationFragment
        binding.viewModel = recommendationViewModel

        //Настраиваем RecyclerView с рекомендациями для одежды и статей
        binding.clothesRecommendationRecyclerView.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ClothesRecommendationAdapter(context, listOf(Stuff(), Stuff(), Stuff())) //ВРЕМЕННО (потом готовить аж с OnCreate)
        }
        binding.articlesRecommendationRecyclerView.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ArticleRecommendationAdapter(context, listOf(Article(), Article(), Article())) //ВРЕМЕННО
        }

        //Слушатель кнопки "Добавить фото"
        binding.makePhotoImageButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Log.d(TAG, "onClick")
                val fragmentTo = ClothesSearchFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_clothes_search)
                navigation.clearStackFragment(R.id.navigation_clothes_search)
            }
        })

        //Слушатель кнопки "Подписаться"
        binding.subscribe.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intentTg = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.TELEGRAM_CHANEL)
                )
                startActivity(intentTg)
            }
        })

        //Слушатель кнопки "Оценить"
        binding.estimate.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.LINK_IN_PLAY_MARKET)
                startActivity(intent)
            }
        })

        //Слушатель кнопки "Техподдержка"
        binding.techSupport.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = TechnicalSupportFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_recommendation)
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recommendationViewModel.liveDataClothes.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observe liveDataClothes")
            binding.clothesRecommendationRecyclerView.adapter = ClothesRecommendationAdapter(requireContext(), recommendationViewModel.liveDataClothes.value.orEmpty())
        })

        recommendationViewModel.liveDataArticles.observe(viewLifecycleOwner, Observer {
            binding.articlesRecommendationRecyclerView.adapter = ArticleRecommendationAdapter(requireContext(), recommendationViewModel.liveDataArticles.value.orEmpty())
        })

        recommendationViewModel.liveDataValidToken.observe(viewLifecycleOwner, Observer {
            if (!it) {
                ApplicationPreferences.setToken(requireContext(), "")

                val fragmentTo = LoginFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
                navigation.initNewState()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}