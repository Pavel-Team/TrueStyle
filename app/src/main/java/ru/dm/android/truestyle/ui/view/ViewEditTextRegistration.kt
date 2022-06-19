/**Кастомный EditText для поля в регистрации*/
package ru.dm.android.truestyle.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.ItemArticleInTopicBinding
import ru.dm.android.truestyle.databinding.ViewEditTextRegistrationBinding


class ViewEditTextRegistration(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var mBinding: ViewEditTextRegistrationBinding;

    init {
//        mBinding = DataBindingUtil.inflate<ViewEditTextRegistrationBinding>(
//            LayoutInflater.from(context),
//            R.layout.view_edit_text_registration,
//            parent,
//            false
//        )
        mBinding = ViewEditTextRegistrationBinding.inflate(LayoutInflater.from(context))
    }

}