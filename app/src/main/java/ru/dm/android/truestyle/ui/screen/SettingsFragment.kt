/**Фрагмент с настройками*/
package ru.dm.android.truestyle.ui.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentSettingsBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.SettingsViewModel


class SettingsFragment: Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Нужно для обновления языка
        val ctx: Context = LanguageContextWrapper.wrap(getContext()!!, ApplicationPreferences.getLanguage(getContext()!!))
        resources.updateConfiguration(ctx.getResources().getConfiguration(), ctx.getResources().getDisplayMetrics())
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        //settingsViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
        //    SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.viewModel = settingsViewModel
        val root: View = binding.root

        binding.lifecycleOwner = this@SettingsFragment

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Слушатели для настроек (ВРЕМЕННО ТОЛЬКО ДЛЯ ЯЗЫКА)
        binding.layoutLanguage.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = SettingFragment.newInstance(binding.textViewLanguage.text.toString())
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}