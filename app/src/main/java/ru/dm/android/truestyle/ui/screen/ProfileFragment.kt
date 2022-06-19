/**Фрагмент личного кабинета пользователя*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentProfileBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.ProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigation: Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}