/**Фрагмент с обучающими статьями*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentArticlesBinding
import ru.dm.android.truestyle.viewmodel.ArticlesViewModel

public class ArticlesFragment : Fragment()  {

    private lateinit var articlesViewModel: ArticlesViewModel
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articlesViewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)

        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}