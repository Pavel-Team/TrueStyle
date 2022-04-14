/**Фрагмент с обучающими статьями*/
package ru.dm.android.truestyle.ui.screen

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentArticlesBinding
import ru.dm.android.truestyle.databinding.ItemRecommendedArticleBinding
import ru.dm.android.truestyle.model.RecommendedArticle
import ru.dm.android.truestyle.ui.activity.MainActivity
import ru.dm.android.truestyle.viewmodel.ArticlesViewModel


private const val TAG = "ArticlesFragment"
private const val DELTA_Y = 600 //Число пикселей, необходимых для слайдинга статьи

public class ArticlesFragment : Fragment()  {

    private lateinit var articlesViewModel: ArticlesViewModel
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var layoutWithCircles: LinearLayout

    private lateinit var listCircle: MutableList<ImageView>
    private var indexActiveArticle = 0 //Индекс текущего активированного кружка


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articlesViewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)

        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewFlipper = binding.viewFlipper
        layoutWithCircles = binding.layoutCircles
        listCircle = mutableListOf()

        setNightMode()
        var listArticles = articlesViewModel.liveData.value!! //ВРЕМЕННО
        updateViewFlipper(listArticles)
        listCircle.get(0).isActivated = true

        //Слушатель для слайдинга пальцем
        var fromPosition = 0f
        viewFlipper.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {

                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        fromPosition = event.getY();
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var toPosition = event.getY()

                        if(fromPosition > toPosition && fromPosition - toPosition > DELTA_Y) {
                            showNextArticle()
                            listCircle.get(indexActiveArticle).isActivated=false
                            indexActiveArticle = if (indexActiveArticle == listArticles.size-1) 0 else indexActiveArticle+1
                            listCircle.get(indexActiveArticle).isActivated=true
                            fromPosition = toPosition
                            return true
                        } else if (fromPosition < toPosition && toPosition - fromPosition > DELTA_Y) {
                            showPreviousArticle()
                            listCircle.get(indexActiveArticle).isActivated=false
                            indexActiveArticle = if (indexActiveArticle == 0) listArticles.size-1 else indexActiveArticle-1
                            listCircle.get(indexActiveArticle).isActivated=true
                            fromPosition = toPosition
                            return true
                        }
                    }
                }

                return true
            }
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
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