/**Фрагмент с одной настройкой*/
package ru.dm.android.truestyle.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentOneSettingBinding
import ru.dm.android.truestyle.databinding.ItemSettingBinding
import ru.dm.android.truestyle.model.Setting
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.preferences.LanguageContextWrapper
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.viewmodel.SettingViewModel

private const val TAG = "SettingFragment"
private const val ARG_TITLE_SETTING = "titleSetting"

class SettingFragment : Fragment(){

    private lateinit var settingViewModel: SettingViewModel
    private var _binding: FragmentOneSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var callbacks: NavigationCallbacks
    private lateinit var titleSetting: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbacks = context as NavigationCallbacks
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentOneSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.lifecycleOwner = this@SettingFragment

        titleSetting = arguments?.getString(ARG_TITLE_SETTING, "")!!
        binding.titleSetting.text = titleSetting

        //Заполнение таблицы настроек
        updateTableSetting(titleSetting)

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Функция заполнения таблицы настроек в завиимости от выбранного вида настроек
    private fun updateTableSetting(titleSetting: String) {
        when (titleSetting) {
            resources.getString(R.string.row_settings_language) -> {
                val arrayLanguages = resources.getStringArray(R.array.array_languages)
                for (i in 0..arrayLanguages.size-2)
                    addValueInTable(Setting(arrayLanguages[i]), false)
                addValueInTable(Setting(arrayLanguages[arrayLanguages.size-1]), true)
            }
            resources.getString(R.string.row_settings_theme) -> {}
            resources.getString(R.string.row_settings_push) -> {}
        }
    }


    //Функция добавляющая одно значение для выбора настроек в таблицу
    //На вход принимает 2 параметра:
    //setting: Setting - текущую настройку
    //isLastElem: Boolean - является ли эта настройка последней в списке
    private fun addValueInTable(setting: Setting, isLastElem: Boolean) {
        val textView = DataBindingUtil.inflate<ItemSettingBinding>(
            LayoutInflater.from(context),
            R.layout.item_setting,
            binding.tableSetting,
            false
        )
        textView.model=setting
        textView.root.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                settingViewModel.onClickSetting(titleSetting, setting)
                activity?.onBackPressed()
            }
        })
        if (isLastElem)
            textView.root.background = null

        binding.tableSetting.addView(textView.root)
    }



    companion object {
        fun newInstance(titleSetting: String) : SettingFragment{
            val args = Bundle().apply {
                putSerializable(ARG_TITLE_SETTING, titleSetting)
            }
            return SettingFragment().apply {
                arguments = args
            }
        }
    }
}