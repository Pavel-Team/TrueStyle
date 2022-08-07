package ru.dm.android.truestyle.ui.navigation

import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.ErrorServerDialogFragment
import ru.dm.android.truestyle.ui.screen.ArticlesFragment
import ru.dm.android.truestyle.ui.screen.ClothesSearchFragment
import ru.dm.android.truestyle.ui.screen.ProfileFragment
import ru.dm.android.truestyle.ui.screen.RecommendationFragment
import java.util.*
import kotlin.collections.HashMap


private const val TAG = "Navigation"

object Navigation{

    private lateinit var fragmentManager: FragmentManager
    private lateinit var navView: BottomNavigationView

    private var mapStackFragments: HashMap<Int, Stack<Fragment>> = HashMap() //<id выбранного пункта меню, стэк фрагментов>
    var lastMenuItem: Int = R.id.navigation_recommendation           //Последний выбранный пункт меню
    var lastFragment: Fragment = RecommendationFragment()

    init {
        Log.d("RegistrationNavigation", "init")
        //Инициализация стэков фрагментов для каждого пункта меню
        mapStackFragments.put(R.id.navigation_recommendation, Stack())
        mapStackFragments.put(R.id.navigation_clothes_search, Stack())
        mapStackFragments.put(R.id.navigation_articles, Stack())
        mapStackFragments.put(R.id.navigation_profile, Stack())
    }


    //ПРОВЕРИТЬ ПОТОМ НА УТЕЧКУ
    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }


    //ПРОВЕРИТЬ ПОТОМ НА УТЕЧКУ
    fun setNavView(navView: BottomNavigationView) {
        this.navView = navView
    }


    fun setVisibleNavView(){
        navView.visibility = View.VISIBLE
        navView.selectedItemId = R.id.navigation_profile
        clearStackFragment(R.id.navigation_recommendation)
    }


    fun initNewState() {
        clearStackFragment()
        navView.visibility = View.GONE
        navView.selectedItemId = R.id.navigation_profile
    }


    //Открытие окна с ошибкой на сервере
    fun openErrorServerDialogFragment(){
        ErrorServerDialogFragment().apply {
            show(fragmentManager!!, ConstantsDialog.DIALOG_ERROR_SERVER)
        }
    }


    //Навигация по фрагментам
    fun navigateTo(toFragment: Fragment, idItemMenu: Int) {
        Log.d(TAG, fragmentManager.backStackEntryCount.toString())
        val currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        mapStackFragments.get(lastMenuItem)!!.push(currentFragment)

        //На случай если надо запустить фрагмент в другой вкладке (не равной lastItem)
        navView.menu.findItem(idItemMenu).isChecked=true

        lastMenuItem = idItemMenu
        lastFragment = toFragment
        fragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, toFragment)
            .addToBackStack(null)
            .commit()
    }


    //Выбор пункта меню
    fun onNavigationItemSelected(item: MenuItem): Boolean {
        //Если нажат тот же пункт меню - ничего не делаем
        if (item.itemId != lastMenuItem) {
            //Добавляем в стек прошлый фрагмент
            mapStackFragments.get(lastMenuItem)?.push(fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main))
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
                    fragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, newFragment)
                        .addToBackStack(null)
                        .commit()
                    lastFragment = newFragment
                }
            } else {
                //Последний фрагмент из стека
                val fragmentFromStack = mapStackFragments.get(item.itemId)!!.pop()
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragmentFromStack)
                    .addToBackStack(null)
                    .commit()
                lastFragment = fragmentFromStack
            }
            lastMenuItem = item.itemId
        }
        return true
    }


    //Очистка стека фрагмента по определенному Id
    fun clearStackFragment(idItemMenu: Int) {
        while (!mapStackFragments.get(idItemMenu)!!.isEmpty())
            mapStackFragments.get(idItemMenu)!!.pop()
    }


    //Очистка всего стека фрагментов
    fun clearStackFragment() {
        mapStackFragments = HashMap()
        mapStackFragments.put(R.id.navigation_recommendation, Stack())
        mapStackFragments.put(R.id.navigation_clothes_search, Stack())
        mapStackFragments.put(R.id.navigation_articles, Stack())
        mapStackFragments.put(R.id.navigation_profile, Stack())
    }


    //Нажатие кнопки назад. Возвращает true, если необходимо закрыть приложение
    fun onBackPressed(): Boolean {
        if (mapStackFragments.get(lastMenuItem)!!.isEmpty())
            //finish()
            return true
        else {
            val lastFragment = mapStackFragments.get(lastMenuItem)!!.pop()
            fragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, lastFragment)
                .commit()
            this.lastFragment = lastFragment
            return false
        }
    }
}