/**Фрагмент со статьей*/
package ru.dm.android.truestyle.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.databinding.FragmentArticleBinding
import ru.dm.android.truestyle.viewmodel.ArticleViewModel

private const val TAG = "ArticleFragment"
private const val ARG_ID_ARTICLE = "idArticle" //Константа для получения из Bundle id статьи


@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private lateinit var articleViewModel: ArticleViewModel
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private var idArticle: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idArticle = arguments?.getInt(ARG_ID_ARTICLE)
        Log.d(TAG, idArticle.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        articleViewModel.loadArticle(idArticle)

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        binding.webViewArticle.webChromeClient = object : WebChromeClient() { //webChromeClient - для обработки событий
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    Log.d(TAG, "MAX PROGRESS")
                    binding.progressBarArticle.visibility = View.GONE
                } else {
                    Log.d(TAG, "PROGRESS = $newProgress")
                    binding.progressBarArticle.visibility = View.VISIBLE
                    binding.progressBarArticle.progress = newProgress
                }
            }
        }
        val root: View = binding.root

        binding.lifecycleOwner = this@ArticleFragment
        binding.viewModel = articleViewModel

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.webViewArticle.webChromeClient=null
        _binding = null
    }


    companion object {
        fun newInstance(idArticle: Long) : ArticleFragment{
            val args = Bundle().apply {
                putSerializable(ARG_ID_ARTICLE, idArticle)
            }
            return ArticleFragment().apply {
                arguments = args
            }
        }
    }
}