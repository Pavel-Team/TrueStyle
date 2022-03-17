/**Главная активити*/
package ru.dm.android.truestyle.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ActivityMainBinding


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED //Для того, чтобы не отображался текст

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_recommendation, R.id.navigation_clothes_search, R.id.navigation_articles, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)
    }
    
}