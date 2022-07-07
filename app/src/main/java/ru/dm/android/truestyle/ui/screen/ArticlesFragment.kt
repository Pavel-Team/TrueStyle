/**Фрагмент с обучающими статьями*/
package ru.dm.android.truestyle.ui.screen

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentArticlesBinding
import ru.dm.android.truestyle.databinding.ItemRecommendedArticleBinding
import ru.dm.android.truestyle.model.RecommendedArticle
import ru.dm.android.truestyle.ui.activity.MainActivity
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ArticlesViewModel
import javax.inject.Inject


private const val TAG = "ArticlesFragment"
private const val DELTA_Y = 450 //Число пикселей, необходимых для слайдинга статьи

@AndroidEntryPoint
public class ArticlesFragment : Fragment()  {

    private lateinit var articlesViewModel: ArticlesViewModel
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var layoutWithCircles: LinearLayout

    private lateinit var listCircle: MutableList<ImageView>
    private var indexActiveArticle = 0 //Индекс текущего активированного кружка

    @Inject
    lateinit var navigation: Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreate")
        articlesViewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)

        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewFlipper = binding.viewFlipper
        layoutWithCircles = binding.layoutCircles
        listCircle = mutableListOf()

        setNightMode()
        var listArticles = articlesViewModel.liveData.value!! //ВРЕМЕННО
        updateViewFlipper(listArticles)
        listCircle.get(indexActiveArticle).isActivated = true //Установка кружочка

        //Слушатель для слайдинга пальцем
        var fromPosition = 0f
        var isScroll = false //Была ли проскроллена статья
        viewFlipper.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {

                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        fromPosition = event.getY();
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (!isScroll) {
                            var toPosition = event.getY()

                            if (fromPosition > toPosition && fromPosition - toPosition > DELTA_Y) {
                                showNextArticle()
                                isScroll=true
                                listCircle.get(indexActiveArticle).isActivated = false
                                indexActiveArticle =
                                    if (indexActiveArticle == listArticles.size - 1) 0 else indexActiveArticle + 1
                                listCircle.get(indexActiveArticle).isActivated = true
                                fromPosition = toPosition
                                return true
                            } else if (fromPosition < toPosition && toPosition - fromPosition > DELTA_Y) {
                                showPreviousArticle()
                                isScroll=true
                                listCircle.get(indexActiveArticle).isActivated = false
                                indexActiveArticle =
                                    if (indexActiveArticle == 0) listArticles.size - 1 else indexActiveArticle - 1
                                listCircle.get(indexActiveArticle).isActivated = true
                                fromPosition = toPosition
                                return true
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isScroll && fromPosition-event.getY()==0f) {
                            val idArticle = listArticles.get(indexActiveArticle).id
                            Log.d(TAG, "Нажата статья id = " + idArticle.toString())
                            val fragmentTo = ArticleFragment.newInstance(idArticle.toLong())
                            navigation.navigateTo(fragmentTo, R.id.navigation_articles)
                        }
                        isScroll = false
                    }
                }

                return true
            }
        })


        //Слушатель для кнопки показа всех тем
        binding.imageButtonTopics.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = TopicsFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_articles)
            }
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
        setLightMode()
    }


    //Для установки "ночной" темы
    private fun setNightMode(){
        activity!!.window.apply {
            statusBarColor = resources.getColor(R.color.black)
            navigationBarColor = resources.getColor(R.color.black)
            decorView.systemUiVisibility = View.STATUS_BAR_HIDDEN
        }

        val mainActivity = activity!! as MainActivity
        mainActivity.navView.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.black))

        val menu = mainActivity.navView.menu
        menu.getItem(0).setIcon(resources.getDrawable(R.drawable.icon_recommendation_night))
        menu.getItem(1).setIcon(resources.getDrawable(R.drawable.icon_photo_night))
        menu.getItem(2).setIcon(resources.getDrawable(R.drawable.icon_articles_checked_night))
        menu.getItem(3).setIcon(resources.getDrawable(R.drawable.icon_profile_night))
    }


    //Для установки "светлой" темы
    private fun setLightMode(){
        activity!!.window.apply {
            statusBarColor = resources.getColor(R.color.white)
            navigationBarColor = resources.getColor(R.color.white)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val mainActivity = activity!! as MainActivity
        mainActivity.navView.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))

        val menu = mainActivity.navView.menu
        menu.getItem(0).setIcon(resources.getDrawable(R.drawable.ic_recommendation))
        menu.getItem(1).setIcon(resources.getDrawable(R.drawable.ic_photo))
        menu.getItem(2).setIcon(resources.getDrawable(R.drawable.ic_articles))
        menu.getItem(3).setIcon(resources.getDrawable(R.drawable.ic_profile))
    }


    //Заполнение viewFlipper'а статьями
    private fun updateViewFlipper(listArticles: List<RecommendedArticle> ) {
        listArticles.forEach {
            val bindingArticle = ItemRecommendedArticleBinding.inflate(layoutInflater, viewFlipper, false)
            bindingArticle.model = it
            if (it.id == 2)
                bindingArticle.imageViewRecommendedArticle.setImageResource(R.drawable.example_recommended_article_2)
//            val idArticle = it.id
//            bindingArticle.root.setOnClickListener(object: View.OnClickListener {
//                override fun onClick(p0: View?) {
//                    Log.d(TAG, "Нажата статья id = " + idArticle.toString())
//                    val 
//                    val fragmentTo = ArticleFragment.newInstance(idArticle)
//                    //navigation.navigateTo(fragmentTo, R.id.navigation_articles))
//                }
//            })
            viewFlipper.addView(bindingArticle.root)

            //Добавляем кружок
            addCircle()
        }
    }


    //Добавления кружка (с выбранной статьей в слайдинге)
    private fun addCircle() {
        var imageView = ImageView(requireContext())
        imageView.setImageDrawable(resources.getDrawable(R.drawable.circle_article))
        val layoutParams = LinearLayout.LayoutParams(
            resources.getDimension(R.dimen.width_circle_article).toInt(),
            resources.getDimension(R.dimen.width_circle_article).toInt()
        )
        layoutParams.setMargins(0, 0, 0, resources.getDimension(R.dimen.width_circle_article).toInt())
        imageView.layoutParams = layoutParams

        listCircle.add(imageView)
        layoutWithCircles.addView(imageView)
    }


    private fun showPreviousArticle(){
        //+проверка
        viewFlipper.setInAnimation(requireContext(), R.anim.bottom_in)
        viewFlipper.setOutAnimation(requireContext(), R.anim.bottom_out)
        viewFlipper.showPrevious()
    }


    private fun showNextArticle(){
        //+проверка
        viewFlipper.setInAnimation(requireContext(), R.anim.top_in)
        viewFlipper.setOutAnimation(requireContext(), R.anim.top_out)
        viewFlipper.showNext()
    }

}