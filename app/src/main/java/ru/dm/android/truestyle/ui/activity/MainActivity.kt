/**Главная активити*/
package ru.dm.android.truestyle.ui.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import ru.dm.android.truestyle.BuildConfig
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.ConnectionLiveData
import ru.dm.android.truestyle.databinding.ActivityMainBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.NewVersionDialogFragment
import ru.dm.android.truestyle.ui.dialog.NotConnectionDialogFragment
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.LoginFragment
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.MainActivityViewModel
import java.util.*


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

        val viewModel: MainActivityViewModel by viewModels()

        //ВРЕМЕННО
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(!hasWifi(this)) {
            NotConnectionDialogFragment().apply {
                show(supportFragmentManager, ConstantsDialog.DIALOG_NOT_CONNECTION)
            }
        }

        //Проверка на интернет
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, Observer {
            if (!it) {
                NotConnectionDialogFragment().apply {
                    show(supportFragmentManager, ConstantsDialog.DIALOG_NOT_CONNECTION)
                }
            }
        })

        //Проверка на актуальную версию
        viewModel.checkCurrentAppVersion()
        viewModel.liveDataAppVersion.observe(this, Observer {
            Log.d(TAG, "observe appVersion")
            val versionName = BuildConfig.VERSION_NAME

            //Если не последняя актуальная версия - в зависимости от минимально допустимой, запускаем диалоговое окно с возможностью выйти или нет
            if (versionName != it.actualVersionNumber) {
                //Если еще можем пользоваться приложением - раз в сутки показываем окно с новой версией
                if (viewModel.compareVersion(versionName, it.minActualVersionNumber!!)) {
                    viewModel.liveDataCorrectAppVersion.value = true //Запускаем отрисовку фрагментов

                    val nowDate = Date().time
                    val lastDateOpenDialog = ApplicationPreferences.getDateDialogCurrentVersion(this)

                    //Если прошли сутки - показываем уведомление об возможном обновлении
                    if (nowDate - lastDateOpenDialog >= Constants.MIN_DIFF_DATE_FOR_VISIBLE_DIALOG_APP_VERSION) {
                        ApplicationPreferences.setDateDialogCurrentVersion(this, nowDate)

                        NewVersionDialogFragment.newInstance(false).apply {
                            show(supportFragmentManager, ConstantsDialog.DIALOG_NEW_VERSION)
                        }
                    }
                } else {
                    //Иначе - запускаем диалоговое окно без возможности выйти с него
                    NewVersionDialogFragment.newInstance(true).apply {
                        show(supportFragmentManager, ConstantsDialog.DIALOG_NEW_VERSION)
                    }
                }
            } else {
                viewModel.liveDataCorrectAppVersion.value = true
            }
        })
        viewModel.liveDataCorrectAppVersion.observe(this, Observer {
            Log.d(TAG, "observer correctAppVersion")
            //Если версия корректна - отрисовываем фрагменты
            if (it) {
                //ApplicationPreferences.setToken(this, "")
                val token = ApplicationPreferences.getToken(this)!!
                if (token.equals("")) {
                    Navigation.lastMenuItem = R.id.navigation_profile
                    navView.visibility = View.GONE
                    Navigation.lastFragment = LoginFragment()
                }
                //P.S. проверка валидности токена происходит в RecommendationViewModel
                navView.selectedItemId = Navigation.lastMenuItem
                supportFragmentManager.beginTransaction()
                    .add(R.id.nav_host_fragment_activity_main, Navigation.lastFragment).addToBackStack(null).commit()
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