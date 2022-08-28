/**Диалоговое окно со сменой пользовательского стиля*/
package ru.dm.android.truestyle.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.api.response.StyleUser
import ru.dm.android.truestyle.databinding.DialogFragmentEditUserStyleBinding
import ru.dm.android.truestyle.databinding.ItemSpinnerStylesBinding
import ru.dm.android.truestyle.databinding.ItemSpinnerStylesSelectedBinding
import ru.dm.android.truestyle.viewmodel.ProfileViewModel

private const val TAG = "EditUserStyleDialog"
private const val ARG_STYLE = "style" //Выбранный ранее пользователем стиль (из array-string)
private const val ARG_LIST_STYLES = "listStyles" //Для списка со всеми стилями
private const val ARG_DRAWABLE_STYLE = "drawableStyle" //R.id текущей рандомной картинки

class EditUserStyleDialogFragment : DialogFragment() {

    private var lastStyle: StyleUser? = null
    private var arrayStyles: Array<StyleUser>? = null
    private var idDrawable: Int? = null
    private lateinit var viewModel: ProfileViewModel

    private var _binding: DialogFragmentEditUserStyleBinding? = null
    private val binding get() = _binding!!



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreate")
        _binding = DialogFragmentEditUserStyleBinding.inflate(requireActivity().layoutInflater)
        viewModel = ViewModelProvider(targetFragment!!).get(ProfileViewModel::class.java)

        //Получаем аргументы
        lastStyle = arguments?.getParcelable(ARG_STYLE)
        arrayStyles = arguments?.getParcelableArray(ARG_LIST_STYLES) as Array<StyleUser>?
        idDrawable = arguments?.getInt(ARG_DRAWABLE_STYLE)

        //Настраиваем Spinner со странами
        initSpinner(lastStyle)
        //Слушатель на кнопку "Применить"
        binding.buttonApply.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                //Изменяем информацию о пользователе
                val style = binding.spinnerStyles.selectedItem as StyleUser
                Log.d(TAG, style.toString())
                viewModel.setNewUserStyle(style)
                dismiss()
            }
        })


        val dialog = Dialog(requireActivity(), R.style.dialogStyle)
        dialog.setContentView(binding.root)

        return dialog
    }


    //Первоначальная настройка Spinner'а со стилями с учетом прошлого выбранного стиля
    //* Требуется, чтобы в viewModel обязательно было проинициализрован список всех стилей
    private fun initSpinner(lastStyle: StyleUser?) {

        val adapter = AdapterStyleSpinner(
            requireContext(),
            R.layout.item_spinner_styles,
            arrayStyles!!
        )
        binding.spinnerStyles.adapter = adapter

        if (lastStyle != null) {
            val selectedPosition = adapter.getPosition(lastStyle)
            binding.spinnerStyles.setSelection(selectedPosition)
        } else {
            binding.spinnerStyles.setSelection(0)
        }
    }



    companion object {
        fun newInstance(style: StyleUser?, listStyles: Array<StyleUser>, idDrawable: Int) : EditUserStyleDialogFragment {
            val args = Bundle().apply {
                putParcelable(ARG_STYLE, style)
                putParcelableArray(ARG_LIST_STYLES, listStyles)
                putInt(ARG_DRAWABLE_STYLE, idDrawable)
            }
            return EditUserStyleDialogFragment().apply {
                arguments = args
            }
        }
    }



    /**Адаптер для Spinner'а пользовательско стиля*/
    private inner class AdapterStyleSpinner(
        context: Context,
        resource: Int,
        val objects: Array<StyleUser>
    ): ArrayAdapter<StyleUser>(context, resource, objects) {

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomViewSelected(position)
        }

        private fun getCustomView(position: Int): View {
            val bindingItem = ItemSpinnerStylesBinding.inflate(requireActivity().layoutInflater)

            bindingItem.textViewUserStyle.text = objects.get(position).name

            return bindingItem.root
        }

        private fun getCustomViewSelected(position: Int): View {
            val bindingItem = ItemSpinnerStylesSelectedBinding.inflate(requireActivity().layoutInflater)

            bindingItem.textViewUserStyle.text = objects.get(position).name
            bindingItem.imageViewUserStyle.setImageResource(idDrawable!!)

            return bindingItem.root
        }
    }
}