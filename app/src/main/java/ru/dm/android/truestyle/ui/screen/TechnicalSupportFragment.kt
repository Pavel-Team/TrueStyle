package ru.dm.android.truestyle.ui.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.dm.android.truestyle.R
import ru.dm.android.truestyle.databinding.FragmentTechnicalSupportBinding


class TechnicalSupportFragment : Fragment() {

    private var _binding: FragmentTechnicalSupportBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTechnicalSupportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this@TechnicalSupportFragment

        //Слушатель на кнопку назад
        binding.imageButtonBack.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        //Можно заменить на onLongClick или вообще добавить интенты
        binding.viewEmailTechSupport.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val clipboard: ClipboardManager =
                    activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Link technical support", resources.getString(R.string.email_technical_support))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, resources.getString(R.string.email_copy), Toast.LENGTH_SHORT).show()
            }
        })

        binding.viewVkTechSupport.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val clipboard: ClipboardManager =
                    activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Link technical support", resources.getString(R.string.vk_technical_support))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, resources.getString(R.string.vk_copy), Toast.LENGTH_SHORT).show()
            }
        })

        binding.viewTgTechSupport.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val clipboard: ClipboardManager =
                    activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Link technical support", resources.getString(R.string.tg_technical_support))
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, resources.getString(R.string.tg_copy), Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}