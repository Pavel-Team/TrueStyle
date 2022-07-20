/**Фрагмент со статьями в заданной тематике (открывается после выбора темы из TopicsFragment)*/
package ru.dm.android.truestyle.ui.screen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dm.android.truestyle.databinding.FragmentArticlesInTopicBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.adapter.ArticlesInTopicAdapter
import ru.dm.android.truestyle.viewmodel.ArticlesInTopicViewModel
import java.util.*

private const val TAG = "ArticlesInTopic"
private const val ARG_TITLE_TOPIC = "titleTopic" //Константа для Bundle с названием тематики статей


class ArticlesInTopicFragment: Fragment() {

    private lateinit var articlesInTopicViewModel: ArticlesInTopicViewModel
    private var _binding: FragmentArticlesInTopicBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation
    private lateinit var adapterArticles: ArticlesInTopicAdapter

    private lateinit var titleTopic: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articlesInTopicViewModel = ViewModelProvider(this).get(ArticlesInTopicViewModel::class.java)
        articlesInTopicViewModel.liveDataTopic.value = requireArguments().getString(ARG_TITLE_TOPIC, "")

        _binding = FragmentArticlesInTopicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@ArticlesInTopicFragment
        adapterArticles = ArticlesInTopicAdapter(navigation, requireContext())
        adapterArticles.submitList(articlesInTopicViewModel.liveData.value) //ВРЕМЕННО (потом готовить аж с OnCreate)
        binding.recyclerViewArticles.apply{
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterArticles
        }

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                if (binding.editTextSearch.visibility==View.GONE)
                    activity?.onBackPressed()
                else
                    closeSearch()
            }
        })

        //Слушатель для кнопки открытия поиска
        binding.buttonOpenSearch.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                openSearch()
            }
        })

        //Обработка EditText
        binding.editTextSearch.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId==EditorInfo.IME_ACTION_DONE) {
                    search()
                    //Скрываем клавиатуру
                    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    return true
                }
                return false
            }
        })

        //Обработка кнопки поиска
        binding.sendSearch.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                search()
                //Скрываем клавиатуру
                val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesInTopicViewModel.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterArticles.submitList(articlesInTopicViewModel.liveData.value!!)
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Метод открытия поиска
    private fun openSearch() {
        binding.sendSearch.visibility=View.VISIBLE
        binding.editTextSearch.visibility=View.VISIBLE
        binding.titleStyle.visibility=View.GONE
        binding.buttonOpenSearch.visibility=View.GONE

        //Установка клавиатуры
        binding.editTextSearch.isFocusable=true
        binding.editTextSearch.isFocusableInTouchMode=true
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0,0)
        binding.editTextSearch.requestFocus()
    }


    //Метод закрытия поиска
    private fun closeSearch() {
        binding.sendSearch.visibility=View.GONE
        binding.editTextSearch.visibility=View.GONE
        binding.titleStyle.visibility=View.VISIBLE
        binding.buttonOpenSearch.visibility=View.VISIBLE
    }


    //Метод поиска статей
    private fun search() {
        val textForSearch = binding.editTextSearch.text.toString().lowercase(Locale.getDefault())
        Log.d(TAG, textForSearch)
        if (textForSearch.length==0)
            adapterArticles.submitList(articlesInTopicViewModel.liveData.value)
        else {
            var newList = articlesInTopicViewModel.liveData.value?.toMutableList()
            var iterator = newList?.iterator()
            while (iterator!!.hasNext())
                if (!iterator.next().title.lowercase(Locale.getDefault()).contains(textForSearch))
                    iterator.remove()
            Log.d(TAG, newList.toString())
            adapterArticles.submitList(newList)
        }
    }



    companion object {
        fun newInstance(titleTopic: String): ArticlesInTopicFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TITLE_TOPIC, titleTopic)
            }
            return ArticlesInTopicFragment().apply {
                arguments = args
            }
        }
    }

}