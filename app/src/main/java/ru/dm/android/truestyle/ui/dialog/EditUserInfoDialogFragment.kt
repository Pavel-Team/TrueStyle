/**Диалоговое окно с редактированием основной информации о пользователе*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentEditUserInfoBinding
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.ProfileViewModel


private const val TAG = "EditUserInfoDialog"
private const val ARG_COUNTRY = "country" //Выбранная ранее пользователем страна (из array-string)
private const val ARG_GENDER = "gender"   //Выбранный ранее пользователем пол (Men или Women)

class EditUserInfoDialogFragment: DialogFragment() {

    private var lastCountry: String? = null
    private var lastGender: String? = null
    private lateinit var viewModel: ProfileViewModel

    private var _binding: DialogFragmentEditUserInfoBinding? = null
    private val binding get() = _binding!!



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentEditUserInfoBinding.inflate(requireActivity().layoutInflater)
        viewModel = ViewModelProvider(targetFragment!!).get(ProfileViewModel::class.java)

        //Получаем аргументы
        lastCountry = arguments?.getString(ARG_COUNTRY)
        lastGender = arguments?.getString(ARG_GENDER)

        //Настраиваем Spinner со странами
        initSpinner(lastCountry)

        //Настраивам RadioGroup с гендерами
        initRadioGroup(lastGender)

        //Слушатель на кнопку "Применить"
        binding.buttonApply.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                //Изменяем информацию о пользователе
                val country = binding.spinnerCountry.selectedItem.toString()
                val gender = if (binding.radioMan.isChecked) Constants.GENDER_MAN else Constants.GENDER_WOMAN
                viewModel.setNewUserInfo(country, gender)
                dismiss()
            }
        })


        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        return dialog
    }


    //Первоначальная настройка Spinner'а со странами с учетом прошлой выбранной страны
    private fun initSpinner(lastCountry: String?) {
        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner_country,
            R.id.title_country,
            resources.getStringArray(R.array.countries)
        )
        binding.spinnerCountry.adapter = adapter

        if (lastCountry != null) {
            val selectedPosition = adapter.getPosition(lastCountry)
            binding.spinnerCountry.setSelection(selectedPosition)
        } else {
            binding.spinnerCountry.setSelection(166)
        }
    }


    //Первоначальная настройка RadioGroup с гендером с учетом прошлого выбранного пола
    private fun initRadioGroup(lastGender: String?) {
        if (lastGender.equals(Constants.GENDER_WOMAN)) {
            binding.radioMan.isChecked = false
            binding.radioWoman.isChecked = true
        } else {
            binding.radioWoman.isChecked = false
            binding.radioMan.isChecked = true
        }
    }



    companion object {
        fun newInstance(country: String?, gender: String?) : EditUserInfoDialogFragment {
            val args = Bundle().apply {
                putString(ARG_COUNTRY, country)
                putString(ARG_GENDER, gender)
            }
            return EditUserInfoDialogFragment().apply {
                arguments = args
            }
        }
    }
}