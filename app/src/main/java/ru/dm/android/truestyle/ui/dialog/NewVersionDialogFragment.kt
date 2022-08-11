/**Диалоговое окно, уведомляющее о наличии обновления приложения*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.DialogFragmentNewVersionBinding
import ru.dm.android.truestyle.util.Constants

private const val TAG = "NewVersionDialog"
private const val ARG_IS_NECESSARY = "isNecessary" //Обязательно ли нужно обновить приложение

class NewVersionDialogFragment: DialogFragment() {

    private var isNecessary: Boolean = false //Обязательно ли нужно обновиться

    private var _binding: DialogFragmentNewVersionBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentNewVersionBinding.inflate(requireActivity().layoutInflater)

        //Получаем аргументы
        isNecessary = arguments?.getBoolean(ARG_IS_NECESSARY)!!
        binding.isNecessary = isNecessary

        //Слушатель на кнопку "Обновить"
        binding.buttonUpdate.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(Constants.LINK_IN_PLAY_MARKET)
                startActivity(intent)
            }
        })

        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        //Если обновление обязательное - запрещаем закрывать диалоговое окно
        if (isNecessary) {
            dialog.setCanceledOnTouchOutside(false)
            isCancelable = false
        }

        return dialog
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        fun newInstance(isNecessary: Boolean) : NewVersionDialogFragment {
            val args = Bundle().apply {
                putBoolean(ARG_IS_NECESSARY, isNecessary)
            }
            return NewVersionDialogFragment().apply {
                arguments = args
            }
        }
    }
}