/**Интерфейс для настройки навигации фрагментов через любую активити*/
package ru.dm.android.truestyle.ui.navigation

import androidx.fragment.app.Fragment

interface NavigationCallbacks {
    /**Метод навигации с текущего фрагмента на фрагмент toFragment, открытый в itemMenu с id = idItemMenu*/
    fun navigateTo(toFragment: Fragment, idItemMenu: Int)
}