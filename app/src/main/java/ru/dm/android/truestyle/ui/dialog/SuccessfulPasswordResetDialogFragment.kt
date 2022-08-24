/**Диалоговое окно с уведомлением об успешной смене пароля*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentSuccessfulPasswordResetBinding
import ru.dm.android.truestyle.ui.navigation.Navigation
import ru.dm.android.truestyle.ui.screen.LoginFragment
import ru.dm.android.truestyle.ui.screen.ProfileFragment

class SuccessfulPasswordResetDialogFragment : DialogFragment(){
    private var _binding: DialogFragmentSuccessfulPasswordResetBinding? = null
    private val binding get() = _binding!!

    private val navigation = Navigation


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentSuccessfulPasswordResetBinding.inflate(requireActivity().layoutInflater)

        //Слушатель на кнопку "На главную"
        binding.buttonToMain.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val fragmentTo = LoginFragment()
                navigation.navigateTo(fragmentTo, R.id.navigation_profile)
                navigation.clearStackFragment(R.id.navigation_profile)
                dismiss()
            }
        })

        //Запускаем анимацию
        val animation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.set_visible)
        binding.imageViewSuccess.startAnimation(animation)

        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        //Запрещаем закрывать диалоговое окно
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        return dialog
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}