/**Фрагмент диалогового окна с изменением имени пользователя*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentEditUsernameBinding
import ru.dm.android.truestyle.databinding.FragmentProfileBinding
import ru.dm.android.truestyle.viewmodel.ProfileViewModel

private const val ARG_USERNAME = "username" //Имя пользователя, переданное из фрагмента в аргументах

class EditUsernameDialogFragment : DialogFragment() {

    private var lastUsername: String? = null
    private lateinit var viewModel: ProfileViewModel

    private var _binding: DialogFragmentEditUsernameBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentEditUsernameBinding.inflate(requireActivity().layoutInflater)
        viewModel = ViewModelProvider(targetFragment!!).get(ProfileViewModel::class.java)

        //Получаем аргументы
        lastUsername = arguments?.getString(ARG_USERNAME)
        if (lastUsername != null)
            binding.editTextUsername.setText(lastUsername)

        //Слушатель на кнопку "Применить"
        binding.buttonApply.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val username = binding.editTextUsername.text.toString()
                if (username.isNotEmpty() && username != lastUsername)
                    viewModel.checkUsername(username)
            }
        })


        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Слушатель на случай, если никнейм корректный
        viewModel.liveDataIsCorrectUsername.observe(this, Observer {
            val username = binding.editTextUsername.text.toString()

            if (it) {
                binding.textErrorUsername.visibility = View.INVISIBLE

                viewModel.setNewUsername(username)
                viewModel.liveDataIsCorrectUsername.value = false //Сбрасываем корректность

                dismiss() //Если всё успешно - закрываем окно
            } else {
                binding.textErrorUsername.visibility = View.VISIBLE
            }

            //Если ники одинаковы - убираем ошибку, но не закрываем окно
            if (username == lastUsername)
                binding.textErrorUsername.visibility = View.INVISIBLE
        })

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(username: String) : EditUsernameDialogFragment {
            val args = Bundle().apply {
                putString(ARG_USERNAME, username)
            }
            return EditUsernameDialogFragment().apply {
                arguments = args
            }
        }
    }
}