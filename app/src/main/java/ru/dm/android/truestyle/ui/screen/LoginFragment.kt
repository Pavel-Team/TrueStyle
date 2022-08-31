/**Фрагмент со страницей входа*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentLoginBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.LoginViewModel

private const val TAG = "LoginFragment"

class LoginFragment : Fragment(){
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation

    private var isFirstOpen = true //Первый ли запуск окна (антибаг для обсервера)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        Log.d(TAG, "onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@LoginFragment
        binding.viewModel = loginViewModel

        //Слушатель кнопки "Показать пароль"
        binding.imageViewShowPassword.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                loginViewModel.liveDataIsShowPassword.value = !loginViewModel.liveDataIsShowPassword.value!!
            }
        })

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

        //Слушатель поля "Забыли пароль?"
        binding.textViewForgotPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = PasswordResetFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
            }
        })

        //Слушатель кнопки "Войти с помощью Google"
        binding.buttonLoginGoogle.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(activity, resources.getString(R.string.soon_update), Toast.LENGTH_SHORT).show()
            }
        })

        //Слушатель кнопки "Войти с помощью Vk"
        binding.buttonLoginVk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(activity, resources.getString(R.string.soon_update), Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirstOpen = true

        loginViewModel.liveDataIsShowPassword.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        })

        loginViewModel.liveDataIsSignIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                val fragmentTo = ProfileFragment()
                navigation.setVisibleNavView() //Включаем нижнее меню при успешном входе
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
                navigation.clearStackFragment(R.id.navigation_profile)
            } else {
                if (binding.editTextLogin.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty() && !isFirstOpen)
                    Toast.makeText(requireContext(), resources.getString(R.string.error_login), Toast.LENGTH_SHORT).show()
            }
            isFirstOpen = false
        })
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        _binding = null
    }
}