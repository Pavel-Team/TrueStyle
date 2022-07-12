/**Фрагмент редактирования основной информации о пользователе*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentEditProfileBinding
import ru.dm.android.truestyle.model.User
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.viewmodel.EditProfileViewModel
import javax.inject.Inject

private const val TAG = "EditProfileFragment"
private const val ARG_USER = "User" //Для аргумента Bundle с информацией о пользователе

@AndroidEntryPoint
class EditProfileFragment: Fragment() {

    private lateinit var editProfileViewModel: EditProfileViewModel
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigation: Navigation


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        editProfileViewModel.liveData.value=requireArguments().getParcelable(ARG_USER)

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@EditProfileFragment

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Слушатель на кнопку "Изменить"
        binding.buttonEdit.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                //...ДОБАВИТЬ ПРОВЕРКУ НА ВАЛИДАЦИЮ И СДЕЛАТЬ ЗАПРОС С РЕПОЗИТОРИЯ!!!
                Toast.makeText(requireContext(), R.string.soon_edit_profile, Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(user: User): EditProfileFragment {
            val args = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
            return EditProfileFragment().apply {
                arguments = args
            }
        }
    }

}