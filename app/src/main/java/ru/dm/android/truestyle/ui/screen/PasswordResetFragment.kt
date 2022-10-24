package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentPasswordResetBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.PasswordResetViewModel

class PasswordResetFragment : Fragment() {

    private val viewModel by viewModels<PasswordResetViewModel>()
    private var _binding: FragmentPasswordResetBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@PasswordResetFragment
        binding.viewModel = viewModel

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Слушатель на ввод email
        binding.editTextEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonReset.isEnabled = Constants.REGEX_EMAIL.matches(binding.editTextEmail.text)
            }
        })

        //Слушатель на кнопку "Отправить письмо"
        binding.buttonReset.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                //Отправляем запрос во viewModel
                viewModel.resetPassword(binding.editTextEmail.text.toString())
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isFirstOpen = true

        //Если email корректный - перенаправляем на следующую страницу, иначе - выводим ошибку
        viewModel.liveDataIsCorrectEmail.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.liveDataIsCorrectEmail.value = false //Сбрасываем перед переходом на след экран
                isFirstOpen = true
                binding.textErrorEmail.visibility = View.INVISIBLE

                val action = PasswordResetFragmentDirections.actionNavigationPasswordResetToNavigationSetNewPassword()
                this@PasswordResetFragment.findNavController().navigate(action)

                return@Observer
            } else {
                if (!isFirstOpen)
                    binding.textErrorEmail.visibility = View.VISIBLE
            }

            isFirstOpen = false
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}