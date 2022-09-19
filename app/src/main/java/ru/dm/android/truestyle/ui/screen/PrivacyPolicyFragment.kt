/**Фрагмент с политикой конфидециальности*/
package ru.dm.android.truestyle.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.dm.android.truestyle.databinding.FragmentPrivacyPolicyBinding
import ru.dm.android.truestyle.viewmodel.PrivacyPolicyViewModel

class PrivacyPolicyFragment : Fragment() {

    private lateinit var viewwModel: PrivacyPolicyViewModel
    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewwModel = ViewModelProvider(this).get(PrivacyPolicyViewModel::class.java)

        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        binding.webViewPrivacyPolicy.webChromeClient = object : WebChromeClient() { //webChromeClient - для обработки событий
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    binding.progressBarPrivacyPolicy.visibility = View.GONE
                } else {
                    binding.progressBarPrivacyPolicy.visibility = View.VISIBLE
                    binding.progressBarPrivacyPolicy.progress = newProgress
                }
            }
        }

        val root: View = binding.root
        binding.lifecycleOwner = this@PrivacyPolicyFragment

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Загрузилась статья - показываем её
        viewwModel.liveDataHtmlPrivacyPolicy.observe(viewLifecycleOwner, Observer {
            binding.webViewPrivacyPolicy.loadDataWithBaseURL(null, it, "text/html", "en_US", null)
        })

    }


    override fun onDestroy() {
        super.onDestroy()
        binding.webViewPrivacyPolicy.webChromeClient=null
        _binding = null
    }

}