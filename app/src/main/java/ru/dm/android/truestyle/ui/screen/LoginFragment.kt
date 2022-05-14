/**Фрагмент со страницей входа*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.databinding.FragmentLoginBinding
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.viewmodel.LoginViewModel


class LoginFragment : Fragment(){
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
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
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@LoginFragment
        binding.viewModel = loginViewModel

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}