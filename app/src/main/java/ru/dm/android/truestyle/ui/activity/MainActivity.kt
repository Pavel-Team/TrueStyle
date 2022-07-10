/**Главная активити*/
package ru.dm.android.truestyle.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ActivityMainBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.*
import javax.inject.Inject


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    lateinit var navView: BottomNavigationView

    @Inject
    lateinit var navigation: Navigation


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

        navigation.setFragmentManager(supportFragmentManager)
        navigation.setNavView(navView)

        /*val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_recommendation, R.id.navigation_clothes_search, R.id.navigation_articles, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)*/
//        if (supportFragmentManager.findFragmentById(R.id.container) == null)
//            supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, RecommendationFragment()).commit()
        navView.setOnItemSelectedListener(this)

        //ApplicationPreferences.setToken(this, "")
        if (ApplicationPreferences.getToken(this).equals("")) {
            navView.selectedItemId = R.id.navigation_profile
            navView.visibility = View.GONE
            supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, LoginFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, navigation.lastFragment).commit()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy MainActivity")
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "Нажат пункт: " + item.title.toString())
        return navigation.onNavigationItemSelected(item)
    }


    override fun onBackPressed() {
        if (navigation.onBackPressed())
            finish()
    }

}