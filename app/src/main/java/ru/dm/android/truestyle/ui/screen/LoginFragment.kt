/**Фрагмент со страницей входа*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentLoginBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.LoginViewModel
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment(){
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigation: Navigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        Log.d("RegistrationLogin", "onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@LoginFragment
        binding.viewModel = loginViewModel

        //Слушатель кнопки регистрация
        binding.buttonSignUp.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View?) {
                val fragmentTo = RegistrationFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        //Слушатель кнопки входа
        binding.buttonSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (binding.editTextLogin.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty())
                loginViewModel.signIn(binding.editTextLogin.text.toString(), binding.editTextPassword.text.toString())
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.liveDataIsSignIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                val fragmentTo = ProfileFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
                navigation.clearStackFragment(R.id.navigation_profile)
            }
            else {
                if (binding.editTextLogin.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty())
                    Toast.makeText(requireContext(), resources.getString(R.string.error_login), Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}