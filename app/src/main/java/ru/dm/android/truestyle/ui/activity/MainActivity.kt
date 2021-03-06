/**Главная активити*/
package ru.dm.android.truestyle.ui.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import okhttp3.OkHttpClient
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.ConnectionLiveData
import ru.dm.android.truestyle.api.InternetConnectionInterceptor
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.databinding.ActivityMainBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.NotConnectionDialogFragment
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.LoginFragment
import ru.dm.android.truestyle.ui.screen.RecommendationFragment


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    lateinit var navView: BottomNavigationView

    private var navigation = Navigation
    private lateinit var connectionLiveData: ConnectionLiveData


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, ApplicationPreferences.getLanguage(newBase)))
        resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        //Проверка на интернет
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, Observer {
            if (!it) {
                NotConnectionDialogFragment().apply {
                    show(supportFragmentManager, ConstantsDialog.DIALOG_NOT_CONNECTION)
                }
            }
        })

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
            navView.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment_activity_main, LoginFragment()).addToBackStack(null).commit()
        } else {
            navView.selectedItemId = Navigation.lastMenuItem
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment_activity_main, Navigation.lastFragment).addToBackStack(null).commit()
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


    //ВРЕМЕННО: метод проверки на наличие инета
    private fun hasWifi(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return (netInfo != null && netInfo.isConnectedOrConnecting)
    }

}