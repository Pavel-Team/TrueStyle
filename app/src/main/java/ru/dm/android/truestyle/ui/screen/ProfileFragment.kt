/**Фрагмент личного кабинета пользователя*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentProfileBinding
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.EditUsernameDialogFragment
import ru.dm.android.truestyle.ui.dialog.NotConnectionDialogFragment
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ProfileViewModel

private const val CODE_EDIT_USERNAME = 0 //Для таргет фрагмента к диалоговому окну редактирования имени

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        Log.d("sssss", profileViewModel.toString())

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@ProfileFragment
        binding.viewModel = profileViewModel


//        NotConnectionDialogFragment().apply {
//            show(this@ProfileFragment.requireFragmentManager(), ConstantsDialog.DIALOG_NOT_CONNECTION)
//        }


        //Обработчик кнопки с настройками
        binding.imageButtonSettings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = SettingsFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        //Обработчики на сезоны в гардеробе
        binding.buttonAutumn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_autumn))
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })
        binding.buttonSummer.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_summer))
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })
        binding.buttonSpring.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_spring))
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })
        binding.buttonWinter.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_winter))
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Как только получаем инфу о пользователе - делаем доступными вкладки для изменения информации
        profileViewModel.liveData.observe(viewLifecycleOwner, Observer {

            //Слушатели на View с основной информацией о пользователе
            binding.userName.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    //Запускаем диалоговое окно использую shared view model
                    EditUsernameDialogFragment.newInstance(
                        it.username.toString()
                    ).apply {
                        setTargetFragment(this@ProfileFragment, CODE_EDIT_USERNAME)
                        show(this@ProfileFragment.parentFragmentManager, ConstantsDialog.DIALOG_EDIT_USERNAME)
                    }
                }
            })
            binding.userStyleLayout.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
//                    val fragmentTo = EditProfileFragment.newInstance(profileViewModel.liveData.value!!)
//                    navigation.navigateTo(fragmentTo, R.id.navigation_profile)
                }
            })
            binding.userInfoTable.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    //...
                }
            })
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}