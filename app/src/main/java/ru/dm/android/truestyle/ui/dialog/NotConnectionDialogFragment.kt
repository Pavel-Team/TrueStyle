/**Фрагмент диалогового окна с уведомлением о выключенном интернете*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentNotConnectionBinding
import ru.dm.android.truestyle.service.SystemService

class NotConnectionDialogFragment : DialogFragment() {

    lateinit var animation: AnimationDrawable

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = DialogFragmentNotConnectionBinding.inflate(requireActivity().layoutInflater)
        animation = view.imageViewNotConnection.background as AnimationDrawable
        view.buttonRetry.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                if (SystemService.isInternetAvailable(context!!))
                    dismiss()
                else {
                    if (animation.isRunning)
                        animation.stop()
                    animation.start()
                }
            }
        })

        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(view.root)

        return dialog
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (animation.isRunning)
            animation.stop()
    }
}