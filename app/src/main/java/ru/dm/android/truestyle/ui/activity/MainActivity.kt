/**Главная активити*/
package ru.dm.android.truestyle.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.dm.android.truestyle.BuildConfig
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.ConnectionLiveData
import ru.dm.android.truestyle.api.interceptor.InternetConnectionInterceptor
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.api.interceptor.ServerErrorInterceptor
import ru.dm.android.truestyle.databinding.ActivityMainBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.NewVersionDialogFragment
import ru.dm.android.truestyle.ui.dialog.NotConnectionDialogFragment
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.LoginFragmentDirections
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.MainActivityViewModel
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    lateinit var navView: BottomNavigationView

    private var navigation = Navigation
    private lateinit var connectionLiveData: ConnectionLiveData


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, ApplicationPreferences.getLanguage(newBase)))
        resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        //Добавляем интерсептор для сети
        Networking.apply {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(InternetConnectionInterceptor(applicationContext))
                .addInterceptor(ServerErrorInterceptor(supportFragmentManager))
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            api = retrofit.create()
        }

        val viewModel: MainActivityViewModel by viewModels()

        //ВРЕМЕННО
        if(!hasWifi(this)) {
            NotConnectionDialogFragment().apply {
                show(supportFragmentManager, ConstantsDialog.DIALOG_NOT_CONNECTION)
            }
            return
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


        //Настраиваем контейнер
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navView.itemIconTintList = null //Чтобы не накладывался цвет из темы
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED //Для того, чтобы не отображался текст

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)


        //Отрисовываем нужный фрагмент в зависимости от корректности версии и токена
        viewModel.liveDataCorrectAppVersion.observe(this, Observer {
            Log.d(TAG, "observer correctAppVersion")
            Log.d(TAG, it.toString())
            //Если версия корректна - отрисовываем фрагменты
            if (it) {
                val token = ApplicationPreferences.getToken(this)!!
                //На слаучай с поворотом экрана
                if (token == "") {
                    navView.visibility = View.GONE
                } else {
                    val action = LoginFragmentDirections.actionNavigationLoginToNavigationRecommendation()
                    navController.navigate(action)
                }
            }
        })

    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy MainActivity")
    }


    //ВРЕМЕННО: метод проверки на наличие инета
    private fun hasWifi(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return (netInfo != null && netInfo.isConnectedOrConnecting)
    }

}