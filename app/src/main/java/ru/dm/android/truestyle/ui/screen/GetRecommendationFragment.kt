/**Фрагмент с полученными рекомендациями после загрузки фото*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dm.android.truestyle.databinding.FragmentGetRecommendationBinding
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.adapter.GetRecommendationAdapter
import ru.dm.android.truestyle.viewmodel.GetRecommendationViewModel


class GetRecommendationFragment: Fragment() {
    private lateinit var getRecommendationViewModel: GetRecommendationViewModel
    private var _binding: FragmentGetRecommendationBinding? = null
    private val binding get() = _binding!!

    private lateinit var callbacks: NavigationCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbacks = context as NavigationCallbacks
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getRecommendationViewModel = ViewModelProvider(this).get(GetRecommendationViewModel::class.java)

        _binding = FragmentGetRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@GetRecommendationFragment
        binding.recyclerViewRecommendedClothes.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = GetRecommendationAdapter(context, getRecommendationViewModel.liveData.value!!) //ВРЕМЕННО (потом готовить аж с OnCreate)
        }

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}