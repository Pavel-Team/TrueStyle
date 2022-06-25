/**Фрагмент личного кабинета пользователя*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentErrorServerBinding
import ru.dm.android.truestyle.databinding.FragmentProfileBinding
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.ErrorServerDialogFragment
import ru.dm.android.truestyle.ui.dialog.NotConnectionDialogFragment
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.viewmodel.ProfileViewModel

private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var callbacks: NavigationCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbacks = context as NavigationCallbacks
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
                callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        //Обработчики на сезоны в гардеробе
        binding.buttonAutumn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_autumn))
                callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })
        binding.buttonSummer.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_summer))
                callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })
        binding.buttonSpring.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_spring))
                callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })
        binding.buttonWinter.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = WardrobeFragment.newInstance(resources.getString(R.string.title_winter))
                callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.liveDataSqlite.observe(
            viewLifecycleOwner,
            Observer {
                Log.d(TAG, "update sqlite livedata")
                Log.d(TAG, profileViewModel.liveDataSqlite.value?.get(0).toString())

                //Здесь запросики с токеном
                //...
            }
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}