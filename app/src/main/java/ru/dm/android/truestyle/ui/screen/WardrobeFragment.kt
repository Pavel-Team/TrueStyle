/**Фрагмент гардероба (осень/весна/лето/зима)*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentWardrobeBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.adapter.WardrobeClothesAdapter
import ru.dm.android.truestyle.viewmodel.WardrobeViewModel
import javax.inject.Inject

private const val TAG = "WardrobeFragment"
private const val ARG_TITLE_SEASON = "titleSeason" //Константа для Bundle с названием выбранного сезона

@AndroidEntryPoint
class WardrobeFragment: Fragment() {

    private lateinit var wardrobeViewModel: WardrobeViewModel
    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigation: Navigation
    private lateinit var adapterWardrobeClothes: WardrobeClothesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        wardrobeViewModel = ViewModelProvider(this).get(WardrobeViewModel::class.java)
        wardrobeViewModel.liveDataTitleSeason.value=requireArguments().getString(ARG_TITLE_SEASON, "")

        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
        binding.viewModel = wardrobeViewModel
        val root: View = binding.root

        binding.lifecycleOwner = this@WardrobeFragment
        adapterWardrobeClothes = WardrobeClothesAdapter(navigation, requireContext())
        adapterWardrobeClothes.submitList(wardrobeViewModel.liveData.value!!) //ВРЕМЕННО (потом готовить аж с OnCreate)
        binding.recyclerViewClothes.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterWardrobeClothes
        }

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Слушатель для кнопки добавить фото
        binding.imageButtonAddClothes.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = ClothesSearchFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(titleSeason: String): WardrobeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TITLE_SEASON, titleSeason)
            }
            return WardrobeFragment().apply {
                arguments = args
            }
        }
    }
}