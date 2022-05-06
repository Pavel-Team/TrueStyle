/**Главная активити*/
package ru.dm.android.truestyle.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ActivityMainBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.ui.screen.ArticlesFragment
import ru.dm.android.truestyle.ui.screen.ClothesSearchFragment
import ru.dm.android.truestyle.ui.screen.ProfileFragment
import ru.dm.android.truestyle.ui.screen.RecommendationFragment
import java.util.*
import kotlin.collections.HashMap


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, NavigationCallbacks{

    private lateinit var binding: ActivityMainBinding
    lateinit var navView: BottomNavigationView

    //private var customNavigation: CustomNavigation = CustomNavigation()
    private var mapStackFragments: HashMap<Int, Stack<Fragment>> = HashMap() //<id выбранного пункта меню, стэк фрагментов>
    private var lastMenuItem: Int = R.id.navigation_recommendation           //Последний выбранный пункт меню

    init {
        //Инициализация стэков фрагментов для каждого пункта меню
        mapStackFragments.put(R.id.navigation_recommendation, Stack())
        mapStackFragments.put(R.id.navigation_clothes_search, Stack())
        mapStackFragments.put(R.id.navigation_articles, Stack())
        mapStackFragments.put(R.id.navigation_profile, Stack())
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, ApplicationPreferences.getLanguage(newBase)))
        resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navView.itemIconTintList = null //Чтобы не накладывался цвет из темы
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED //Для того, чтобы не отображался текст

        /*val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_recommendation, R.id.navigation_clothes_search, R.id.navigation_articles, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)*/
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, RecommendationFragment()).commit()//ВРЕМЕННО (чтобы был не navhostfragment, а обычный фрагмент с рекомендациями)
        navView.setOnItemSelectedListener(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy MainActivity")
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "Нажат пункт: " + item.title.toString())
        //Если нажат тот же пункт меню - ничего не делаем
        if (item.itemId != lastMenuItem) {
            //Добавляем в стек прошлый фрагмент
            mapStackFragments.get(lastMenuItem)?.push(supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main))
            //Запускаем новый фрагмент (если в стеке что-то лежало до этого в выбранном пункте меню - запускаем его, иначе - по дефолту)
            if (mapStackFragments.get(item.itemId)!!.empty()) {
                var newFragment: Fragment? = null
                when (item.itemId) {
                    R.id.navigation_recommendation -> { newFragment = RecommendationFragment() }
                    R.id.navigation_clothes_search -> { newFragment = ClothesSearchFragment() }
                    R.id.navigation_articles -> { newFragment = ArticlesFragment() }
                    R.id.navigation_profile -> { newFragment = ProfileFragment() }
                }
                if (newFragment != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, newFragment)
                        .commit()
                }
            } else {
                //Последний фрагмент из стека
                val fragmentFromStack = mapStackFragments.get(item.itemId)!!.pop()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragmentFromStack)
                    .commit()
            }
            lastMenuItem = item.itemId
        }
        return true
    }

    override fun navigateTo(toFragment: Fragment, idItemMenu: Int) {
        //Добавляем в соотвествующий стек и переходим к toFragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        mapStackFragments.get(lastMenuItem)!!.push(currentFragment)

        //На случай если надо запустить фрагмент в другой вкладке (не равной lastItem)
        navView.menu.findItem(idItemMenu).isChecked=true
        navView.menu.findItem(idItemMenu).isChecked=false

        lastMenuItem = idItemMenu
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, toFragment)
            .commit()
    }


    override fun clearStackFragment(idItemMenu: Int) {
        while (!mapStackFragments.get(idItemMenu)!!.isEmpty())
            mapStackFragments.get(idItemMenu)!!.pop()
    }


    override fun onBackPressed() {
        if (mapStackFragments.get(lastMenuItem)!!.isEmpty())
            finish()
        else {
            val lastFragment = mapStackFragments.get(lastMenuItem)!!.pop()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, lastFragment)
                .commit()
        }
    }

}