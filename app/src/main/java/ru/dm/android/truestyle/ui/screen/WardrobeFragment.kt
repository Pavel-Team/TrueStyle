/**Фрагмент гардероба (осень/весна/лето/зима)*/
package ru.dm.android.truestyle.ui.screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Stuff
import ru.dm.android.truestyle.databinding.FragmentWardrobeBinding
import ru.dm.android.truestyle.databinding.ItemClothesInWardrobeBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
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
        wardrobeViewModel.loadWardrobe()

        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
        binding.viewModel = wardrobeViewModel
        val root: View = binding.root

        binding.lifecycleOwner = this@WardrobeFragment
        adapterWardrobeClothes = WardrobeClothesAdapter(requireContext())
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wardrobeViewModel.liveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, wardrobeViewModel.liveData.value.toString())
            adapterWardrobeClothes.submitList(wardrobeViewModel.liveData.value!!)
        })
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



    /**Класс-адаптер для одежды*/
    private inner class WardrobeClothesAdapter(context: Context) : ListAdapter<Stuff, WardrobeClothesHolder>(DiffCallbackStuff())  {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeClothesHolder {
            val binding = DataBindingUtil.inflate<ItemClothesInWardrobeBinding>(
                LayoutInflater.from(context),
                R.layout.item_clothes_in_wardrobe,
                parent,
                false
            )
            return WardrobeClothesHolder(binding)
        }

        override fun onBindViewHolder(holder: WardrobeClothesHolder, position: Int) {
            val stuff = currentList[position]
            holder.bind(stuff)
        }

        override fun getItemCount(): Int {
            return currentList.size
        }
    }



    /**Класс для сравнения элементов списка*/
    private inner class DiffCallbackStuff : DiffUtil.ItemCallback<Stuff>() {

        override fun areItemsTheSame(oldItem: Stuff, newItem: Stuff) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Stuff, newItem: Stuff) =
            oldItem == newItem
    }



    /**Класс одного элемента списка*/
    private inner class WardrobeClothesHolder(private val binding: ItemClothesInWardrobeBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener  {

        init {
            itemView.setOnClickListener(this)
            binding.imageViewDeleteWardrobe.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    Log.d(TAG, "onClick")
                    wardrobeViewModel.deleteClothes(binding.model!!.id)
                }
            })
        }

        fun bind(stuff: Stuff) {
            binding.apply {
                model = stuff
                executePendingBindings()
            }
        }

        override fun onClick(view: View?) {
            val clothes = binding.model!!

            val fragmentTo = ClothesFragment.newInstance(clothes)
            navigation.navigateTo(fragmentTo, R.id.navigation_profile)
        }
    }
}