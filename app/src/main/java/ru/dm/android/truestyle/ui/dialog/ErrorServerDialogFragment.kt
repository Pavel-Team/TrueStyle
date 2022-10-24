/**Фрагмент диалового окна с ошибкой получения ответа от сервера*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentErrorServerBinding

private const val TAG = "ErrorServerDialogFrag"

class ErrorServerDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog")
        val view = DialogFragmentErrorServerBinding.inflate(requireActivity().layoutInflater)
        view.buttonOk.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                dismiss()
            }
        })
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.animation_rotate)
        view.imageViewErrorServer.startAnimation(animation)

        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(view.root)

        return dialog
    }
}