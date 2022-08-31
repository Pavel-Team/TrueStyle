package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentSetNewPasswordBinding
import ru.dm.android.truestyle.ui.dialog.ConfirmQuitDialogFragment
import ru.dm.android.truestyle.ui.dialog.ConstantsDialog
import ru.dm.android.truestyle.ui.dialog.SuccessfulPasswordResetDialogFragment
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.RegistrationViewModel
import ru.dm.android.truestyle.viewmodel.SetNewPasswordViewModel

private const val TAG = "SetNewPasswordFrag"

class SetNewPasswordFragment : Fragment() {

    private val viewModel by viewModels<SetNewPasswordViewModel>()
    private var _binding: FragmentSetNewPasswordBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSetNewPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@SetNewPasswordFragment
        binding.viewModel = viewModel

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Слушатель на токен
        binding.editTextToken.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                if (text?.length != 0) {
                    viewModel.isCorrectToken = true
                    binding.textHintToken.visibility = View.VISIBLE
                } else {
                    viewModel.isCorrectToken = false
                    binding.textHintToken.visibility = View.INVISIBLE
                }
                checkEnabledButton()
            }
        })

        //Слушатель на пароль
        binding.editTextPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                val strong = RegistrationViewModel.checkStrongPassword(text.toString())
                if (text?.length==0) {
                    binding.textStrongPassword.visibility = View.INVISIBLE
                    binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))

                    viewModel.isCorrectNewPassword = false
                    checkEnabledButton()

                    return
                }

                binding.textStrongPassword.visibility = View.VISIBLE
                when(strong) {
                    0 -> {
                        binding.textStrongPassword.text = resources.getString(R.string.strong_password_0)
                        binding.textStrongPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                        binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                        binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                        binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    }
                    1 -> {
                        binding.textStrongPassword.text = resources.getString(R.string.strong_password_1)
                        binding.textStrongPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                        binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
                        binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                        binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                        binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    }
                    2 -> {
                        binding.textStrongPassword.text = resources.getString(R.string.strong_password_2)
                        binding.textStrongPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
                        binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
                        binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
                        binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                        binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    }
                    3 -> {
                        binding.textStrongPassword.text = resources.getString(R.string.strong_password_3)
                        binding.textStrongPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                        binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    }
                    else -> {
                        binding.textStrongPassword.text = resources.getString(R.string.strong_password_4)
                        binding.textStrongPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                    }
                }

                viewModel.isCorrectNewPassword = strong != 0
                checkEnabledButton()
            }
        })

        //Слушатель кнопки "Показать пароль"
        binding.imageViewShowPassword.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.liveDataIsShowPassword.value = !viewModel.liveDataIsShowPassword.value!!
            }
        })

        //Слушатель на кнопку "Сменить пароль"
        binding.buttonSetNewPassword.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.setNewPassword(binding.editTextToken.text.toString(), binding.editTextPassword.text.toString())
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isFirstOpen = true

        viewModel.liveDataIsShowPassword.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        })

        viewModel.liveDataIsSuccessfulChangePassword.observe(viewLifecycleOwner, Observer {
            if (it) {
                //Сбрасываем до начальных установок
                viewModel.liveDataIsSuccessfulChangePassword.value = false
                isFirstOpen = true
                binding.textErrorToken.text = resources.getString(R.string.hint_token)
                binding.textErrorToken.setTextColor(resources.getColor(R.color.gray_3))

                //Открываем диалоговое окно
                SuccessfulPasswordResetDialogFragment().apply {
                    show(this@SetNewPasswordFragment.parentFragmentManager, ConstantsDialog.DIALOG_SUCCESSFUL_RESET_PASSWORD)
                }

                return@Observer
            } else {
                if (!isFirstOpen) {
                    binding.textErrorToken.text = resources.getString(R.string.error_token)
                    binding.textErrorToken.setTextColor(resources.getColor(R.color.red))
                }
            }
            isFirstOpen = false
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Установка возможности нажать на кнопку
    private fun checkEnabledButton() {
        binding.buttonSetNewPassword.isEnabled = viewModel.isCorrectToken && viewModel.isCorrectNewPassword
    }

}