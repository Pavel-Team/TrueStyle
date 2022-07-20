/**Фрагмент с полученными рекомендациями после загрузки фото*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.FragmentGetRecommendationBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.adapter.GetRecommendationAdapter
import ru.dm.android.truestyle.viewmodel.GetRecommendationViewModel


private const val ARG_CLOTHES = "Clothes" //Константа для получения листа с одеждой из Bundle



class GetRecommendationFragment: Fragment() {
    private lateinit var getRecommendationViewModel: GetRecommendationViewModel
    private var _binding: FragmentGetRecommendationBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation

    private var listClothes: List<Stuff>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listClothes = arguments?.get(ARG_CLOTHES) as List<Stuff>
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getRecommendationViewModel = ViewModelProvider(this).get(GetRecommendationViewModel::class.java)
        getRecommendationViewModel.liveData.value = listClothes

        _binding = FragmentGetRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@GetRecommendationFragment
        binding.recyclerViewRecommendedClothes.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = GetRecommendationAdapter(navigation, context, getRecommendationViewModel.liveData.value!!) //ВРЕМЕННО (потом готовить аж с OnCreate)
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

        getRecommendationViewModel.liveData.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewRecommendedClothes.adapter = GetRecommendationAdapter(navigation,
                requireContext(),
                getRecommendationViewModel.liveData.value!!
            )
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(clothes: ArrayList<Stuff>): GetRecommendationFragment {
            val args = Bundle().apply {
                putParcelableArrayList(ARG_CLOTHES, clothes)
            }
            return GetRecommendationFragment().apply {
                arguments = args
            }
        }
    }
}