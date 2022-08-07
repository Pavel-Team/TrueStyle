/**Фрагмент со статьей*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.Article
import ru.dm.android.truestyle.databinding.FragmentArticleBinding
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.ArticleViewModel

private const val TAG = "ArticleFragment"
private const val ARG_ARTICLE = "article" //Константа для получения из Bundle статьи



class ArticleFragment : Fragment() {

    private lateinit var articleViewModel: ArticleViewModel
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private var article: Article? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        article = arguments?.getParcelable(ARG_ARTICLE)
        Log.d(TAG, article.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        articleViewModel.liveData.value = article

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

        binding.webViewArticle.loadUrl(Constants.URL + article!!.url.substring(1))
        val root: View = binding.root

        binding.lifecycleOwner = this@ArticleFragment
        binding.viewModel = articleViewModel

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Фича с кнопкой "Назад"
        setScrollingWithButtonBack(binding.webViewArticle)

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.webViewArticle.webChromeClient=null
        _binding = null
    }


    //Метод установки onTouchListener на View, при скроллинге которого появляется кнока "Назад"
    fun setScrollingWithButtonBack(view: View) {
        val diffYForAnimation = 20 //Минимальное число пикселей, необходимых для активации анимации
        var fromPosition = 0f      //Стартовая позиция скролла
        var isVisibleButton = binding.imageButtonBack.visibility.equals(View.VISIBLE) //Видна ли кнока

        view.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        fromPosition = event.getY()
                        Log.d(TAG, "fromPosition = $fromPosition")
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val diffY = event.getY() - fromPosition

                        Log.d(TAG, "diffY = $diffY")
                        Log.d(tag, "isVisibleButton = $isVisibleButton")
                        //Показ кнопки "Назад"
                        if (!isVisibleButton && diffY >= diffYForAnimation) {
                            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.set_visible)
                            binding.imageButtonBack.visibility = View.VISIBLE
                            isVisibleButton = true
                            binding.imageButtonBack.startAnimation(animation)
                            fromPosition = event.getY()
                        } else if (isVisibleButton && diffY * -1 >= diffYForAnimation) {
                            //Скрытие кнопки назад
                            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.set_unvisible)
                            binding.imageButtonBack.visibility = View.INVISIBLE
                            isVisibleButton = false
                            binding.imageButtonBack.startAnimation(animation)
                            fromPosition = event.getY()
                        }
                    }
                }
                return view.onTouchEvent(event)
            }
        })
    }


    companion object {
        fun newInstance(article: Article) : ArticleFragment{
            val args = Bundle().apply {
                putParcelable(ARG_ARTICLE, article)
            }
            return ArticleFragment().apply {
                arguments = args
            }
        }
    }
}