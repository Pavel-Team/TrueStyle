package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentRegistrationBinding
import ru.dm.android.truestyle.ui.navigation.NavigationCallbacks
import ru.dm.android.truestyle.viewmodel.RegistrationViewModel

private const val TAG = "RegistrationFragment"

class RegistrationFragment: Fragment() {
    private lateinit var registrationViewModel: RegistrationViewModel
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var callbacks: NavigationCallbacks

    private val REGEX_EMAIL by lazy { Regex("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbacks = context as NavigationCallbacks
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@RegistrationFragment
        binding.viewModel = registrationViewModel


        //Слушатель на ник пользователя
        binding.editTextUsername.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (binding.editTextUsername.text.isNotEmpty() || hasFocus)
                    binding.textHintUsername.visibility = View.VISIBLE
                else
                    binding.textHintUsername.visibility = View.INVISIBLE

                if(!hasFocus) {
                    if (binding.editTextUsername.text.isNotEmpty()) {
                        //Отправка запроса на сервер
                        registrationViewModel.checkUserName(binding.editTextUsername.text.toString())
                    } else {
                        registrationViewModel.liveDataIsCorrectUsername.value = false;
                    }

                    binding.textErrorUsername.visibility = if (registrationViewModel.liveDataIsCorrectUsername.value!!) View.INVISIBLE else View.VISIBLE
                    //setEnabledButtonRegister()
                }
            }


        //Слушатель на email пользователя
        binding.editTextEmail.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (binding.editTextEmail.text.isNotEmpty() || hasFocus)
                    binding.textHintEmail.visibility = View.VISIBLE
                else
                    binding.textHintEmail.visibility = View.INVISIBLE

                if (!hasFocus) {
                    registrationViewModel.liveDataIsCorrectEmail.value = REGEX_EMAIL.matches(binding.editTextEmail.text)
                    //Если такой Email есть
                    binding.textErrorEmail.text = if (registrationViewModel.checkEmail(binding.editTextEmail.text.toString()))
                        resources.getString(R.string.error_email)
                     else
                        resources.getString(R.string.error_email_2)


                    if (registrationViewModel.liveDataIsCorrectEmail.value!!) {
                        val color = ContextCompat.getColor(requireContext(), R.color.black)
                        binding.textErrorEmail.visibility = View.INVISIBLE
                        binding.textHintEmail.setTextColor(color)
                        binding.editTextEmail.background.colorFilter =
                            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
                    } else {
                        val color = ContextCompat.getColor(requireContext(), R.color.red)
                        binding.textErrorEmail.visibility = View.VISIBLE
                        binding.textHintEmail.setTextColor(color)
                        binding.editTextEmail.background.colorFilter =
                            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
                    }

                    if (binding.editTextEmail.text.isEmpty()) {
                        val color = ContextCompat.getColor(requireContext(), R.color.black)
                        binding.textErrorEmail.visibility = View.INVISIBLE
                        binding.textHintEmail.setTextColor(color)
                        binding.editTextEmail.background.colorFilter =
                            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
                    }

                    //setEnabledButtonRegister()
                }
            }


        //Слушатель на пароль
        binding.editTextPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                val strong = registrationViewModel.checkStrongPassword(text.toString())
                if (text?.length==0) {
                    binding.textStrongPassword.visibility = View.INVISIBLE
                    binding.lineStrongPassword1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    binding.lineStrongPassword2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    binding.lineStrongPassword3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))
                    binding.lineStrongPassword4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_4))

                    registrationViewModel.liveDataIsCorrectPassword.value = false
                    setEnabledButtonRegister()
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

                registrationViewModel.liveDataIsCorrectPassword.value = strong != 0
                //setEnabledButtonRegister()
            }
        })


        //Слушатель на кнопку регистрации
        binding.buttonRegister.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                //...запускаем анимацию

                //Запрос на сервер
                registrationViewModel.registerUser(
                    username = binding.editTextUsername.text.toString(),
                    email = binding.editTextEmail.text.toString(),
                    password = binding.editTextPassword.text.toString()
                )
            }
        })


        //Слушатель для текста "Есть аккаунт"
        binding.textHasAccount.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = LoginFragment()
                callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
                callbacks.clearStackFragment(R.id.navigation_profile)
            }
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registrationViewModel.liveDataIsCorrectUsername.observe(viewLifecycleOwner, Observer {
            setEnabledButtonRegister()
        })

        registrationViewModel.liveDataIsCorrectEmail.observe(viewLifecycleOwner, Observer {
            setEnabledButtonRegister()
        })

        registrationViewModel.liveDataIsCorrectPassword.observe(viewLifecycleOwner, Observer {
            setEnabledButtonRegister()
        })

        registrationViewModel.liveDataSuccessRegistration.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "update viewModel")

            //...убираем анимацию

            val fragmentTo = ProfileFragment()
            callbacks.navigateTo(fragmentTo, R.id.navigation_profile)
            callbacks.clearStackFragment(R.id.navigation_profile)
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Функция устанавливающая возможность нажатия на кнопку регистрации
    private fun setEnabledButtonRegister() {
        binding.buttonRegister.isEnabled = (registrationViewModel.liveDataIsCorrectUsername.value!! &&
                registrationViewModel.liveDataIsCorrectEmail.value!! &&
                registrationViewModel.liveDataIsCorrectPassword.value!!)
    }
}