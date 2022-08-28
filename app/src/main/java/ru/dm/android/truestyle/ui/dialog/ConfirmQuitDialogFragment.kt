/**Диалоговое окно с подтверждением выхода с аккаунта*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentConfirmQuitBinding
import ru.dm.android.truestyle.preferences.ApplicationPreferences
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.LoginFragment

class ConfirmQuitDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentConfirmQuitBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentConfirmQuitBinding.inflate(requireActivity().layoutInflater)

        //Слушатель на кнопку "Подтвердить"
        binding.buttonConfirmQuit.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                quit()

                val fragmentTo = LoginFragment()
                Navigation.navigateTo(fragmentTo, R.id.navigation_profile)
                Navigation.initNewState()

                dismiss()
            }
        })

        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        return dialog
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //Выход из приложения (удаление токена из настроек)
    fun quit() {
        ApplicationPreferences.setToken(requireContext(), "")
    }
}