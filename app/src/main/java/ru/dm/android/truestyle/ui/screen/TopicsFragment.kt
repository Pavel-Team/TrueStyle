/**Фрагмент с темами статей*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dm.android.truestyle.databinding.FragmentTopicsBinding
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.adapter.TopicAdapter
import ru.dm.android.truestyle.viewmodel.TopicsViewModel

class TopicsFragment: Fragment() {

    private lateinit var topicsViewModel: TopicsViewModel
    private var _binding: FragmentTopicsBinding? = null
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
        topicsViewModel = ViewModelProvider(this).get(TopicsViewModel::class.java)

        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@TopicsFragment
        binding.recyclerViewTopics.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TopicAdapter(context, topicsViewModel.liveData.value!!) //ВРЕМЕННО (потом готовить аж с OnCreate)
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