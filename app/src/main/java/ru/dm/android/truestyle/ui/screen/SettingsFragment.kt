/**Фрагмент с настройками*/
package ru.dm.android.truestyle.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentSettingsBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.dialog.ConfirmQuitDialogFragment
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.EditUsernameDialogFragment
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.util.makeLinks
import ru.dm.android.truestyle.viewmodel.SettingsViewModel


class SettingsFragment: Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation


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

        settingsViewModel.initViewModel() //Для окончательного обновления языка

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Слушатель на кнопку "Выйти"
        binding.imageButtonQuit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ConfirmQuitDialogFragment().apply {
                    show(this@SettingsFragment.parentFragmentManager, ConstantsDialog.DIALOG_CONFIRM_QUIT)
                }
            }
        })

        //Слушатели для настроек (ВРЕМЕННО ТОЛЬКО ДЛЯ ЯЗЫКА)
        binding.layoutLanguage.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = SettingFragment.newInstance(binding.textViewLanguage.text.toString())
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        //Слушатель кнопки "Подписаться"
        binding.subscribe.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intentTg = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.TELEGRAM_CHANEL)
                )
                startActivity(intentTg)
            }
        })

        //Слушатель кнопки "Оценить"
        binding.estimate.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.LINK_IN_RUSTORE)
                startActivity(intent)
            }
        })

        //Слушатель кнопки "Техподдержка"
        binding.techSupport.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = TechnicalSupportFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        //Слушатель текста с политикой конфидециальности
        binding.textViewPrivacyPolicy.makeLinks(
            Pair(resources.getString(R.string.terms2), View.OnClickListener {
                val fragmentTo = PrivacyPolicyFragment.newInstance(Constants.URL_TERMS)
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }),
            Pair(resources.getString(R.string.privacy2), View.OnClickListener {
                val fragmentTo = PrivacyPolicyFragment.newInstance(Constants.URL_PRIVACY_POLICY)
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            })
        )

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}